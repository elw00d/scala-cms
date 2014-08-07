package ru

import java.io.StringWriter
import java.util
import javax.servlet.http.HttpServletRequest

import freemarker.cache.TemplateLoader
import freemarker.template.Configuration

import scala.collection.immutable.HashMap

/**
 * @author igor.kostromin
 *         07.07.2014 23:07
 */
class CmsContext(var cmsConfig: CmsConfig,
                    var node: Node,
                    var freemarkerConfiguration: Configuration,
                    var baseUrl: String,
                    var matchedPath: String,
                    var rawRequest : HttpServletRequest) {
}

/**
 *
 * @param moduleInstance
 *
 * @param cmsContext
 * @param attributes
 */
class ModuleContext(var moduleInstance: ModuleInstance,
                     var cmsContext: CmsContext,
                     var attributes: java.util.HashMap[String, Object]) {
  def baseModuleUrl: String = {
    cmsContext.baseUrl +
        "/" +
      cmsContext.matchedPath +
        "/" +
      moduleInstance.instanceId
  }
}

/**
 * @param path Путь текущего запроса относительно модуля
 *             (при вызове render - всё, что дальше nodeUrlPrefix,
 *             при вызове handleAction - всё, что дальше nodeUrlPrefix/moduleInstanceId)
 * @param method GET или POST, остальные (пока?) не поддерживаются для модулей
 * @param params Коллекция http параметров запроса
 */
class ActiveModuleContext(var path: String,
                          var method: String,
                           var params: java.util.Map[String, Array[String]]) {
}

trait IModule {
  /**
   *
   * @param moduleContext всегда присутствует
   * @param activeModuleContext null, если текущий запрос не относится к этому модулю
   *                            not null, если текущий запрос ведёт именно к этому модулю
   * @return
   */
  def service(moduleContext: ModuleContext, activeModuleContext: ActiveModuleContext): String
}

// Тестовый модуль для демонстрации возможностей cmsContext
class CurrentNodeModule extends IModule {
  override def service(moduleContext: ModuleContext, activeModuleContext: ActiveModuleContext): String = {
    moduleContext.cmsContext.node.urlPrefix
  }
}

