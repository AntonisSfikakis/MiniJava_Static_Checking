GEN = generated
PARSER = $(GEN)/parser
LIB = lib
SRC = src
JTB = $(LIB)/jtb133di.jar
JAVACC = $(LIB)/javacc5.jar
BUILD = build

all: compile clean

$(GEN):
		mkdir -p $@ 

$(PARSER):
		mkdir -p $@ 

$(BUILD):
		mkdir -p $@ 


jtb: | $(GEN)
		cd $(GEN) && java -jar ../$(JTB) -te ../minijava.jj 
		mv minijava-jtb.jj $(GEN)/

jcc: jtb | $(PARSER)
		java -jar $(JAVACC) -OUTPUT_DIRECTORY=$(PARSER) $(GEN)/minijava-jtb.jj
		sed -i '1s/^/package parser;\n/' $(PARSER)/*.java

compile: jcc | $(BUILD)
		javac -sourcepath src:generated -d $(BUILD) $(SRC)/Main.java
	
clean:
		rm -rf $(GEN) $(BUILD)
