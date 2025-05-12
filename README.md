[![DOI](https://zenodo.org/badge/957212812.svg)](https://doi.org/10.5281/zenodo.15388891)

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
docker run --name professorx -it make24
```
### For Apple Silicon Mac Systems
Due to limitations in JPL (Java-Prolog bindings) on ARM64 architectures, set up Docker from your Apple Silicon workstation using the following command. Please add `--platform linux/amd64` to all Docker commands hereafter.
```bash
docker build --platform linux/amd64 -t make24 ./
```
Start a container:
```bash
docker run --platform linux/amd64 --name professorx -it make24
```
In the above default mode, ProfessorX performs one run, in which a simple seed policy is mutated for 100 rounds until a discrepancy is found or 100 rounds are reached.
### Output File Descriptions
Discrepancies will be printed on the terminal. All access decisions, inconsistent access decisions, and runtime records are stored in `decisions.csv`, `discrepancies.csv`, `timer.csv`, respectively.  
To copy the generated CSV files to your current local directory, run:
```bash
docker cp professorx:/app/decisions.csv ./
docker cp professorx:/app/discrepancies.csv ./
docker cp professorx:/app/timer.csv ./
```

#### 1. `decisions.csv`
Records all access request decisions.  
**Columns**:
- `cur_round_time`: Timestamp of the mutation event.
- `mutation`: Current policy mutation (e.g., `Add-Node_u3_(u3, ua1)` = adds policy element `u3` assigned to `ua1`).
- `cur_access_time`: Timestamp of the access request (generated per round: `#UA × #OA + #U × #OA ` requests).
- `nist_decision`: Access decision from NIST policy engine (`Grant`/`Deny`).
- `prolog_decision`: Access decision from Prolog policy engine (`Grant`/`Deny`).
---

#### 2. `discrepancies.csv`
Records inconsistent access requests (where Prolog and NIST access decisions differ).  
**Columns**:
- `cur_access_time`: Timestamp (matches `decisions.csv`).
- `subject`: Requesting entity (e.g., `u3`).
- `subject_degree`: Degree of the subject node (in/out edges).
- `subject_degree_count`: Number of nodes with the same degree as `subject`.
- `subject_type_count`: Total nodes of the same category (i.e., all `UA` + `U` nodes).
- `object`: Accessed entity (e.g., `oa1`).
- `object_degree`: Degree of the object attribute node.
- `object_degree_count`: Number of nodes with the same degree as `object`.
- `object_type_count`: Total nodes of the same category (i.e., all `OA` nodes).
- `permissions`: Requested access right (e.g., `read`).

---

#### 3. `timer.csv`
Tracks mutation and decision processing times.  
**Columns**:
- `run_no`: Run sequence number (e.g., `1` if single run)
- `round_no`: Mutation round sequence number within a run.
- `cur_round_time`: Timestamp (matches `decisions.csv`).
- `duration`: Total time for mutation + access decisions (nanoseconds).

---

#### Key Features
- **Consistent Timestamps**: `cur_round_time` and `cur_access_time` align across files for cross-referencing.
- **Metrics**: Node degrees/counts help analyze policy graph complexity.
- **Performance**: `timer.csv` provides granular runtime profiling.

## Custom Docker image build
### On release 2024
Modify the target `run24` in Makefile to set appropriate arguments for custom configuration. Then, build a new Docker image and container with the same Docker commands as above.   
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
**Options:**  
- maxRound: The number of maximum rounds to mutate starting from a seed policy in a run.  
- runNumber: The number of runs.  
- seedNumber: Random seed for reproducibility. The default is `System.nanoTime()`. Set to any integer for deterministic results.  
- performanceEvaluation: If provided (any value), forces ProfessorX to execute all maxRound rounds regardless of whether it found a discrepancy or not.

### On release 2022
To run Professor X on the NIST policy engine released in 2022, instead of modifying the target `run24` in Makefile, modify `run22` with the same syntax.  
Build a new Docker image:
```bash
docker build -t make22 --build-arg VERSION="run22" ./
```
Start a container:
```bash
docker run --name professorx -it make22
```

[//]: # (## Build your own policy)
