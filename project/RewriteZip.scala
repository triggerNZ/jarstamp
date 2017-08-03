import java.io.{File, FileOutputStream, InputStream}
import java.util.zip.{ZipEntry, ZipFile, ZipOutputStream}

import scala.collection.JavaConversions._

object RewriteZip {
  def rewrite(in: File, out: File, newTimestamp: Long) = {
    val zin  = new ZipFile(in)
    val zout = new ZipOutputStream(new FileOutputStream(out))

    zin.entries().toIterator.foreach { e =>
      val bytesIn = readAll(zin.getInputStream(e))
      val (ze, ab) = transform(newTimestamp)(e, bytesIn)
      zout.putNextEntry(ze)
      zout.write(ab, 0, ab.length)
    }

    zout.close
    zin.close
  }

  def readAll(is: InputStream): Array[Byte] = {
    val res = Stream.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
    is.close()
    res
  }

  def transform(newTimestamp: Long)(ze: ZipEntry, ab: Array[Byte]): (ZipEntry, Array[Byte]) = {
    ze.setTime(newTimestamp)
    (ze, ab)
  }
}
