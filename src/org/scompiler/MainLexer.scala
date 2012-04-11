package org.scompiler

import io.BufferedSource
import lexer.{Token, LexicalTokenizer}
import collection.Iterator

object MainLexer {
  def main(args: Array[String]) {
    val stream = new BufferedSource(System.in)
    val input: Iterator[Char] = stream.iter

    val lexicalTokenizer = new LexicalTokenizer(input)

    for(val token <- lexicalTokenizer if token != null) {
      Console.println(token)
    }
  }
}
