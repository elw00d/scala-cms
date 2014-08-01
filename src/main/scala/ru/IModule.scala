package ru

import java.io.StringWriter
import java.util

import freemarker.cache.TemplateLoader
import freemarker.template.Configuration

/**
 * @author igor.kostromin
 *         07.07.2014 23:07
 */
class CmsContext(var cmsConfig: CmsConfig,
                 var node: Node,
                 var freemarkerConfiguration: Configuration,
                  var dataContext: java.util.HashMap[String, Any]) {

}

trait IModule {
  def render(cmsContext: CmsContext, attributes: java.util.HashMap[String, Object]): String
}

class CurrentNodeModule extends IModule {
  override def render(cmsContext: CmsContext, attributes: java.util.HashMap[String, Object]): String = {
    cmsContext.node.urlPrefix
  }
}

class MenuModule extends IModule {
  override def render(cmsContex: CmsContext, attributes: java.util.HashMap[String, Object]): String = {
    System.out.println("Active page is " + cmsContex.node.attributes.get("active-page"))
    val template: freemarker.template.Template = cmsContex.freemarkerConfiguration.getTemplate(attributes.get("view").asInstanceOf[String])
    val dataContext = new util.HashMap[String, Object]()

    // todo : move this configuration code to CmsContext
    dataContext.put("baseUrl", "/webapp")

    dataContext.put("activePage", cmsContex.node.attributes.get("active-page"))
    val stringWriter: StringWriter = new StringWriter()
    template.process(dataContext, stringWriter)
    stringWriter.toString
  }
}