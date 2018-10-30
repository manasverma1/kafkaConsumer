#!/bin/sh
export RUN_HOME=/home/manas/ProjectCheck
cd $RUN_HOME
export JAVA_HOME=/usr/java/latest
CLASSPATH=$RUN_HOME/properties:$RUN_HOME/jars/*
export TIMESTAMP=$( date +"%Y%m%d_%H%M%S")
nohup $JAVA_HOME/bin/java  -cp $CLASSPATH com.multiple.topics.ConsumerApp >> $RUN_HOME/logs/logsConsumer_$TIMESTAMP.log &
