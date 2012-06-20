package org.scompiler

import exception.WrongPathException
import io.BufferedSource
import lexer.{Token, LexicalTokenizer}
import collection.Iterator
import syntactic.{PascalGrammarGraph, NodeTraverseContext}

object Main {
  def main(args: Array[String]) {
    val stream = new BufferedSource(System.in)
    val input: Iterator[Char] = stream.iter

    val lexicalTokenizer = new LexicalTokenizer(input)

    /*
    for(val token <- lexicalTokenizer if token != null) {
      Console.println(token)
    }
    */

    val context = new NodeTraverseContext(lexicalTokenizer)
    val pascalGraph = new PascalGrammarGraph


    pascalGraph.traverse('program, context)
    if (context.errors.isEmpty) {
      println("program whitout a error")
    } else {
      println(context.errors.mkString("\n"))
    }
  }
}
