@echo off

cd ..

call mvn clean install -Dmaven.test.skip=true

cd bin