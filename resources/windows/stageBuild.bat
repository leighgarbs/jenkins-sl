@echo off

REM Set up VisualStudio environment
call "%VSDEVCMD%"

REM We always want a clean build
git clean -x -d -f

REM cmake -DCMAKE_BUILD_TYPE=Debug .

cmake --build . --target "%TARGET%" --config Debug
