package ru

import java.util
import javax.servlet.http.HttpServletRequest

import freemarker.core.Environment
import freemarker.ext.beans.BeanModel
import freemarker.template.{SimpleScalar, TemplateDirectiveBody, TemplateModel, TemplateDirectiveModel}

import scala.collection.mutable

/**
 * @author igor.kostromin
 *         07.07.2014 23:16
 */
class ModuleDirective extends TemplateDirectiveModel {
  override def execute(env: Environment,
                       params: util.Map[_, _],
                       loopVars: Array[TemplateModel],
                       body: TemplateDirectiveBody): Unit = {
//    val originalHttpRequest: HttpServletRequest = env.getDataModel.get("originalHttpRequest").asInstanceOf[BeanModel]
//      .getWrappedObject.asInstanceOf[HttpServletRequest]
//    val cmsContext: CmsContext = env.getDataModel.get("cmsContext").asInstanceOf[BeanModel]
//      .getWrappedObject.asInstanceOf[CmsContext]
//    val instanceId: String = params.get("id").asInstanceOf[SimpleScalar].getAsString
//    val instance: ModuleInstance = cmsContext.node.modules.filter((instance: ModuleInstance) => instance.getInstanceId == instanceId)
//      .last
//    val moduleDefinition: ModuleDefinition = cmsContext.cmsConfig.moduleDefinitions.get(instance.getDefinitionId)
//    val module: IModule = Class.forName(moduleDefinition.className).getConstructor().newInstance().asInstanceOf[IModule]
//
//    val restPath: String = env.getDataModel.get("restPath").asInstanceOf[SimpleScalar].getAsString
//
//    val moduleContext: ModuleContext = new ModuleContext(instance, restPath, cmsContext, moduleDefinition.attributes)
//    val renderedContent: String = module.render(moduleContext)
//    env.getOut.write(renderedContent)
    val map: mutable.HashMap[String, String] = env.getDataModel.get("modulesContentMap").asInstanceOf[BeanModel]
      .getWrappedObject.asInstanceOf[mutable.HashMap[String, String]]
    val instanceId: String = params.get("id").asInstanceOf[SimpleScalar].getAsString
    env.getOut.write(map.get(instanceId).get)
  }
}
