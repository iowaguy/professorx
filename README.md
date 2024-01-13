# Policy Engine Fuzzing via Differential Analysis

A method to validate that an implementation of a Next Generation Access Control (NGAC) [_policy engine_](https://github.com/PM-Master/policy-machine-core/wiki) built by National Institute of Standards and Technologies (NIST) is operating correctly. 
The goal of this method is to compare the access decisions of the NIST reference implementation to the access decisions of a policy engine we wrote using logic programming, Prolog.

## Getting Started
For Apple Silicon Mac systems, set up Docker from your workstation by opening a terminal and performing the following:
```bash
docker build --platform linux/amd64 -t makenew .
```
To execute the code, perform the following:
```bash
docker run --platform linux/amd64 makenew
```