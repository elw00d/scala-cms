package ru.ace

import org.zkoss.zk.ui.HtmlBasedComponent
import org.zkoss.zk.ui.sys.ContentRenderer

/**
 * @author igor.kostromin
 *         8/18/2014
 */
class AceEditorComponent extends HtmlBasedComponent {
  var value: String = null

  def getValue: String = value

  def setValue(v: String) = {
    if (v != value) {
      value = v
      smartUpdate("value", value)
    }
  }

  override def renderProperties(renderer: ContentRenderer): Unit = {
    super.renderProperties(renderer)
    render(renderer, "value", value)
  }
}
