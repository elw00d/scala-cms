package ru

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

/**
 * @author igor.kostromin
 *         08.08.2014 11:04
 */
//@WebServlet(value=Array("/admin"), name = "adminServlet")
class AdminServlet extends HttpServlet {
  override def service(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    resp.getWriter.write("Admin")
  }
}
