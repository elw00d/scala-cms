package ru

import java.io.{FileInputStream, File}
import java.util
import javax.servlet.ServletOutputStream
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import freemarker.cache.FileTemplateLoader
import freemarker.template.{SimpleScalar, Configuration}

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
  def normalize(path: String): String = {
    path.split("/").filter(!_.isEmpty).mkString("/")
  }

  /**
   * Возвращает Tuple(Node, String, String)
   * Node - лучшее совпадение с урлом (или null, если не найдено)
   * String - совпавшая часть урла (или пустая строка, если совпадение не найдено)
   * String - оставшаяся часть урла
   * @return
   */
  def bestMatch(rootNode: Node, reqPath: String): (Node, String, String) = {
    def matches (node: Node, prefix: String): (Boolean, String) = {
      // Нормализуем ServletPath - убираем первый и последний слеши, двойные слеши
      // заменяем на одинарные
      val normalizedPrefix = prefix.split("/").filter(!_.isEmpty).mkString("/")
      val normalizedNodePrefix = node.getUrlPrefix.split("/").filter(!_.isEmpty).mkString("/")
      if (normalizedPrefix.startsWith(normalizedNodePrefix))
        return (true, normalizedPrefix.substring(normalizedNodePrefix.length))
      (false, prefix)
    }

    var prefix = reqPath
    var candidates: List[Node] = List(rootNode)
    var bestMatch: Node = null
    var matchedPath: String = ""
    while (candidates != null) {
      val matchedNode: Option[Node] = candidates.find((node: Node) => {
        val matchResult: (Boolean, String) = matches(node, prefix)
        matchResult._1
      })
      if (matchedNode.isEmpty)
        return (bestMatch, normalize(matchedPath), normalize(prefix))
      val matchResult: (Boolean, String) = matches(matchedNode.get, prefix)
      bestMatch = matchedNode.get
      val normalizedNodePrefix = matchedNode.get.urlPrefix.split("/").filter(!_.isEmpty).mkString("/")
      matchedPath = matchedPath + "/" + normalizedNodePrefix
      prefix = matchResult._2
      if (matchedNode.get.nodes != null)
        candidates = matchedNode.get.nodes.toList
      else
        candidates = null
    }
    (bestMatch, normalize(matchedPath), normalize(prefix))
  }

  class Loan[A <: AutoCloseable](resource: A) {
    def to[B](block: A => B) = {
      var t: Throwable = null
      try {
        block(resource)
      } catch {
        case x => t = x; throw x
      } finally {
        if (resource != null) {
          if (t != null) {
            try {
              resource.close()
            } catch {
              case y => t.addSuppressed(y)
            }
          } else {
            resource.close()
          }
        }
      }
    }
  }

  object Loan {
    def loan[A <: AutoCloseable](resource: A) = new Loan(resource)
  }

  import Loan._

  // todo : cache (return 304 NOT MODIFIED)
  def downloadStatic(response: HttpServletResponse, path: String) = {
    val basedir: String = System.getProperty("cms.basedir")
    val file = new File(basedir, path)
    if(file.exists() && !file.isDirectory) {
      loan(response.getOutputStream).to((ostream: ServletOutputStream) => {
        val helper: MimeTypesHelper = new MimeTypesHelper()
        response.setContentType(helper.getMimeTypeForExtension(helper.extractExtension(file.getName)))
        response.setContentLength(file.length().asInstanceOf[Int])
        loan(new FileInputStream(file)).to((istream: FileInputStream) => {
          val buffer = new Array[Byte](64 * 1024)
          var readed = istream.read(buffer)
          while ( readed != -1) {
            ostream.write(buffer, 0, readed)
            readed = istream.read(buffer)
          }
        })
      })
    } else {
      response.setStatus(404)
    }
  }

  override def service(req: HttpServletRequest, resp: HttpServletResponse):Unit = {
    val basedir: String = System.getProperty("cms.basedir")
    val file: BufferedSource = Source.fromFile(new File(basedir, "config_v2.json"))
    val content: String = file.mkString
    val gson: Gson = new Gson()
    val cmsConfig: CmsConfig = gson.fromJson(content, classOf[CmsConfig] )

    if (req.getServletPath.startsWith("/static/")){
      downloadStatic(resp, req.getServletPath)
      return
    }

    // Get best match node
    System.out.println("Path: " + req.getServletPath)
    val matchResult = bestMatch(cmsConfig.rootNode, req.getServletPath)
    val matchNode: Node = matchResult._1
    val matchedPath: String = matchResult._2
    val restPath : String = matchResult._3

    val method: String = req.getMethod

    if (method != "GET" && method != "POST") {
      resp.getWriter.print("Handler not found")
      return
    }

    val templateLoader = new FileTemplateLoader(new File(basedir, "views"))
    val cfg = new Configuration()
    cfg.setTemplateLoader(templateLoader)

    def getRootView(templateId: String,
                    regionsMap: mutable.HashMap[String, String],
                     attributesMap: java.util.HashMap[String, Object]) : String = {
      var currentTemplate = cmsConfig.getTemplateById(templateId)
      // Если шаблона с таким именем нет, считаем, что это и есть view
      if (currentTemplate == null) return templateId
      while (currentTemplate.baseTemplate != null){
        // Если есть какие-то регионы в текущем определении шаблона,
        // добавляем их в общий словарь
        if (currentTemplate.regions != null){
          currentTemplate.regions.foreach((tuple: (String, String)) => {
            regionsMap.put(tuple._1, getRootView(tuple._2, regionsMap, attributesMap))
          })
        }
        // То же самое с атрибутами
        if (currentTemplate.attributes != null){
          currentTemplate.attributes.foreach((tuple: (String, Object)) => {
            attributesMap.put(tuple._1, tuple._2)
          })
        }
        currentTemplate = cmsConfig.getTemplateById(currentTemplate.baseTemplate)
      }
      currentTemplate.view
    }

    val regions = new mutable.HashMap[String, String]()
    val attributes = new java.util.HashMap[String, Object]()
    val view = getRootView(matchNode.template, regions, attributes)

    if (matchNode.attributes == null) {
      matchNode.attributes = attributes
    }

    val ftlTemplate = cfg.getTemplate(view)
    val dataContext = new util.HashMap[String, Any]
    dataContext.put("region", new RegionDirective)
    dataContext.put("module", new ModuleDirective)
    // todo : make not const
    val baseUrl = "/webapp"
    dataContext.put("baseUrl", baseUrl)

    // fixme: make bestMatch instead of startsWith !
    val activeModuleInstance: ModuleInstance = matchNode.modules.find((instance: ModuleInstance) => restPath.startsWith(instance.instanceId))
      .lastOption.orNull

    if (null == activeModuleInstance && method == "POST") {
      resp.getWriter.print("Handler not found")
      return
    }

    // Рендерим модули
    val modulesContentMap = new mutable.HashMap[String, String]()
    if (null != activeModuleInstance) {
      val modulePath = normalize(restPath.substring(activeModuleInstance.instanceId.length))
      val moduleDefinition: ModuleDefinition = cmsConfig.moduleDefinitions.get(activeModuleInstance.definitionId)
      val module: IModule = Class.forName(moduleDefinition.className).getConstructor().newInstance().asInstanceOf[IModule]
      val moduleContent: String = module.service(
        new ModuleContext(
          activeModuleInstance,
          new CmsContext(cmsConfig, matchNode, cfg, baseUrl, matchedPath, req),
          moduleDefinition.attributes
        ),
        new ActiveModuleContext(modulePath, method, req.getParameterMap)
      )
      modulesContentMap.put(activeModuleInstance.instanceId, moduleContent)
    }
    // Для всех остальных модулей на узле мы получаем default content
    for (moduleInstance <- matchNode.modules if moduleInstance != activeModuleInstance) {
      val definition: ModuleDefinition = cmsConfig.moduleDefinitions.get(moduleInstance.getDefinitionId)
      val module: IModule = Class.forName(definition.className).getConstructor().newInstance().asInstanceOf[IModule]
      val moduleContext: ModuleContext = new ModuleContext(
        moduleInstance,
        new CmsContext(cmsConfig, matchNode, cfg, baseUrl, matchedPath, req),
        definition.attributes
      )
      val content: String = module.service(moduleContext, null)
      modulesContentMap.put(moduleInstance.instanceId, content)
    }

    dataContext.put("modulesContentMap", modulesContentMap)

    regions.foreach((tuple: (String, String)) => dataContext.put(
      "region_" + tuple._1, cfg.getTemplate(tuple._2)
    ))

    ftlTemplate.process(dataContext, resp.getWriter)
  }
}
