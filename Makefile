
v20 := 051920
v22 := 111822
v24 := v3.0.0-alpha.3
default_rounds := 100
default_runs := 1

.PHONY: clean clean20 clean22 build20 build22 build24 run20 run22 run24

clean: clean20 clean22minuspolicy

clean22minuspolicy:
	pushd policy-engine-111822/ && mvn clean && popd
	pushd analyzer-111822/ && mvn clean && popd

clean20:
	mvn -f pom-$(v20).xml clean
	rm -f policy/pom.xml

clean22:
	mvn -f pom-$(v22).xml clean
	rm -f policy/pom.xml

clean24:
	mvn -f pom-$(v24).xml clean
	rm -f policy/pom.xml

build20:
	sed -e "s/VERSION/$(v20)/g" policy/pom.xml.template > policy/pom.xml
	sed -e "s/VERSION/$(v20)/g" prolog-policy-engine/pom.xml.template > prolog-policy-engine/pom.xml
	sed -e "s/VERSION/$(v20)/g" policy-graph/pom.xml.template > policy-graph/pom.xml
	mvn -f pom-$(v20).xml clean install

build22:
	sed -e "s/VERSION/$(v22)/g" policy/pom.xml.template > policy/pom.xml
	sed -e "s/VERSION/$(v22)/g" prolog-policy-engine/pom.xml.template > prolog-policy-engine/pom.xml
	sed -e "s/VERSION/$(v22)/g" policy-graph/pom.xml.template > policy-graph/pom.xml
	mvn install:install-file -Dfile=lib/jpl.jar \
		-DgroupId=com.northeastern \
			-DartifactId=jpl \
			-Dversion=8.3.29 \
			-Dpackaging=jar \
			-DgeneratePom=true
	mvn -f pom-$(v22).xml clean install

build24:
	sed -e "s/VERSION/$(v24)/g" policy/pom.xml.template > policy/pom.xml
	sed -e "s/VERSION/$(v24)/g" prolog-policy-engine/pom.xml.template > prolog-policy-engine/pom.xml
	sed -e "s/VERSION/$(v24)/g" policy-graph/pom.xml.template > policy-graph/pom.xml
	mvn install:install-file -Dfile=lib/jpl.jar \
		-DgroupId=com.northeastern \
			-DartifactId=jpl \
			-Dversion=8.3.29 \
			-Dpackaging=jar \
			-DgeneratePom=true
	mvn -f pom-$(v24).xml clean install

run20: build20
	java -jar -Djava.library.path=lib analyzer-$(v20)/target/analyzer-$(v20)-0.1.jar \
	  prolog-policy-engine/src/main/resources/rules.pl \
		policy-graph/src/main/resources/seedPolicy_$(v20).pal \
		policy-graph/src/main/resources/seedPolicy.pl \
		$(default_rounds) $(default_runs)

run22: build22
	java -jar -Djava.library.path=lib analyzer-$(v22)/target/analyzer-$(v22)-0.1.jar \
	  prolog-policy-engine/src/main/resources/rules.pl \
		policy-graph/src/main/resources/seedPolicy_$(v22).pal \
		policy-graph/src/main/resources/seedPolicy.pl \
		$(default_rounds) $(default_runs)

run24: build24
	java -jar -Djava.library.path=lib analyzer-$(v24)/target/analyzer-$(v24)-0.1.jar \
    prolog-policy-engine/src/main/resources/rules.pl \
    policy-graph/src/main/resources/seedPolicy.pal \
    policy-graph/src/main/resources/seedPolicy.pl \
    $(default_rounds) $(default_runs)
