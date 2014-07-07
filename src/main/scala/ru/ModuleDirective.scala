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
    val cmsConfig: CmsConfig = env.getDataModel.get("cmsConfig").asInstanceOf[BeanModel]
      .getWrappedObject.asInstanceOf[CmsConfig]
    val className: String = cmsConfig.modules.get(params.get("id").asInstanceOf[SimpleScalar].getAsString)
    val module: IModule = Class.forName(className).getConstructor().newInstance().asInstanceOf[IModule]
    val renderedContent: String = module.render(new CmsContext(cmsConfig,
      new Node("urlPrefix !", null, null),
      null,
      null))
    env.getOut.write(renderedContent)
  }
}
