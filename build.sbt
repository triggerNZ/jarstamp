name := "jarstamp"

version := "1.0"

scalaVersion := "2.12.3"

val newTimestamp = SettingKey[Long]("newTimestamp")
newTimestamp := 0

assembly := {
  val oldJar = assembly.value
  val preclean = new File(oldJar.getParentFile(), oldJar.getName() + ".preclean")
  IO.move(oldJar, preclean)
  println(s"Rewriting $oldJar")
  RewriteZip.rewrite(preclean, oldJar, newTimestamp.value)
  s"shasum $oldJar".!
  oldJar
}
