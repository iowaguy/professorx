
resources := src/main/resources
.PHONY: clean verifynormal

all: build json

clean:
	mvn clean

build:
	mvn clean install

json:
	java -jar target/policy-machine-0.1-jar-with-dependencies.jar $(resources)/policy3N2H.json

pal:
	java -jar target/policy-machine-0.1-jar-with-dependencies.jar $(resources)/simple.pal
