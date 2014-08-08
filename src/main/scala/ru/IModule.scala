package ru

import java.io.StringWriter
import java.util
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import freemarker.cache.TemplateLoader
import freemarker.template.Configuration

import scala.collection.immutable.HashMap

/**
 * Common modules context (both active and inactive).
 *
 * @param moduleInstance reference to module instance info
 * @param cmsContext cmsContext available
 * @param attributes attributes merged from ModuleDefinition and ModuleInstance
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
 * Context of active module.
 *
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

abstract class ModuleResult {
}

class ContentResult(var content: String) extends ModuleResult {
}

class DirectResponseResult(var responseMutator: (HttpServletResponse) => Unit) extends ModuleResult {
}

trait IModule {
  /**
   *
   * @param moduleContext всегда присутствует
   * @param activeModuleContext null, если текущий запрос не относится к этому модулю
   *                            not null, если текущий запрос ведёт именно к этому модулю
   * @return
   */
  def service(moduleContext: ModuleContext, activeModuleContext: ActiveModuleContext): ModuleResult
}

// Тестовый модуль для демонстрации возможностей cmsContext
//class CurrentNodeModule extends IModule {
//  override def service(moduleContext: ModuleContext, activeModuleContext: ActiveModuleContext): ModuleResult = {
//    new ContentResult(moduleContext.cmsContext.node.urlPrefix)
//  }
//}

