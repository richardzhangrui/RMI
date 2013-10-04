JFLAGS =
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	RemoteException.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
