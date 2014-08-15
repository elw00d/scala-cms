package ru

import com.google.gson.annotations.SerializedName

import scala.beans.BeanProperty
import scala.collection.immutable.HashMap

/**
 * CMS configuration deserialized form json config file.
 *
 * @author igor.kostromin
 *         05.07.2014 19:23
 */
class CmsConfig(@BeanProperty var rootNode: Node,
                 @BeanProperty var templates: Array[Template],
                 @BeanProperty var moduleDefinitions: java.util.HashMap[String, ModuleDefinition]) {
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

class ModuleDefinition(@BeanProperty var className: String,
              @BeanProperty var attributes: java.util.HashMap[String, Object]) {
}

class ModuleInstance(@BeanProperty var definitionId :String,
                      @BeanProperty var instanceId: String,
                      @BeanProperty var attributes: java.util.HashMap[String, Object]) {
}

class Node(@BeanProperty var urlPrefix: String,
           @BeanProperty var template: String,
           @BeanProperty var nodes: Array[Node],
            @BeanProperty var attributes: java.util.HashMap[String, Object],
            @BeanProperty var modules: Array[ModuleInstance]) {
  override def toString: String = urlPrefix

  def isParentOf(node: Node) : Boolean = {
    if (nodes == null) return false

    var found: Boolean = false
    def searchRecursive(currentParent: Node): Unit = {
      if (currentParent.nodes != null) {
        for (child <- currentParent.nodes) {
          if (found) return
          else {
            if (child == node) {
              found = true
              return
            }
            searchRecursive(child)
          }
        }
      }
    }

    searchRecursive(this)
    found
  }
}

class Template(@BeanProperty var id: String,
                @BeanProperty var view: String,
                @BeanProperty var baseTemplate: String,
                @BeanProperty var regions: java.util.HashMap[String, String],
                @BeanProperty var attributes: java.util.HashMap[String, Object]) {
  override def toString: String = id
}