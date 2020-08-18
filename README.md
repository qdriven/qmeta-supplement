# README

[![codecov](https://codecov.io/gh/qdriven/qmeta-supplement/branch/master/graph/badge.svg)](https://codecov.io/gh/qdriven/qmeta-supplement)


Supplement Tools for JAVA. It is a collection
of different JAVA Helper based on Hutool

## Features:

1. random utils
2. Assert Utils
3. Bean Utils
4. Collection Utils
5. ExcelUtils based on Alibaba Excel
6. StringUtils
7. Simple SQL Utils
8. Thread Utils

## Setup For Github travis and code coverage

- Github Action
- Code Coverage- JACOCO Report

## Jacoco Report

Our report shows 21% instructions coverage, 17% branches coverage, 3/5 for cyclomatic complexity and so on.

The 38 instructions shown by JaCoCo in the report refers to the bytecode instructions as opposed to ordinary Java code instructions.

JaCoCo reports help you visually analyze code coverage by using diamonds with colors for branches and background colors for lines:

Red diamond means that no branches have been exercised during the test phase.
Yellow diamond shows that the code is partially covered – some branches have not been exercised.
Green diamond means that all branches have been exercised during the test.
The same color code applies to the background color, but for lines coverage.

JaCoCo mainly provides three important metrics:

Lines coverage reflects the amount of code that has been exercised based on the number of Java byte code instructions called by the tests.
Branches coverage shows the percent of exercised branches in the code – typically related to if/else and switch statements.
Cyclomatic complexity reflects the complexity of code by giving the number of paths needed to cover all the possible paths in a code through linear combination.
To take a trivial example, if there is no if or switch statements in the code, the cyclomatic complexity will be 1, as we only need one execution path to cover the entire code.

Generally the cyclomatic complexity reflects the number of test cases we need to implement in order to cover the entire code.

- [jacoco](https://www.eclemma.org/jacoco/trunk/doc/report-mojo.html)
- []