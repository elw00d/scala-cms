package ru

import java.io.StringWriter

/**
 * Рендерит меню.
 * Конфигурируется атрибутом view
 * В ftl, на который ссылается view, передаётся переменная activePage
 * (которая берётся из атрибутов current node)
 *
 * @author igor.kostromin
 *         01.08.2014 14:07
 */
class MenuModule extends IModule {
  override def render(cmsContex: CmsContext, attributes: java.util.HashMap[String, Object]): String = {
    System.out.println("Active page is " + cmsContex.node.attributes.get("active-page"))
    val template: freemarker.template.Template = cmsContex.freemarkerConfiguration.getTemplate(attributes.get("view").asInstanceOf[String])
    val dataContext = new java.util.HashMap[String, Object]()

    // todo : move this configuration code to CmsContext
    dataContext.put("baseUrl", "/webapp")

    dataContext.put("activePage", cmsContex.node.attributes.get("active-page"))
    val stringWriter: StringWriter = new StringWriter()
    template.process(dataContext, stringWriter)
    stringWriter.toString
  }
}
