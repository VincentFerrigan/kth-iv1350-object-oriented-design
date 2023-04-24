# Please note that one has to add an ouput folder in order for this to work.
# Define variables
PLANTUML := java -jar plantuml.jar
PUML_FILES := $(wildcard *.puml)
PNG_FILES := $(patsubst %.puml, output/%.png, $(PUML_FILES))

# Default target
all: $(PNG_FILES)

# Rule to create PNGs from PlantUML files
output/%.png: %.puml
	mkdir -p output
	$(PLANTUML) -tpng -o output $<

# Clean target to remove generated files
clean:
	rm -rf output

.PHONY: all clean

