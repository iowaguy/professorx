<h1 align="center">ProfessorX</h1>

A system to verify that the software implementing Next Generation Access Control (NGAC) policy engine properly conforms to the [_NGAC_](https://standards.incits.org/higherlogic/ws/public/projects/2328/details) standard.

## Prerequisites
- **Java**: 17+
- **Maven**: 3.4.x+
- **Docker**: 4.35.0+
## Quick Start
Run ProfessorX to discover access decision discrepancies between the National Institute of Standards and Technologies (NIST) [_policy engine_](https://github.com/PM-Master/policy-machine-core/wiki) (release 2024) and a Prolog-based policy engine.  
Build a Docker image:
```bash
docker build -t make24 ./
```
Start a container:
```bash
docker run -it make24
```
### For Apple Silicon Mac Systems
Due to limitations in JPL (Java-Prolog bindings) on ARM64 architectures, set up Docker from your Apple Silicon workstation using the following command. Please add `--platform linux/amd64` for all Docker commands hereafter.
```bash
docker build --platform linux/amd64 -t make24 ./
```
Start a container:
```bash
docker run --platform linux/amd64 -it make24
```
In the above default mode, ProfessorX performs one run, in which a simple seed policy will be mutated for 100 rounds until a discrepancy was found or 100 rounds is reached.  
Discrepancies will be printed on the terminal. All access decisions, inconsistent access decisions, and runtimes records are stored in decision.csv, discrepancies.csv, and timer.csv, respectively.

[//]: # (The default setting is maxRound=100, runNumber=1, and no seedNumber or performanceEvaluation arguments are provided. Change the setting in the Makefile accordingly.)

## Custom Docker image build
### On release 2024
By modifying the target `run24` in Makefile, set appropriate arguments for custom configuration. Build a new Docker image and container with the same docker commands as above.   
Basic Syntax:
```bash
run24: build24
  java -jar -Djava.library.path=lib analyzer-$(v24)/target/analyzer-$(v24)-0.1.jar \
    prolog-policy-engine/src/main/resources/rules.pl \
    policy-graph/src/main/resources/seedPolicy.pal \
    policy-graph/src/main/resources/seedPolicy.pl \
    <maxRound> <runNumber> \
    [<seedNumber>] [<performanceEvaluation>]
```
Options:  
maxRound: The number of maximum rounds to mutate starting from a seed policy in a run.  
runNumber: The number of runs.  
seedNumber: Random seed for reproducibility. Default is `System.nanoTime()`. Set to any integer for deterministic results.  
performanceEvaluation: If provided (any value), forces ProfessorX to execute all maxRound rounds regardless of if it found a discrepancy or not.

### On release 2022
To run Professor X on the NIST policy engine released on 2022, instead of modifying target `run24` in Makefile, modify `run22` with the same syntax.  
Build a new Docker image:
```bash
docker build -t make22 --build-arg VERSION="run22" ./
```
Start a container:
```bash
docker run -it make22
```

[//]: # (## Build your own policy)