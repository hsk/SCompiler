package org.scompiler.syntactic

import org.scompiler.lexer.Token
import org.scompiler.lexer.TokenType._
import collection.mutable.{ArrayBuffer}

class NonTerminalNode(val nodeName: Symbol, graph: GrammarGraph) extends Node {
  private var expr: Option[GrammarExpression] = None

  def parseToken(token: Token) {}

  override def toString = expr.get.toString

  def ~>(func: => GrammarExpression) {
    expr = Some(func)
    graph.setNonTerminal(nodeName, this)
  }
}