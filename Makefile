all: BeerCellar Beer UI Control

BeerCellar:
	javac BeerCellar.java

Beer:
	javac Beer.java

UI:
	javac UI.java

Control:
	javac Control.java

JAR: all
	jar cfe BeerCellar.jar BeerCellar *class

javadoc:
	javadoc -d javadoc *java

clean:
	rm -f *class *~ BeerCellar.jar beers.txt 
	rm -rf javadoc
