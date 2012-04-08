package org.scompiler

import io.BufferedSource
import lexer.{Token, LexicalTokenizer}

object MainLexer {
  def main(args: Array[String]) {
    val stream : BufferedSource = new BufferedSource(System.in)
    val input = stream.iter

    val lexicalTokenizer = new LexicalTokenizer(input)

    for(val token: Token <- lexicalTokenizer if token != null) {
      Console.println(token)
    }
  }
}
