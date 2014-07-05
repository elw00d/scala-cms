package ru

import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}

/**
 * User: igor.kostromin
 * Date: 28.06.2014
 * Time: 10:55
 */
class MyServlet extends HttpServlet {
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    resp.getWriter.print("fsfdf")
    resp.flushBuffer()
    //super.doGet (req, resp)
  }
}
