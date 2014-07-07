package ru

import scala.beans.BeanProperty
import scala.collection.immutable.HashMap

/**
 * User: igor.kostromin
 * Date: 05.07.2014
 * Time: 19:23
 */
class CmsConfig(@BeanProperty var rootNode: Node,
                 @BeanProperty var templates: Array[Template]) {
  private var map: collection.mutable.HashMap[String,Template] = null

  // todo : do this in ctor
  def getTemplateById(id: String) : Template = {
    if (map == null){
      map = new collection.mutable.HashMap[String, Template]
      this.templates.foreach(tmpl => map.put(tmpl.id, tmpl))
    }
    map.get(id).orNull
  }
}

class Node(@BeanProperty var urlPrefix: String,
           @BeanProperty var template: String,
           @BeanProperty var nodes: Array[Node]) {
}

class Template(@BeanProperty var id: String,
                @BeanProperty var view: String,
                @BeanProperty var baseTemplate: String,
                @BeanProperty var regions: java.util.HashMap[String, String]) {
}