package ru

import java.io.StringWriter
import java.util

import freemarker.template.SimpleScalar

/**
 * @author igor.kostromin
 *         06.08.2014 13:59
 */
class TestFormModule extends IModule {
  override def render(moduleContext: ModuleContext): String = {
    System.out.println("modulePath: " + moduleContext.path)
    val template: freemarker.template.Template = moduleContext.cmsContext.freemarkerConfiguration.getTemplate("testFormModule.ftl")
    val dataContext = new util.HashMap[String, Object]()
    // todo : make this sweet
    dataContext.put("baseModuleUrl",
      moduleContext.cmsContext.dataContext.get("baseUrl").asInstanceOf[String] +
      moduleContext.cmsContext.dataContext.get("matchedPath").asInstanceOf[String] +
        "/" +
      moduleContext.moduleInstance.instanceId
    )
    val stringWriter = new StringWriter()
    template.process(dataContext, stringWriter)
    stringWriter.toString
  }

  override def handleAction(moduleContext: ModuleContext): String = {
    ""
  }
}
