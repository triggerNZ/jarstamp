package com.pavlinic

import sbt._
import sbtassembly._, AssemblyKeys._

object Jarstamp extends AutoPlugin {
  import JarstampKeys._

  override def requires = AssemblyPlugin

  override def projectSettings = Seq(
    newTimestamp := 0,
    assembly := {
      val oldJar = assembly.value
      val preclean = new File(oldJar.getParentFile(), oldJar.getName() + ".preclean")
      IO.move(oldJar, preclean)
      println(s"Rewriting $oldJar")

      val (duration, _) = time(
        RewriteZip.rewrite(preclean, oldJar, newTimestamp.value)
      )
      println(s"Took $duration ms")

      s"shasum $oldJar".!
      oldJar
    }
  )

  def time[T](block: => T): (Long, T) = {
    val before = System.currentTimeMillis
    val result = block
    val timing = System.currentTimeMillis - before
    (timing, result)
  }
}

object JarstampKeys {
  val newTimestamp = SettingKey[Long]("newTimestamp")
}
