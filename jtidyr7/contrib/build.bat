@echo off

rem %1 is the root where JTidy.zip was expanded, eg. 'c:'
rem %2 is the directory under JTidy\src where the source is located,
rem eg. '26jul1999'
rem
rem assumes the following environment variable:
rem   'java_home' the JDK install directory
rem assumes JDK tools are in your path

set javac=javac -O
rem set javac=jikes -O
set jtidy_lib=%1\JTidy\lib
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;%jtidy_lib%
rem set CLASSPATH=%CLASSPATH%;%JAVA_HOME%\lib\classes.zip

echo Cleaning...
cd %jtidy_lib%
del *.jar
del org\w3c\tidy\*.properties
del org\w3c\tidy\*.class
del org\w3c\dom\*.class
del org\w3c\tidy\*.gif
cd %1\JTidy\doc
del org\w3c\dom\*.html
del org\w3c\tidy\*.html

echo Compiling tidy source...
cd %1\JTidy\src\%2\org\w3c\dom
%javac% -d %jtidy_lib% *.java
cd %1\JTidy\src\%2\org\w3c\tidy
%javac% -d %jtidy_lib% *.java
copy TidyMessages.properties %jtidy_lib%\org\w3c\tidy
copy %1\JTidy\doc\images\tidy.gif %jtidy_lib%\org\w3c\tidy

echo Creating documentation...
cd %1\JTidy\src\%2
javadoc -d %1\JTidy\doc org.w3c.dom org.w3c.tidy

echo Creating Tidy.jar...
cd %jtidy_lib%
jar cfm Tidy.jar manifest.txt org\w3c\tidy\*.* org\w3c\dom\*.*

echo Done.
