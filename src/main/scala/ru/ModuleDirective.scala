package ru

import java.util

import freemarker.core.Environment
import freemarker.ext.beans.BeanModel
import freemarker.template.{SimpleScalar, TemplateDirectiveBody, TemplateModel, TemplateDirectiveModel}

/**
 * @author igor.kostromin
 *         07.07.2014 23:16
 */
class ModuleDirective extends TemplateDirectiveModel {
  override def execute(env: Environment,
                       params: util.Map[_, _],
                       loopVars: Array[TemplateModel],
                       body: TemplateDirectiveBody): Unit = {
    val cmsContext: CmsContext = env.getDataModel.get("cmsContext").asInstanceOf[BeanModel]
      .getWrappedObject.asInstanceOf[CmsContext]
    val className: String = cmsContext.cmsConfig.modules.get(params.get("id").asInstanceOf[SimpleScalar].getAsString)
    val module: IModule = Class.forName(className).getConstructor().newInstance().asInstanceOf[IModule]
    val renderedContent: String = module.render(cmsContext)
    env.getOut.write(renderedContent)
  }
}
