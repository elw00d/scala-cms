package ru

import java.io.File
import java.util.Comparator

import com.google.gson.Gson
import org.zkoss.bind.annotation.{ContextType, ContextParam, Init}
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.select.Selectors
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ext.Sortable
import org.zkoss.zul.{AbstractListModel, Window}

import scala.beans.BeanProperty
import scala.io.{Source, BufferedSource}

class TemplatesListModel(@BeanProperty var templates: Array[Template])
  extends AbstractListModel[Template]() with Sortable[Template] {

  override def sort(cmpr: Comparator[Template], ascending: Boolean): Unit = {
    //
  }

  override def getSortDirection(cmpr: Comparator[Template]): String = {
    null
  }

  override def getElementAt(index: Int): Template = {
    templates(index)
  }

  override def getSize: Int = {
    templates.length
  }
}

/**
 * @author igor.kostromin
 *         8/15/2014
 */
class TemplatesVm {
  @Wire("#win")
  var win: Window = null

  @BeanProperty var templatesVm: TemplatesListModel = null

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
    templatesVm = new TemplatesListModel(cmsConfig.templates)
  }
}
