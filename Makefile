
newversion := 111822
oldversion := 051920

.PHONY: clean cleanold cleannew buildold buildnew old new

clean: cleanold cleannewminuspolicy

cleannewminuspolicy:
	pushd policy-engine-111822/ && mvn clean && popd
	pushd analyzer-111822/ && mvn clean && popd

cleanold:
	mvn -f pom-$(oldversion).xml clean
	rm -f policy/pom.xml

cleannew:
	mvn -f pom-$(newversion).xml clean
	rm -f policy/pom.xml

buildold:
	sed -e "s/VERSION/$(oldversion)/g" policy/pom.xml.template > policy/pom.xml
	mvn -f pom-$(oldversion).xml clean install

buildnew:
	sed -e "s/VERSION/$(newversion)/g" policy/pom.xml.template > policy/pom.xml
	mvn -f pom-$(newversion).xml clean install

old: buildold
	java -jar analyzer-$(oldversion)/target/analyzer-$(oldversion)-0.1.jar analyzer-$(oldversion)/src/main/resources/policy3N2H.json

new: buildnew
	java -jar analyzer-$(newversion)/target/analyzer-$(newversion)-0.1.jar analyzer-$(newversion)/src/main/resources/simple.pal
