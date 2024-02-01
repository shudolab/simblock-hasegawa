#!/bin/bash


if [ "$#" -eq 0 ]; then
    PROPERTIES_FILE_NAME="base"
else
    PROPERTIES_FILE_NAME="$1"
fi


OPTION="-properties $PROPERTIES_FILE_NAME"

./gradlew simulator:run --args="$OPTION"