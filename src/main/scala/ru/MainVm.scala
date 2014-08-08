package ru

import java.io.File
import java.util

import com.google.gson.Gson
import org.zkoss.bind.annotation.{Init, ContextType, ContextParam, Command}
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
class NodeVm(urlPrefix: String, template: String, nodes: Array[Node], attributes: java.util.HashMap[String, Object],
             modules: Array[ModuleInstance]) extends Node(urlPrefix, template, nodes, attributes, modules) {
  @BeanProperty var treeItem : Node = null
}

class MainVm {
  @BeanProperty var treeVm: TreeVm = null
  @BeanProperty var currentItemVm: NodeVm = null

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

  currentItemVm = new NodeVm("urlPrefix", "template!", null, null, null)

  @Command(Array("add"))
  def add() {
    System.out.println("Add command")
  }

  @Command(Array("edit"))
  def edit() {
    System.out.println("Edit command")
    val w: Window = new Window("ZK IM - ", "normal", true)
    w.setPosition("parent")
    w.setParent(win)
    val dataArgs = new util.HashMap[String, String]()
    dataArgs.put("name", "testName")
    Executions.createComponents("/dialog.zul", w, dataArgs)
    w.doModal()
  }

  def canExecute(cmd: String) : Boolean = {
    System.out.println(String.format("Can execute (%s)", cmd))
    true
  }
}
