#!/bin/bash

# 並列実行時gradleで実行するとエラーが出たのでgradleでbuild -> 実行ファイルを直接実行の順番でやる

# gradleでbuild
./gradlew build --quiet

PROPERTIES_FILE_NAMES=("gossip0.1" "gossip0.2" "gossip0.3" "gossip0.4" "gossip0.5" "gossip0.6" "gossip0.7" "gossip0.8" "gossip0.9")

for properties in "${PROPERTIES_FILE_NAMES[@]}"; do
    OUTPUT_FILE_NAME=${properties}
    PROPAGATION_FILE_NAME=${properties}
    PROPERTIES_FILE_NAME=${properties}

    OPTION="-output $OUTPUT_FILE_NAME -propagation $PROPAGATION_FILE_NAME -properties $PROPERTIES_FILE_NAME"

    # gradleを使わずに実行
    M=16 W=24 O=log qcmd java -classpath simulator/build/classes/java/main/:simulator/src/dist/conf/ simblock.simulator.Main $OPTION
done
