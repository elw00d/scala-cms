package ru

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping

/**
 * User: igor.kostromin
 * Date: 28.06.2014
 * Time: 12:37
 */
@Controller
class IndexController {
  trait MyTrait {
    def firstMethod() =
      System.out.println("firstMethod")
    def secondMethod() =
    System.out.println("secondMethod")
  }

  trait MySecondTrait {
    def thirdMethod() =
      System.out.println("thirdMethod")
  }

  class MyClass extends MyTrait with MySecondTrait {
  }

  @RequestMapping(value = Array("/"))
  def viewIndex(modelMap: ModelMap) = {
    val x = new MyClass()
    x.firstMethod()
    x.secondMethod()
    x.thirdMethod()
    modelMap.addAttribute("text", "Igorrrrr")
    "index"
  }
}
