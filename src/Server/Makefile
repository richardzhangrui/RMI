JFLAGS =
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	CommunicationModule.java \
	RemoteObjectRef.java \
	RemoteServerRef.java \
	Remote.java \
	RemoteThread.java \
	RemoteService.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
