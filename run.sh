#!/bin/bash

apache_maven_link='http://linorg.usp.br/apache/maven/binaries/apache-maven-3.0.4-bin.tar.gz'
scompiler_jar_dir='target/SCompiler-0.1-ALPHA.jar'

if [ ! -d scala-2.9.1-1 ]; then
	sh ./install.sh
fi

if [ ! -f $scompiler_jar_dir ]; then
	if [ ! -d maven ]; then
		if [ `uname` = "Darwin" ]; then
			curl -O $apache_maven_link
		else
			wget $apache_maven_link
		fi
    	tar -xf apache-maven-3.0.4-bin.tar.gz
    	mv apache-maven-3.0.4 maven
	fi
  	maven/bin/mvn install
fi

export PATH=$PATH:$PWD/scala-2.9.1-1/bin
scala $scompiler_jar_dir