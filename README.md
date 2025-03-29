<h1 align="center">ProfessorX</h1>

A system to verify that the software implementing Next Generation Access Control (NGAC) policy engine properly conforms to the [_NGAC_](https://standards.incits.org/higherlogic/ws/public/projects/2328/details) standard.

## Prerequisites
- **Java**: 17+
- **Maven**: 3.4.x+
## Quick Start
1. Build the project:
   ```bash
    mvn clean package
   ```
2. Running ProfessorX on the National Institute of Standards and Technologies (NIST) [_policy engine_](https://github.com/PM-Master/policy-machine-core/wiki) (release 2024).
    ```bash
   java -jar -Djava.library.path=lib analyzer-v3.0.0-alpha.3/target/analyzer-v3.0.0-alpha.3-0.1.jar \
   prolog-policy-engine/src/main/resources/rules.pl \
   policy-graph/src/main/resources/translatePolicy.pal \
   policy-graph/src/main/resources/translatePolicy.pl \
   100 \
   1
   ```
3. Discrepancies will be printed on the terminal. All access decisions, inconsistent access decisions, and runtimes records are stored in decision.csv, discrepancies.csv, and timer.csv, respectively.
## Custom by modifying command arguments
### On release 2024
Basic Syntax:
```bash
java -jar -Djava.library.path=lib analyzer-v3.0.0-alpha.3/target/analyzer-v3.0.0-alpha.3-0.1.jar \
prolog-policy-engine/src/main/resources/rules.pl \
policy-graph/src/main/resources/translatePolicy.pal \
policy-graph/src/main/resources/translatePolicy.pl \
<maxRound> <runNumber> \
[<seedNumber>] [<performanceEvaluation>]
```
options:  
maxRound: The number of maximum rounds to mutate starting from a seed policy in a run.  
runNumber: The number of runs.  
seedNumber: Random seed for reproducibility. Default is `System.nanoTime()`. Set to any integer for deterministic results.  
performanceEvaluation: If provided (any value), forces ProfessorX to execute all maxRound rounds regardless of if it found a discrepancy or not.

### On release 2022
To run Professor X on release 2022, change the path to the executable JAR file from `analyzer-v3.0.0-alpha.3/target/analyzer-v3.0.0-alpha.3-0.1.jar` to `analyzer-111822/target/analyzer-111822-0.1.jar` in the above commands.

### For Apple Silicon Mac Systems
Due to limitations in JPL (Java-Prolog bindings) on ARM64 architectures, set up Docker from your Apple Silicon workstation using the following command.
```bash
docker build --platform linux/amd64 -t makenewest .
```
To execute the code, perform the following:
```bash
docker run --platform linux/amd64 makenewest
```
The default setting is maxRound=100, runNumber=1, and no seedNumber or performanceEvaluation arguments are provided. Change the setting in the Makefile accordingly.

[//]: # (## Build your own policy)