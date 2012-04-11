if [ ! -d maven ]; then
    sh ./install.sh
fi

maven/bin/mvn install
