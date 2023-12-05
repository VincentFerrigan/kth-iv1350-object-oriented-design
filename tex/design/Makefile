# Makefile       : Makefile
# Module         : S2 
# Description    : Seminar 2, Design
#                 
#                  This Makefile is for LaTeX only
# Course         : Object-Oriented Design, IV1350}
# Author/Student : Vincent Ferrigan
# maintainer     : ferrigan@kth.se

LATEX=lualatex
SOURCES=$(wildcard *.tex)
OUTPUT_DIR=output/tex
OBJECTS=$(addprefix $(OUTPUT_DIR)/,$(SOURCES:.tex=.pdf))

.PHONY: all clean clean_aux

all: $(OBJECTS)

$(OUTPUT_DIR)/%.pdf: %.tex
	mkdir -p $(OUTPUT_DIR)
	$(LATEX) -output-directory=$(OUTPUT_DIR) --shell-escape $<

clean:
	rm -rf $(OUTPUT_DIR)

clean_aux:
	rm -f $(OUTPUT_DIR)/*.aux $(OUTPUT_DIR)/*.log $(OUTPUT_DIR)/*.out $(OUTPUT_DIR)/*.toc $(OUTPUT_DIR)/*.synctex.gz

#LATEX=lualatex
#INPUT=sem2-iv1350-ferrigan.tex
#OUTPUT=output/tex
#
#.PHONY: all clean clean_aux
#
#all: $(OUTPUT)/sem2-iv1350-ferrigan.pdf
#
#$(OUTPUT)/sem2-iv1350-ferrigan.pdf: $(INPUT)
#	mkdir -p $(OUTPUT)
#	$(LATEX) -output-directory=$(OUTPUT) --shell-escape $(INPUT)
#
#clean:
#	rm -rf $(OUTPUT)
#
#clean_aux:
#	rm -f $(OUTPUT)/*.aux $(OUTPUT)/*.log $(OUTPUT)/*.out $(OUTPUT)/*.toc $(OUTPUT)/*.synctex.gz
