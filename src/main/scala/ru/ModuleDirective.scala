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
    val map: mutable.HashMap[String, String] = env.getDataModel.get("modulesContentMap").asInstanceOf[BeanModel]
      .getWrappedObject.asInstanceOf[mutable.HashMap[String, String]]
    val instanceId: String = params.get("id").asInstanceOf[SimpleScalar].getAsString
    env.getOut.write(map.get(instanceId).get)
  }
}
