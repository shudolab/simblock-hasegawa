#!/bin/bash

# gradleでbuild
./gradlew build --quiet

OUTPUT_FILE_NAME="output"
PROPAGATION_FILE_NAME="test"

OPTION="-output $OUTPUT_FILE_NAME -propagation $PROPAGATION_FILE_NAME"

# gradleを使わずに実行
O=log qcmd java -classpath simulator/build/classes/java/main/:simulator/src/dist/conf/ simblock.simulator.Main  $OPTION