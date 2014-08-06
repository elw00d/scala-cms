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

/**
 *
 * @param moduleInstance
 * @param path Путь текущего запроса относительно модуля
 *             (при вызове render - всё, что дальше nodeUrlPrefix,
 *             при вызове handleAction - всё, что дальше nodeUrlPrefix/moduleInstanceId)
 * @param cmsContext
 * @param attributes
 */
class ModuleContext(var moduleInstance: ModuleInstance,
                    var path: String,
                     var cmsContext: CmsContext,
                     var attributes: java.util.HashMap[String, Object]) {
}

trait IModule {
  def render(moduleContext: ModuleContext): String

  def handleAction(moduleContext: ModuleContext): String
}

// Тестовый модуль для демонстрации возможностей cmsContext
class CurrentNodeModule extends IModule {
  override def render(moduleContext: ModuleContext): String = {
    moduleContext.cmsContext.node.urlPrefix
  }

  override def handleAction(moduleContext: ModuleContext): String = ???
}

