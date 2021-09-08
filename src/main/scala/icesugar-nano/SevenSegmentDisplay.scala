package icesugar_nano

import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

case class SevenSegmentPMOD() extends Bundle {
  val pin1 = out(Bool)
  val pin2 = out(Bool)
  val pin3 = out(Bool)
  val pin4 = out(Bool)
  val pin7 = out(Bool)
  val pin8 = out(Bool)
  val pin9 = out(Bool)
  val pin10 = out(Bool)
}

case class SevenSegmentDisplayCtrl() extends Component {
  val io = new Bundle {
    val pins = SevenSegmentPMOD()
    val value = in(UInt(8 bits))
  }

  val fsm = new StateMachine {
    val showLowNibble = new State with EntryPoint
    val hideLowNibble = new State
    val showHighNibble = new State
    val hideHighNibble = new State

    val segments = Reg(UInt(7 bits)) init (0)

    io.pins.pin1 := ~segments(0)
    io.pins.pin2 := ~segments(1)
    io.pins.pin3 := ~segments(2)
    io.pins.pin4 := ~segments(3)
    io.pins.pin7 := ~segments(4)
    io.pins.pin8 := ~segments(5)
    io.pins.pin9 := ~segments(6)

    val lowNibble = True
    io.pins.pin10 := lowNibble

    showLowNibble.whenIsActive {
      segments := digitToSegments(io.value(3 downto 0))
      goto(hideLowNibble)
    }

    hideLowNibble.whenIsActive {
      segments := 0
      goto(showHighNibble)
    }

    showHighNibble.whenIsActive {
      segments := digitToSegments(io.value(7 downto 4))
      lowNibble := False
      goto(hideHighNibble)
    }

    hideHighNibble.whenIsActive {
      segments := 0
      lowNibble := False
      goto(showLowNibble)
    }

    def digitToSegments(digit: UInt) =
      digit.mux(
        U"x0" -> U"7'b0111111",
        U"x1" -> U"7'b0000110",
        U"x2" -> U"7'b1011011",
        U"x3" -> U"7'b1001111",
        U"x4" -> U"7'b1100110",
        U"x5" -> U"7'b1101101",
        U"x6" -> U"7'b1111101",
        U"x7" -> U"7'b0000111",
        U"x8" -> U"7'b1111111",
        U"x9" -> U"7'b1101111",
        U"xA" -> U"7'b1110111",
        U"xB" -> U"7'b1111100",
        U"xC" -> U"7'b0111001",
        U"xD" -> U"7'b1011110",
        U"xE" -> U"7'b1111001",
        U"xF" -> U"7'b1110001"
      )
  }
}

case class SevenSegmentDisplay() extends Component {
  val io = new Bundle {
    val pmod3 = SevenSegmentPMOD()
  }

  val displayArea = new SlowArea(400 Hz) {
    val display = new SevenSegmentDisplayCtrl
    display.io.pins <> io.pmod3
  }

  val ticker = new SlowArea(1 Hz) {
    displayArea.display.io.value := CounterFreeRun(256)
  }
}

object SevenSegmentDisplay {
  def main(args: Array[String]) {
    iCESugar.generate(new SevenSegmentDisplay)
  }
}
