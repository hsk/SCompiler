#!/bin/bash

if [ ! -f scala-2.9.1-1.tgz ]; then
	wget http://www.scala-lang.org/downloads/distrib/files/scala-2.9.1-1.tgz
fi

if [ ! -d scala-2.9.1-1 ]; then
	tar -xf scala-2.9.1-1.tgz
fi
