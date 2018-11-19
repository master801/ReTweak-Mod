#!/bin/sh

export NAME=ReTweak-Mod

rm $NAME.iml $NAME.ipr $NAME.iws
rm -rf build .gradle out

./gradlew clean cleanIdea setupDecompWorkspace idea
