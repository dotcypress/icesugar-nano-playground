name := "icesugar-nano-uart-pcm-player"
version := "0.0.1"
scalaVersion := "2.11.12"

val spinalVersion = "1.4.0"

libraryDependencies ++= Seq(
  "com.github.spinalhdl" % "spinalhdl-core_2.11" % spinalVersion,
  "com.github.spinalhdl" % "spinalhdl-lib_2.11" % spinalVersion,
  compilerPlugin(
    "com.github.spinalhdl" % "spinalhdl-idsl-plugin_2.11" % spinalVersion
  )
)
