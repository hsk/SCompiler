#!/bin/bash

scala_link='http://www.scala-lang.org/downloads/distrib/files/scala-2.9.1-1.tgz'

if [ ! -f scala-2.9.1-1.tgz ]; then
	if [ `uname` = "Darwin" ]; then
		curl -O $scala_link
	else
		wget $scala_link
	fi
fi

if [ ! -d scala-2.9.1-1 ]; then
	tar -xf scala-2.9.1-1.tgz
fi