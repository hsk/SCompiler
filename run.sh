#!/bin/bash

if [ ! -d scala-2.9.1-1 ]; then
	sh ./install.sh
fi

if [ ! -f target/SCompiler-0.1-ALPHA.jar ]; then
  if [ ! -d maven ]; then
    wget http://linorg.usp.br/apache/maven/binaries/apache-maven-3.0.4-bin.tar.gz
    tar -xf apache-maven-3.0.4-bin.tar.gz
    mv apache-maven-3.0.4 maven
  fi
  maven/bin/mvn install
fi

export PATH=$PATH:$PWD/scala-2.9.1-1/bin
scala target/SCompiler-0.1-ALPHA.jar
