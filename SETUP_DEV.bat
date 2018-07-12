@echo off

set name=ReTweak-Mod
del /Q %name%.iml %name%.ipr %name%.iws
rmdir /Q /S build .gradle

gradlew clean cleanIdea setupDecompWorkspace idea
pause
