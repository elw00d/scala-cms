package ru

import scala.beans.BeanProperty
import scala.collection.immutable.HashMap

/**
 * @author igor.kostromin
 *         05.07.2014 19:23
 */
class CmsConfig(@BeanProperty var rootNode: Node,
                 @BeanProperty var templates: Array[Template],
                 @BeanProperty var modules: java.util.HashMap[String, String]) {
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
           @BeanProperty var nodes: Array[Node],
            @BeanProperty var attributes: java.util.HashMap[String, Object]) {
}

class Template(@BeanProperty var id: String,
                @BeanProperty var view: String,
                @BeanProperty var baseTemplate: String,
                @BeanProperty var regions: java.util.HashMap[String, String],
                @BeanProperty var attributes: java.util.HashMap[String, Object]) {
}