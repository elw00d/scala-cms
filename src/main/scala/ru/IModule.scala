package ru

import freemarker.cache.TemplateLoader

/**
 * @author igor.kostromin
 *         07.07.2014 23:07
 */
class CmsContext(var cmsConfig: CmsConfig,
                 var node: Node,
                 var templateLoader: TemplateLoader,
                  var dataContext: java.util.HashMap[String, Any]) {

}

trait IModule {
  def render(cmsContext: CmsContext): String
}

class CurrentNodeModule extends IModule {
  override def render(cmsContext: CmsContext): String = {
    cmsContext.node.urlPrefix
  }
}

class MenuModule extends IModule {
  override def render(cmsContex: CmsContext): String = {
    System.out.println(cmsContex.node.attributes.get("active-page"))
    ""
  }
}