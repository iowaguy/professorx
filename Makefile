
.PHONY: clean

clean:
	mvn clean

buildold:
	mvn -f pom-051920.xml clean package

buildnew:
	mvn -f pom-111822.xml clean package

old: buildold
	java -jar analyzer-051920/target/analyzer-051920-0.1.jar analyzer-051920/src/main/resources/policy3N2H.json

new: buildnew
	java -jar analyzer-111822/target/analyzer-111822-0.1.jar analyzer-111822/src/main/resources/simple.pal
