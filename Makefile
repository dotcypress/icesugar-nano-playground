MODULE = SevenSegmentDisplay
BUILD_DIR = target/bitstream
CONSTRAINTS = src/main/resources/constraints.pcf

all: elaborate bitstream

elaborate:
	sbt --supershell=never "runMain icesugar_nano.$(MODULE)"

bitstream:
	cd $(BUILD_DIR) && \
	yosys -q -p 'synth_ice40 -top $(MODULE) -json $(MODULE).json' $(MODULE).v && \
	nextpnr-ice40 --package cm36 --lp1k --json $(MODULE).json --pcf ../../$(CONSTRAINTS) --asc $(MODULE).asc && \
	icetime -d lp1k -mtr $(MODULE).rpt $(MODULE).asc && \
	icepack $(MODULE).asc $(MODULE).bin

prog:
	icesprog $(BUILD_DIR)/$(MODULE).bin
	icesprog -c 1

clean:
	sbt clean --supershell=never
	rm -rf $(BUILD_DIR)

.SECONDARY:
.PHONY: elaborate bitstream prog clean
