package ru

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Paths, Files}
import java.util

import com.google.gson.{GsonBuilder, Gson}
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation._
import org.zkoss.zk.ui.event.{Events, Event, EventListener}
import org.zkoss.zk.ui.select.Selectors
import org.zkoss.zk.ui.{Component, Executions}
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.event.TreeDataEvent
import org.zkoss.zul.{Messagebox, Window}

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
  var currentItemVm: Node = null
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
    System.out.println("MainVm.submit()")
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

  @Command(Array("delete"))
  def deleteNode() = {
    var path: java.util.List[Node] = new util.ArrayList[Node]()

    def getParentNode(parentNode: Node, node: Node): Node = {
      path.add(parentNode)
      if (parentNode.nodes != null) {
        val matchOption: Option[Node] = parentNode.nodes.filter((n: Node) => n == node).lastOption
        if (matchOption.nonEmpty)
          return parentNode

        var parentInChilds : Node = null
        parentNode.nodes.foreach((n: Node) => {
          val foundParent: Node = getParentNode(n, node)
          if (foundParent != null)
            parentInChilds = foundParent
        })
        if (parentInChilds == null)
          path.remove(parentNode)
        return parentInChilds
      }
      path.remove(parentNode)
      null
    }

    val parentNode: Node = getParentNode(treeVm.root, currentItemVm)
    System.out.println("Parent node: " + parentNode)

    if (parentNode == treeVm.getRoot) {
      // Deleting root node
      Messagebox.show("Can't delete root node")
    } else {
      // todo : confirm
      parentNode.nodes = parentNode.nodes.filter(_ != currentItemVm)
      //currentItemVm = null
      BindUtils.postNotifyChange(null, null, this, "treeVm")
      //treeVm.fireEvent(TreeDataEvent.STRUCTURE_CHANGED, 0, 0, 0)
      //BindUtils.postNotifyChange(null, null, this, "currentItemVm")
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