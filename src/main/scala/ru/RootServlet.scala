package ru

import java.io.File
import java.util
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import freemarker.cache.FileTemplateLoader
import freemarker.template.Configuration

import scala.util.control.Breaks._

import com.google.gson.Gson

import scala.io.{Source, BufferedSource}

/**
 * User: igor.kostromin
 * Date: 28.06.2014
 * Time: 10:55
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
    val file: BufferedSource = Source.fromFile("D:\\elwood\\my-repos\\scala-cms\\scala-cms\\cms\\config.json")
    val content: String = file.mkString
    val gson: Gson = new Gson()
    val cmsConfig: CmsConfig = gson.fromJson(content, classOf[CmsConfig] )

    // Get best match node
    System.out.println("Path: " + req.getServletPath)
    val matchNode: Node = bestMatch(cmsConfig.rootNode, req.getServletPath)

    val method: String = req.getMethod
    val handler = method match {
      case "GET" => (request: HttpServletRequest, response: HttpServletResponse) => {
        if (matchNode == null || matchNode.view == null) {
          response.getWriter.print("Not found")
        } else {
          val templateLoader = new FileTemplateLoader(new File("D:\\elwood\\my-repos\\scala-cms\\scala-cms\\cms\\views"))
          var cfg = new Configuration()
          cfg.setTemplateLoader(templateLoader)
          var template = cfg.getTemplate(matchNode.view)
          val dataContext = new util.HashMap[String, Any]
          dataContext.put("region", new RegionDirective())
          dataContext.put("someVar", "SomeVariableContent")
          dataContext.put("region_main", "MyRegionContent ${someVar}")
          template.process(dataContext, response.getWriter)
        }
      }
      case _ => (request: HttpServletRequest, response: HttpServletResponse) => {
        response.getWriter.print("Handler not found")
      }
    }
    handler(req, resp)
  }
}
