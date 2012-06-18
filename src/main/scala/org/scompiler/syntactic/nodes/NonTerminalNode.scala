package org.scompiler.syntactic.nodes

import collection.mutable.{ArrayBuffer}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.expression.AbstractExpression
import org.scompiler.syntactic.{NodeTraverseContext, Node, GrammarGraph}
import org.scompiler.lexer.Token

class NonTerminalNode(val nodeName: Symbol, graph: GrammarGraph) extends Node {
  private var expr: Option[AbstractExpression] = None


  override def toString = expr.getOrElse(nodeName).toString

  def ~>(func: => AbstractExpression) {
    expr = Some(func)
    graph.setNonTerminal(nodeName, this)
  }

  @throws(classOf[WrongPathException])
  def traverseGraph(context: NodeTraverseContext) {
    if (expr.isDefined) {
      expr.get.asInstanceOf[Node].traverseGraph(context)
    } else {
      throw new RuntimeException(nodeName + " dont exists")
    }
  }

  def isValid(token: Token):Boolean = {
    return expr.get.asInstanceOf[Node].isValid(token)
  }
}