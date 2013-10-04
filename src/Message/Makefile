JFLAGS =
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	RMIMessage.java \
	ExMessage.java \
	RVMessage.java \
	RorMessage.java \
	MethodMessage.java \
	MethodInfo.java \
	RefMessage.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
