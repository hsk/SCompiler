#!/bin/bash

apache_maven_link='http://apache.mirror.pop-sc.rnp.br/apache/maven/binaries/apache-maven-3.0.4-bin.tar.gz'

if [ ! -d maven ]; then
    rm apache-maven-3.0.4-bin.tar.gz
    if [ `uname` = "Darwin" ]; then
        curl -O $apache_maven_link
    else
        wget $apache_maven_link
    fi

    tar -xf apache-maven-3.0.4-bin.tar.gz
    mv apache-maven-3.0.4 maven
fi
        
