package ru

import java.io.File
import java.util

import com.google.gson.Gson
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation._
import org.zkoss.zk.ui.select.Selectors
import org.zkoss.zk.ui.{Component, Executions}
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Window

import scala.beans.BeanProperty
import scala.io.{Source, BufferedSource}

/**
 * @author igor.kostromin
 *         08.08.2014 12:04
 */
class NodeVm {
  @BeanProperty var node : Node = null

  @Init
  def init(@BindingParam("node") node: Node) = {
    System.out.println("Node: " + node.toString)
    this.node = node
  }

  def this(node: Node) {
    this()
    this.node = node
  }
}

class MainVm {
  @BeanProperty var treeVm: TreeVm = null
  var currentItemVm: Node = null
  @BeanProperty var currentItemVmVm: NodeVm = null

  def getCurrentItemVm: Node = currentItemVm
  def setCurrentItemVm(node: Node): Unit = {
    currentItemVm = node
    currentItemVmVm = new NodeVm(node)
    BindUtils.postNotifyChange(null, null, this, "currentItemVmVm")
  }

  @Wire("#win")
	var win: Window = null

  @Init
	def init(@ContextParam(ContextType.VIEW) view: Component) {
    System.out.println("init called")
		Selectors.wireComponents(view, this, false)
	}

  val basedir: String = System.getProperty("cms.basedir")
  val file: BufferedSource = Source.fromFile(new File(basedir, "config_v2.json"))
  val content: String = file.mkString
  val gson: Gson = new Gson()
  val cmsConfig: CmsConfig = gson.fromJson(content, classOf[CmsConfig] )

  val rootNode : Node = new Node(null, null, Array(cmsConfig.rootNode), null, null)
  treeVm = new TreeVm(rootNode)

  //currentItemVm = new Node("urlPrefix", "template!", null, null, null)

  @Command(Array("add"))
  def add() {
    System.out.println("Add command")
  }

  @Command(Array("edit"))
  def edit() {
    System.out.println("Edit command")
//    val w: Window = new Window("ZK IM - ", "normal", true)
//    w.setPosition("parent")
//    w.setParent(win)
    val dataArgs = new util.HashMap[String, Object]()
    dataArgs.put("node", currentItemVm)
    Executions.createComponents("/dialog.zul", win, dataArgs).asInstanceOf[Window]
    .doModal()
//    w.doModal()
  }

  def canExecute(cmd: String) : Boolean = {
    System.out.println(String.format("Can execute (%s)", cmd))
    true
  }
}
