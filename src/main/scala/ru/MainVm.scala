package ru

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Paths, Files}
import java.util

import com.google.gson.{GsonBuilder, Gson}
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation._
import org.zkoss.zk.ui.event.{DropEvent, Events, Event, EventListener}
import org.zkoss.zk.ui.select.Selectors
import org.zkoss.zk.ui.{Component, Executions}
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.event.TreeDataEvent
import org.zkoss.zul._

import scala.beans.BeanProperty
import scala.io.{Source, BufferedSource}

/**
 * @author igor.kostromin
 *         08.08.2014 12:04
 */
class NodeVm {
  @BeanProperty var node : Node = null

  @Init
  def init(@ExecutionArgParam("node") node: Node) = {
    System.out.println("Node: " + node)
    this.node = node
  }

  def this(node: Node) {
    this()
    this.node = node
  }

  @Command(Array("submit"))
  def submit() = {
    System.out.println("NodeVm.submit()")
  }
}

class MainVm {
  @BeanProperty var treeVm: TreeVm = null
  // Это св-во отражает текущий selectedItem дерева
  var currentItemVm: Node = null
  // А это св-во содержит враппер для selectedItem, который редактируется
  // details-представлением node-edit.zul. При изменении selectedItem дерева
  // изменяется currentItemVm, вызывается метод setCurrentItemVm, в котором мы
  // явно вызываем notifyChange("currentItemVmVm") чтобы дочернее представление
  // обновило данные
  @BeanProperty var currentItemVmVm: NodeVm = new NodeVm()

  var newItemVm: NodeVm = null

  def getCurrentItemVm: Node = currentItemVm
  def setCurrentItemVm(node: Node): Unit = {
    currentItemVm = node
    currentItemVmVm = new NodeVm(node)
    BindUtils.postNotifyChange(null, null, this, "currentItemVmVm")
    BindUtils.postNotifyChange(null, null, this, "canExecute")
  }

  @Wire("#win")
	var win: Window = null

  @Init
	def init(@ContextParam(ContextType.VIEW) view: Component) {
    System.out.println("init called")
		Selectors.wireComponents(view, this, false)
	}

  loadFromFile()
  
  def loadFromFile() = {
    val basedir: String = System.getProperty("cms.basedir")
    val file: BufferedSource = Source.fromFile(new File(basedir, "config_v2.json"))
    val content: String = file.mkString
    val gson: Gson = new Gson()
    val cmsConfig: CmsConfig = gson.fromJson(content, classOf[CmsConfig])

    val rootNode: Node = new Node(null, null, Array(cmsConfig.rootNode), null, null)
    treeVm = new TreeVm(rootNode)
  }

  @Command(Array("add"))
  def add() {
    System.out.println("Add command")
    val dataArgs: util.HashMap[String, Object] = new util.HashMap[String, Object]()
    newItemVm = new NodeVm(new Node("", "", Array(), new util.HashMap[String, Object](), Array()))
    dataArgs.put("nodeVm", newItemVm)
    dataArgs.put("title", "Add node")
    val dlg = Executions.createComponents("/dialog.zul", win, dataArgs).asInstanceOf[Window]
    win.addEventListener("onDlgClosed", new CloseEventListener[Event]())
    dlg.doModal()
  }

  @Command(Array("edit"))
  def edit() {
    System.out.println("Edit command")
    val dataArgs = new util.HashMap[String, Object]()
    dataArgs.put("nodeVm", currentItemVmVm)
    val dlg: Window = Executions.createComponents("/dialog.zul", win, dataArgs).asInstanceOf[Window]
    win.addEventListener("onDlgClosed", new CloseEventListener[Event]())
    dlg.doModal()
  }

  class CloseEventListener[T <: Event] extends EventListener[T] {
    override def onEvent(event: T): Unit = {
      // Чтобы в форме справа отразились изменения, сделанные в диалоговом окне
      if (MainVm.this.newItemVm != null) {
        if (currentItemVm.nodes == null) {
          currentItemVm.nodes = Array()
        }
        currentItemVm.nodes +:= newItemVm.node
        newItemVm = null
        // Обновляем treeVm, чтобы дерево показало добавленный элемент
        BindUtils.postNotifyChange(null, null, MainVm.this, "treeVm")
      } else {
        BindUtils.postNotifyChange(null, null, MainVm.this, "currentItemVmVm")
      }
    }
  }

  def canExecute(cmd: String) : Boolean = {
    System.out.println(String.format("Can execute (%s)", cmd))
    if (cmd == "submit")
      return this.currentItemVm != null
    true
  }

  @Command(Array("submit"))
  def submit() = {
    // Обновляем представление изменённого узла в дереве
    BindUtils.postNotifyChange(null, null, this, "treeVm")
  }

  @Command(Array("saveConfig"))
  def saveConfig() = {
    System.out.println("saveConfig()")
    val basedir: String = System.getProperty("cms.basedir")
    val file: BufferedSource = Source.fromFile(new File(basedir, "config_v2.json"))
    val content: String = file.mkString
    val gson: Gson = new GsonBuilder().setPrettyPrinting().create()
    val cmsConfig: CmsConfig = gson.fromJson(content, classOf[CmsConfig] )
    cmsConfig.rootNode = treeVm.root
    val json: String = gson.toJson(cmsConfig)
    Files.write(Paths.get(basedir, "config_v2_saved.json"), json.getBytes(StandardCharsets.UTF_8))
  }

