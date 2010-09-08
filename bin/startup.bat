@echo off
set TMP_CLASSPATH=%CLASSPATH%

for %%i in (%0) do cd /d %%~dpi\..


set CLASSPATH=%CLASSPATH%;.\classes\
rem Add all jars....
for %%i in (".\lib\*.jar") do call ".\bin\cpappend.bat" %%i
for %%i in (".\lib\*.zip") do call ".\bin\cpappend.bat" %%i

set AT_CLASSPATH=%CLASSPATH%
set CLASSPATH=%TMP_CLASSPATH%

java -cp "%AT_CLASSPATH%" -Xms24m -Xmx512m org.archiviststoolkit.Main %*

