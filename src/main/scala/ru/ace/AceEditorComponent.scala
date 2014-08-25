package ru.ace

import org.zkoss.json.JSONObject
import org.zkoss.zk.ui.event.{Event, Events}
import org.zkoss.zk.ui.sys.ContentRenderer
import org.zkoss.zk.ui.{AbstractComponent, HtmlBasedComponent}

/**
 * @author igor.kostromin
 *         8/18/2014
 */
object AceEditorComponent extends HtmlBasedComponent {
  System.out.println("AceEditorComponent.init()")
  AbstractComponent.addClientEvent(classOf[AceEditorComponent], "onChange", 0 )
  AbstractComponent.addClientEvent(classOf[AceEditorComponent], "onChanged", 0 )
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

  // Так как этот метод называется "onChanged", то он и будет вызываться
  // при получении соответствующего события
  def onChange(e: Event ): Unit = {
    // Сначала обновляем значение в свойстве
    val newValue = e.getData.asInstanceOf[JSONObject].get("text").asInstanceOf[String]
    value = newValue
    // А теперь инициируем событие, по которому будет сделать binding (например,
    // будет вызван setter в связанной view model)
    Events.sendEvent("onChanged", this, null)
  }

  override def renderProperties(renderer: ContentRenderer): Unit = {
    super.renderProperties(renderer)
    render(renderer, "value", value)
  }
}
