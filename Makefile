
newversion := 111822
oldversion := 051920
newestversion := v3.0.0-alpha.3

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
	sed -e "s/VERSION/$(oldversion)/g" prolog-policy-engine/pom.xml.template > prolog-policy-engine/pom.xml
	mvn -f pom-$(oldversion).xml clean install

buildnew:
	sed -e "s/VERSION/$(newversion)/g" policy/pom.xml.template > policy/pom.xml
	sed -e "s/VERSION/$(newversion)/g" prolog-policy-engine/pom.xml.template > prolog-policy-engine/pom.xml
	mvn install:install-file -Dfile=lib/jpl.jar \
		-DgroupId=com.northeastern \
   		-DartifactId=jpl \
   		-Dversion=8.3.29 \
   		-Dpackaging=jar \
   		-DgeneratePom=true
	mvn -f pom-$(newversion).xml clean install

buildnewest:
	sed -e "s/VERSION/$(newestversion)/g" policy/pom.xml.template > policy/pom.xml
	sed -e "s/VERSION/$(newestversion)/g" prolog-policy-engine/pom.xml.template > prolog-policy-engine/pom.xml
	mvn install:install-file -Dfile=lib/jpl.jar \
		-DgroupId=com.northeastern \
   		-DartifactId=jpl \
   		-Dversion=8.3.29 \
   		-Dpackaging=jar \
   		-DgeneratePom=true
	mvn -f pom-$(newestversion).xml clean install

old: buildold
	java -jar analyzer-$(oldversion)/target/analyzer-$(oldversion)-0.1.jar analyzer-$(oldversion)/src/main/resources/policy3N2H.json

new: buildnew
	java -jar -Djava.library.path=lib analyzer-$(newversion)/target/analyzer-$(newversion)-0.1.jar analyzer-$(newversion)/src/main/resources/policy3N2H.pal prolog-policy-engine/src/main/resources/rules.pl analyzer-$(newversion)/src/main/resources/policy1.pl

test: buildnew
	java -jar -Djava.library.path=lib analyzer-$(newversion)/target/analyzer-$(newversion)-0.1.jar policy-graph/src/main/resources/translatePolicy.pal prolog-policy-engine/src/main/resources/rules.pl policy-graph/src/main/resources/translatePolicy.pl

newest: buildnewest
	java -jar -Djava.library.path=lib analyzer-$(newestversion)/target/analyzer-$(newestversion)-0.1.jar prolog-policy-engine/src/main/resources/rules.pl analyzer-$(newestversion)/src/main/resources/seedPolicy.pal analyzer-$(newestversion)/src/main/resources/seedPolicy.pl 10

