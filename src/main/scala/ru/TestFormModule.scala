package ru

import java.io.StringWriter
import java.util

import freemarker.template.SimpleScalar

/**
 * @author igor.kostromin
 *         06.08.2014 13:59
 */
class TestFormModule extends IModule {
  override def service(moduleContext: ModuleContext, activeModuleContext: ActiveModuleContext): String = {
    if (activeModuleContext != null) {
      System.out.println("modulePath: " + activeModuleContext.path)
      return "Action!"
    }
    val template: freemarker.template.Template = moduleContext.cmsContext.freemarkerConfiguration.getTemplate("testFormModule.ftl")
    val dataContext = new util.HashMap[String, Object]()
    dataContext.put("baseModuleUrl", moduleContext.baseModuleUrl)
    val stringWriter = new StringWriter()
    template.process(dataContext, stringWriter)
    stringWriter.toString
  }
}
