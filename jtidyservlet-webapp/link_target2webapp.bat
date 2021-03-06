@rem This is used to create NTFS Links so webapp could be run directly from sources and classes compiled using Eclipse
@rem http://www.sysinternals.com/ntw2k/source/misc.shtml#junction
@rem Usage: junction [-d] <junction directory> [<junction target>]

@set servlet_classes=src\webapp\WEB-INF\classes\org\w3c\tidy\servlet

echo removing junctions

junction -d src\webapp\WEB-INF\lib
junction -d src\webapp\WEB-INF\classes
junction -d %servlet_classes%\sample
pause

junction src\webapp\WEB-INF\lib target\jtidyservlet-webapp\WEB-INF\lib

@rem junction src\webapp\WEB-INF\classes target\classes



@rem mkdir %classes%

@rem all jtidyservlet classes
junction src\webapp\WEB-INF\classes ..\jtidyservlet\target\classes

@rem jtidyservlet-webapp sample pkg classes
junction %servlet_classes%\sample target\classes\org\w3c\tidy\servlet\sample


if "%TOMCAT_HOME%" == "" goto no_tomcat_home

junction "%TOMCAT_HOME%\webapps\jtidyservlet-webapp" src\webapp
rem junction -D "%TOMCAT_HOME%\webapps\jtidyservlet-webapp"

:no_tomcat_home

pause