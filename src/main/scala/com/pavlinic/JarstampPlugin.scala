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
      RewriteZip.rewrite(preclean, oldJar, newTimestamp.value)
      s"shasum $oldJar".!
      oldJar
    }
  )
}

object JarstampKeys {
  val newTimestamp = SettingKey[Long]("newTimestamp")
}
