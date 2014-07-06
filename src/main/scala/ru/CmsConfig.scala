package ru

import scala.beans.BeanProperty

/**
 * User: igor.kostromin
 * Date: 05.07.2014
 * Time: 19:23
 */
class CmsConfig(@BeanProperty var rootNode: Node) {
//  var rootNode: Node = null
}

class Node(@BeanProperty var urlPrefix: String,
           @BeanProperty var view: String,
           @BeanProperty var nodes: Array[Node]) {
}