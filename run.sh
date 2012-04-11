#!/bin/bash

scompiler_jar_dir='target/SCompiler-0.1-ALPHA.jar'

if [ ! -f $scompiler_jar_dir ]; then	
    sh ./compile.sh
fi

java -jar $scompiler_jar_dir
