package ru

import javax.servlet.ServletContextEvent
import javax.servlet.annotation.WebListener

import com.google.gson.Gson

import scala.io.{BufferedSource, Source}

/**
 * User: igor.kostromin
 * Date: 05.07.2014
 * Time: 11:30
 */
@WebListener
class WebAppInitializer extends javax.servlet.ServletContextListener {
  override def contextInitialized(sce: ServletContextEvent): Unit = {
    System.out.println("initialized2")
  }

  override def contextDestroyed(sce: ServletContextEvent): Unit = {
    System.out.println("destroyed")
  }
}
