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
    iCESugar.generate(new Blinky)
  }
}
