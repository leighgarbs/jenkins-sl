@echo off

REM Set up VisualStudio environment
call "%VSDEVCMD%"

REM We always want a clean build
git clean -x -d -f

cmake -DCMAKE_BUILD_TYPE=%BUILD_TYPE% .

cmake --build . --target "%TARGET%" > build.%BUILD_TYPE%.out
