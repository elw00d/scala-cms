package ru

import javax.servlet.http.HttpServletRequest

import freemarker.template.Configuration

/**
 * Context of current node being processing. Available in directives and modules code.
 *
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
