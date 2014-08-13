package ru

import java.io.File

import com.google.gson.Gson
import org.zkoss.zul.{DefaultTreeModel, AbstractTreeModel}
import org.zkoss.zul.ext.TreeSelectableModel

import scala.io.{Source, BufferedSource}

/**
* @author igor.kostromin
*         08.08.2014 11:39
*/
class TreeVm(var root: Node) extends AbstractTreeModel[Node](root) {
  override def isLeaf(node: Node): Boolean = {
    node.nodes == null || node.nodes.isEmpty
  }

  override def getChild(parent: Node, index: Int): Node = {
    //try {
      parent.nodes(index)
//    } catch {
//      case e: ArrayIndexOutOfBoundsException => {
//        System.out.println("ArrayIndexOutOfBoundsException")
//        return null
//      }
//    }
  }

  override def getChildCount(parent: Node): Int = {
    parent.nodes.size
  }

//  override def getIndexOfChild(parent: Node, child: Node): Int = {
//    0
//  }
}
