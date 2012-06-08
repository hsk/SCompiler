package org.scompiler.syntactic

import expression.AbstractExpression
import org.scompiler.lexer.Token
import org.scompiler.lexer.TokenType._
import collection.mutable.{ArrayBuffer}
import org.scompiler.exception.WrongPathException

class NonTerminalNode(val nodeName: Symbol, graph: GrammarGraph) extends Node {
  private var expr: Option[AbstractExpression] = None

  def parseToken(token: Token) {
    if(expr.isDefined) {
      expr.get.parseToken(token)
    } else {
      throw new WrongPathException(this)
    }
  }

  override def toString = expr.get.toString

  def ~>(func: => AbstractExpression) {
    expr = Some(func)
    graph.setNonTerminal(nodeName, this)
  }
}