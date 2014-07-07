package ru

import java.io.File
import java.util
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import freemarker.cache.FileTemplateLoader
import freemarker.template.Configuration

import scala.collection.mutable
import scala.collection.JavaConversions._

import com.google.gson.Gson

import scala.io.{Source, BufferedSource}

/**
 * @author igor.kostromin
 *         28.06.2014 10:55
 */
@WebServlet(value=Array("/"), name = "rootServlet")
class RootServlet extends HttpServlet {
  def bestMatch(rootNode: Node, reqPath: String): Node = {
    def matches (node: Node, prefix: String): (Boolean, String) = {
      // Нормализуем ServletPath - убираем первый и последний слеши, двойные слеши
      // заменяем на одинарные
      val normalizedPrefix = prefix.split("/").filter(!_.isEmpty).mkString("/")
      val normalizedNodePrefix = node.getUrlPrefix.split("/").filter(!_.isEmpty).mkString("/")
      if (normalizedPrefix.startsWith(normalizedNodePrefix))
        return (true, normalizedPrefix.substring(normalizedNodePrefix.length))
      (false, reqPath)
    }

    var prefix = reqPath
    var candidates: List[Node] = List(rootNode)
    var bestMatch: Node = null
    while (candidates != null) {
      val matchedNode: Option[Node] = candidates.find((node: Node) => {
        val matchResult: (Boolean, String) = matches(node, prefix)
        matchResult._1
      })
      if (matchedNode.isEmpty)
        return bestMatch
      val matchResult: (Boolean, String) = matches(matchedNode.get, prefix)
      bestMatch = matchedNode.get
      prefix = matchResult._2
      if (matchedNode.get.nodes != null)
        candidates = matchedNode.get.nodes.toList
      else
        candidates = null
    }
    bestMatch
  }

  override def service(req: HttpServletRequest, resp: HttpServletResponse) = {
    val file: BufferedSource = Source.fromFile("d:\\all\\scala\\webapp\\cms\\config_v2.json")
    val content: String = file.mkString
    val gson: Gson = new Gson()
    val cmsConfig: CmsConfig = gson.fromJson(content, classOf[CmsConfig] )

    // Get best match node
    System.out.println("Path: " + req.getServletPath)
    val matchNode: Node = bestMatch(cmsConfig.rootNode, req.getServletPath)

    val method: String = req.getMethod
    val handler = method match {
      case "GET" => (request: HttpServletRequest, response: HttpServletResponse) => {
        if (matchNode == null || matchNode.template == null) {
          response.getWriter.print("Not found")
        } else {
          val templateLoader = new FileTemplateLoader(new File("d:\\all\\scala\\webapp\\cms\\views"))
          val cfg = new Configuration()
          cfg.setTemplateLoader(templateLoader)

          def getRootView(templateId: String, regionsMap: mutable.HashMap[String, String]) : String = {
            var currentTemplate = cmsConfig.getTemplateById(templateId)
            // Если шаблона с таким именем нет, считаем, что это и есть view
            if (currentTemplate == null) return templateId
            while (currentTemplate.baseTemplate != null){
              // Если есть какие-то регионы в текущем определении шаблона,
              // добавляем их в общий словарь
              if (currentTemplate.regions != null){
                currentTemplate.regions.foreach((tuple: (String, String)) => {
                  regionsMap.put(tuple._1, getRootView(tuple._2, regionsMap))
                })
              }
              currentTemplate = cmsConfig.getTemplateById(currentTemplate.baseTemplate)
            }
            currentTemplate.view
          }

          var regions = new mutable.HashMap[String, String]()
          val view = getRootView(matchNode.template, regions)

          val ftlTemplate = cfg.getTemplate(view)
          val dataContext = new util.HashMap[String, Any]
          dataContext.put("region", new RegionDirective)
          dataContext.put("module", new ModuleDirective)
          dataContext.put("cmsConfig", cmsConfig)

          regions.foreach((tuple: (String, String)) => dataContext.put(
            "region_" + tuple._1, cfg.getTemplate(tuple._2)
          ))

          //dataContext.put("someVar", "SomeVariableContent")
          //dataContext.put("region_main", "MyRegionContent ${someVar}")
          ftlTemplate.process(dataContext, response.getWriter)
        }
      }
      case _ => (request: HttpServletRequest, response: HttpServletResponse) => {
        response.getWriter.print("Handler not found")
      }
    }
    handler(req, resp)
  }
}
