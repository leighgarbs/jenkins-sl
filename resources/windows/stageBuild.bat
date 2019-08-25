@echo on

REM Set up VisualStudio environment
call "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\Common7\Tools\VsDevCmd.bat"

REM We always want a clean build
git clean -x -d -f

cmake -DCMAKE_BUILD_TYPE=%BUILD_TYPE% .

cmake --build . --target "%TARGET%" > buildlog.%BUILD_TYPE%.txt
