@echo off
cd /d "%~dp0"
if exist build rmdir /s /q build
mkdir build
for /r services %%f in (*.java) do echo %%f>>sources.txt
for /r tests %%f in (*.java) do echo %%f>>sources.txt
javac -d build @sources.txt
java -cp build tema2.Main
del sources.txt
