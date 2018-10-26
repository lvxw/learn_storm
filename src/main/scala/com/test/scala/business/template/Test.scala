package com.test.scala.business.template

import scala.collection.mutable

object Test extends App {

  val wordCountMap = new mutable.HashMap[String, String]

  wordCountMap+=("a"->"")
  wordCountMap+=("b"->"")

  println(wordCountMap)

}
