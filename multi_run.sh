#!/bin/bash

# 並列実行時gradleで実行するとエラーが出たのでgradleでbuild -> 実行ファイルを直接実行の順番でやる

# gradleでbuild
./gradlew build --quiet

PROPERTIES_FILE_NAMES=("base" "light" "initial")

for properties in "${PROPERTIES_FILE_NAMES[@]}"; do

    OPTION="-properties $properties"

    # gradleを使わずに実行
    O=log qcmd java -classpath simulator/build/classes/java/main/:simulator/src/dist/conf/ simblock.simulator.Main $OPTION
done
