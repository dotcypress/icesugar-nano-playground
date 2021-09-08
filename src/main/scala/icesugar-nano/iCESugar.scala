package icesugar_nano

import java.nio.file._
import spinal.core._
import spinal.lib._

object iCESugar {
  def generate[T <: Component](gen: => T) {
    val targetDirectory = Paths.get("target/bitstream")
    if (!Files.exists(targetDirectory)) {
      Files.createDirectory(targetDirectory)
    }
    new SpinalConfig(
      defaultClockDomainFrequency = FixedFrequency(8 MHz),
      targetDirectory = targetDirectory.toString()
    ).generateVerilog(gen)
  }
}