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

// Тестовый модуль для демонстрации возможностей cmsContext
class CurrentNodeModule extends IModule {
  override def render(cmsContext: CmsContext, attributes: java.util.HashMap[String, Object]): String = {
    cmsContext.node.urlPrefix
  }
}

