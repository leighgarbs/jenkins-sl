@echo off

REM Set up VisualStudio environment
%VSDEVCMD%

REM We always want a clean build
git clean -x -d -f

REM The single carat capitalizes the first letter of the BUILD_TYPE string
cmake -DCMAKE_BUILD_TYPE=Debug .
