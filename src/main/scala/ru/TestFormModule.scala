package ru

import java.io.StringWriter
import java.util
import javax.servlet.http.HttpServletResponse

import freemarker.template.SimpleScalar

/**
 * @author igor.kostromin
 *         06.08.2014 13:59
 */
class TestFormModule extends IModule {
  override def service(moduleContext: ModuleContext, activeModuleContext: ActiveModuleContext): ModuleResult = {
    if (activeModuleContext != null && activeModuleContext.method == "POST") {
      System.out.println("modulePath: " + activeModuleContext.path)
      val entered: String = activeModuleContext.params.get("name")(0)
      val template: freemarker.template.Template = moduleContext.cmsContext.freemarkerConfiguration.getTemplate("testFormModule.ftl")
      val dataContext = new util.HashMap[String, Object]()
      dataContext.put("baseModuleUrl", moduleContext.baseModuleUrl)
      dataContext.put("entered", entered)
      val stringWriter = new StringWriter()
      template.process(dataContext, stringWriter)
      if (entered == "go")
        new DirectResponseResult((response: HttpServletResponse) => {
          response.sendRedirect("/")
        })
      else
        new ContentResult(stringWriter.toString)
    } else {
      val template: freemarker.template.Template = moduleContext.cmsContext.freemarkerConfiguration.getTemplate("testFormModule.ftl")
      val dataContext = new util.HashMap[String, Object]()
      dataContext.put("baseModuleUrl", moduleContext.baseModuleUrl)
      val stringWriter = new StringWriter()
      template.process(dataContext, stringWriter)
      new ContentResult(stringWriter.toString)
    }
  }
}
