package ru.ace

import org.zkoss.bind.BindUtils

import scala.beans.BeanProperty

/**
 * @author igor.kostromin
 *         23.08.2014 12:23
 */
class AceEditorTestVm {
  var text: String = "Initial value"

  def getText(): String = {
    text
  }

  def setText(text: String) = {
    this.text = text
    BindUtils.postNotifyChange(null, null, this, "text")
  }

}
