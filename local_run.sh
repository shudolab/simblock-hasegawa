#!/bin/bash

# 並列実行時gradleで実行するとエラーが出たのでgradleでbuild -> 実行ファイルを直接実行の順番でやる

# gradleでbuild
./gradlew build --quiet

PROPERTIES_FILE_NAME="light"

OPTION="-properties $PROPERTIES_FILE_NAME"

# gradleを使わずに実行
java -classpath simulator/build/classes/java/main/:simulator/src/dist/conf/ simblock.simulator.Main  $OPTION