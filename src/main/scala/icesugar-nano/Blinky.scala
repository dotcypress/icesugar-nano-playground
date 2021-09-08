package icesugar_nano

import spinal.core._
import spinal.lib._
import icesugar_nano._

case class Blinky() extends Component {
  val io = new Bundle {
    val led = out(Bool)
  }

  new SlowArea(2 Hz) {
    io.led := CounterFreeRun(2).value === 0
  }
}

object Blinky {
  def main(args: Array[String]) {
    import java.nio.file._
    val targetDirectory = Paths.get("target/bitstream")
    if (!Files.exists(targetDirectory)) {
      Files.createDirectory(targetDirectory)
    }

    val config = new SpinalConfig(
      defaultClockDomainFrequency = FixedFrequency(8 MHz),
      targetDirectory = targetDirectory.toString()
    )
    config.generateVerilog(new Blinky)
  }
}