  @Command(Array("cancelSave"))
  def cancelSave() = {
    System.out.println("cancelSave()")
    loadFromFile()
    BindUtils.postNotifyChange(null, null, this, "treeVm")
  }

  @Command(Array("drop"))
  def drop(@BindingParam("event") event: Event) = {
    System.out.println("drop command")
    // The dragged target is a TreeRow belongs to an
    // Treechildren of TreeItem.
    val draggedItem: Treeitem = event.asInstanceOf[DropEvent].getDragged.getParent.asInstanceOf[Treeitem]
    val draggedNode: Node = draggedItem.getValue[Node]
    val parentItem: Treeitem = event.getTarget.getParent.asInstanceOf[Treeitem]
    val parentResult: (Node, Array[Int]) = getParentNode(treeVm.root, draggedNode)
    val oldParent: Node = parentResult._1

    val index: Int = oldParent.nodes.indexOf(draggedNode)
    oldParent.nodes = oldParent.nodes.filter(_ != draggedNode)
    treeVm.fireEvent(oldParent, index, index, TreeDataEvent.INTERVAL_REMOVED)

    val node: Node = parentItem.getValue[Node]
    if (node.nodes == null)
      node.nodes = Array()
    node.nodes +:= draggedNode
    treeVm.fireEvent(node, 0, 0, TreeDataEvent.INTERVAL_ADDED)

    val list: util.ArrayList[Node] = new util.ArrayList[Node]()
    list.add(draggedNode)
    treeVm.setSelection(list)
    //BindUtils.postNotifyChange(null, null, this, "treeVm")
  }

  def getParentNode(parentNode: Node, node: Node): (Node, Array[Int]) = {
    val path: java.util.List[Node] = new util.ArrayList[Node]()
    val pathInts: java.util.List[Int] = new util.ArrayList[Int]()

    def getParentNodeInner(parentNode: Node, node: Node): Node = {
      if (path.isEmpty) {
        pathInts.add(0)
      } else {
        pathInts.add(path.get(path.size() - 1).nodes.indexOf(parentNode))
      }
      //path.add(parentNode)

      if (parentNode.nodes != null) {
        val matchOption: Option[Node] = parentNode.nodes.filter((n: Node) => n == node).lastOption
        if (matchOption.nonEmpty)
          return parentNode

        var parentInChilds: Node = null
        parentNode.nodes.foreach((n: Node) => {
          val foundParent: Node = getParentNodeInner(n, node)
          if (foundParent != null)
            parentInChilds = foundParent
        })
        if (parentInChilds == null) {
          //path.remove(parentNode)
          pathInts.remove(pathInts.size() - 1)
        }
        return parentInChilds
      }
      //path.remove(parentNode)
      pathInts.remove(pathInts.size() - 1)
      null
    }

    val parent: Node = getParentNodeInner(parentNode, node)
    val ints: Array[Int] = new Array[Int](pathInts.size())
    for ( i <- 0 to pathInts.size() - 1) {
      ints(i) = pathInts.get(i)
    }
    (parent, ints)
  }

  @Command(Array("delete"))
  def deleteNode() = {
    //var path: java.util.List[Node] = new util.ArrayList[Node]()
    //var pathInts: java.util.List[Int] = new util.ArrayList[Int]()

    val parentResult: (Node, Array[Int]) = getParentNode(treeVm.root, currentItemVm)
    val parentNode: Node = parentResult._1
    val pathInts = parentResult._2
    System.out.println("Parent node: " + parentNode)

    if (parentNode == treeVm.getRoot) {
      // Deleting root node
      Messagebox.show("Can't delete root node")
    } else {
      // todo : confirm
      val index: Int = parentNode.nodes.indexOf(currentItemVm)
      parentNode.nodes = parentNode.nodes.filter(_ != currentItemVm)
      BindUtils.postNotifyChange(null, null, this, "treeVm")
      treeVm.fireEvent(TreeDataEvent.INTERVAL_REMOVED,
        pathInts,
        index,
        index)
      //treeVm.fireEvent(parentNode, index, index, TreeDataEvent.INTERVAL_REMOVED)
      // Очищаем selection, т.к. fireEvent почему-то не исправляет selectionPath внутри
      // AbstractTreeModel (а должно по идее) в случае удаления последнего элемента
      // todo : оформить багрепорт в ZK
      treeVm.clearSelection()
      // todo : убрать это, когда ZK Tree научится понимать, что при clearSelection()
      // нужно еще и selectedItem обновлять в null
      setCurrentItemVm(null)
    }
  }
}

class DialogVm {
  @Wire("#dlg")
	var dlg: Window = null

  @Init
	def init(@ContextParam(ContextType.VIEW) view: Component) {
		Selectors.wireComponents(view, this, false)
	}

  @Command(Array("submit"))
  def submit() = {
    System.out.println("DialogVm.submit()")
    Events.sendEvent("onDlgClosed", dlg.getParent, null)
    dlg.detach()
  }
}