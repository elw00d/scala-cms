package ru.ace

import org.zkoss.zk.ui.{AbstractComponent, HtmlBasedComponent}
import org.zkoss.zk.ui.sys.ContentRenderer
import ru.ace

/**
 * @author igor.kostromin
 *         8/18/2014
 */
object AceEditorComponent extends HtmlBasedComponent {
  System.out.println("AceEditorComponent.init()")
  AbstractComponent.addClientEvent(classOf[AceEditorComponent], "onChange", 0 )
}

class AceEditorComponent extends HtmlBasedComponent {
  // "Call" our "static ctor"
  AceEditorComponent

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
