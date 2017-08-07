package com.pavlinic

import scala.collection.JavaConversions._

import org.pantsbuild.jarjar.util._

import java.io.File

object RewriteZip {
  def rewrite(in: File, out: File, newTimestamp: Long) = {
    StandaloneJarProcessor.run(in, out, new JarProcessor {
      override def process(struct: EntryStruct): Boolean = {
        struct.time = newTimestamp
        true
      }
    })
  }
}
