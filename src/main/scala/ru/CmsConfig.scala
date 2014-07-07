package ru

import scala.beans.BeanProperty

/**
 * User: igor.kostromin
 * Date: 05.07.2014
 * Time: 19:23
 */
class CmsConfig(@BeanProperty var rootNode: Node,
                 @BeanProperty var templates: Array[Template]) {
}

class Node(@BeanProperty var urlPrefix: String,
           @BeanProperty var view: String,
           @BeanProperty var nodes: Array[Node]) {
}

class Template(@BeanProperty var id: String,
                @BeanProperty var view: String,
                @BeanProperty var baseTemplate: String,
                @BeanProperty var regions: Map[String, String]) {
}