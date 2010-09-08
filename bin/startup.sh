#! /bin/sh


# add the libraries to the IREPORT_CLASSPATH.
# EXEDIR is the directory where this executable is.
EXEDIR=${0%/*}
DIRLIBS=${EXEDIR}/../lib/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$AT_CLASSPATH" ] ; then
    AT_CLASSPATH=$i
  else
    AT_CLASSPATH="$i":$AT_CLASSPATH
  fi
done

DIRLIBS=${EXEDIR}/../lib/*.zip
for i in ${DIRLIBS}
do
  if [ -z "$AT_CLASSPATH" ] ; then
    AT_CLASSPATH=$i
  else
    AT_CLASSPATH="$i":$AT_CLASSPATH
  fi
done

AT_CLASSPATH="${EXEDIR}/../classes":$AT_CLASSPATH
AT_HOME="${EXEDIR}/.."

java -classpath "$AT_CLASSPATH:$CLASSPATH" -Direport.home=$AT_HOME org.archiviststoolkit.Main "$@"
