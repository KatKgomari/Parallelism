# binary search program makefile
# Hussein Suleman
# 27 March 2017

.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
JAVAC=/usr/bin/javac
JAVA=/usr/bin/java

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES2= MeanFilterSerial.class MeanFilterParallel.class MedianFilterSerial.class MedianFilterParallel.class

CLASSES=$(CLASSES2:%.class=$(BINDIR)/%.class)

default: $(CLASSES)

run: $(CLASSES)
	$(JAVA) -cp $(BINDIR)

clean:
	rm $(BINDIR)/*.class