JFLAGS =
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Registry.java \
	Registry_Client.java \
	Registry_Server.java \
	LocateRegistry.java \
	registryThread.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
