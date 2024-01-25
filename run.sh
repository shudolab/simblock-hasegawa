#!/bin/bash

# gradleのbuild
./gradlew build --quiet

# gradleを使わずに実行
O=log qcmd java -classpath simulator/build/classes/java/main/:simulator/src/dist/conf/ simblock.simulator.Main