
resources := analyzer/src/main/resources
.PHONY: clean

all: build json

clean:
	mvn clean

build:
	mvn clean package

json:
	java -jar analyzer/target/analyzer-0.1.jar $(resources)/policy3N2H.json

pal:
	java -jar target/policy-machine-0.1-jar-with-dependencies.jar $(resources)/simple.pal
