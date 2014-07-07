package ru

import java.util

import freemarker.core.Environment
import freemarker.template._

/**
 * @author igor.kostromin
 *         07.07.2014 12:11
 */
class RegionDirective extends TemplateDirectiveModel {
  override def execute(env: Environment,
                       params: util.Map[_, _],
                       loopVars: Array[TemplateModel],
                       body: TemplateDirectiveBody): Unit = {
    // Можно сделать что по умолчанию (если нет содержимого для региона) рендерится то, что
    // внутри директивы region
    val regionContentTemplateModel: TemplateModel = env.getDataModel.get("region_" + params.get("id"))
    val regionContent = regionContentTemplateModel.asInstanceOf[SimpleScalar].getAsString
    val f: Template = new Template("someName", regionContent, env.getConfiguration)
    f.process(env.getDataModel, env.getOut)
  }
}
