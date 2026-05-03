GEN = generated
PARSER = $(GEN)/parser
LIB = lib
JTB = $(LIB)/jtb133di.jar
JAVACC = $(LIB)/javacc5.jar

all: jtb jcc

$(GEN):
		mkdir -p $@ 

$(PARSER):
		mkdir -p $@ 

jtb: | $(GEN)
		cd $(GEN) && java -jar ../$(JTB) -te ../minijava.jj 
		mv minijava-jtb.jj $(GEN)/

jcc: jtb | $(PARSER)
		java -jar $(JAVACC) -OUTPUT_DIRECTORY=$(PARSER) $(GEN)/minijava-jtb.jj
	
clean:
		rm -rf $(GEN)
