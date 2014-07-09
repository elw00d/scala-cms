package ru

import scala.collection.immutable.HashMap

/**
 * @author igor.kostromin
 *         09.07.2014 15:56
 */
class MimeTypesHelper {
  val map: Map[String, String] = Map[String, String](
    "css" -> "text/css",
    "js" -> "application/x-javascript",
    "jpg" -> "image/jpeg",
    "jpeg" -> "image/jpeg",
    "png" -> "image/png"
  )

  def extractExtension(fileName: String):String = {
    if (null == fileName) throw new IllegalArgumentException("fileName")
    val lastIndexOfDot = fileName.lastIndexOf(".")
    if (-1 == lastIndexOfDot) return ""
    if (lastIndexOfDot == fileName.length() - 1) return ""
    fileName.substring(lastIndexOfDot + 1)
  }

  def getMimeTypeForExtension(ext: String): String = {
    val value: Option[String] = map.get(ext)
    if (value.isEmpty)
      "application/octet-stream"
    else
      value.get
  }
}
