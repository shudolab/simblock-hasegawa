#!/bin/bash

# gradleのbuild
./gradlew build --quiet

PROPAGATION_FILE_NAME="test"

# gradleを使わずに実行
O=log qcmd java -classpath simulator/build/classes/java/main/:simulator/src/dist/conf/ simblock.simulator.Main $PROPAGATION_FILE_NAME