## Sobre
Compilador Pascal escrito na linguagem Scala.


## Execução

Existem tres scripts bash. 
Um para a instalação (**install.sh**), outro para compilação(**compile.sh**) e outro para rodar o programa (**run.sh**).



#### run.sh
O arquivo run.sh executa o .jar do programa. Caso as dependências ainda não tenham sido instaladas, é executado automaticamente o arquivo **install.sh** e/ou **compile.sh**

A entrada é do teclado, mas pode-se utilizar: ./run.sh < arquivo.pas


#### install.sh
Prepara as dependencias(*Maven*)


#### compile.sh
Recompila o codigo fonte. gerando o .jar no caminho target/SCompiler-**VERSAO**.jar.
