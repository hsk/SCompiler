### Sobre
O Compilador é escrito na linguagem Scala.

### Execução

Existem dois scripts bash. Um para a instalação (**install.sh**) e outro para rodar o programa (**run.sh**).


#### run.sh
O arquivo run.sh executa o .jar do programa, caso as dependências ainda não tenham sido instaladas, é executado automaticamente o arquivo **install.sh**

#### install.sh
Prepara as dependencias(*Scala* e *Maven*)

#### compile.sh
Recompila o codigo fonte. gerando o .jar no caminho target/SCompiler-**VERSION**.jar
