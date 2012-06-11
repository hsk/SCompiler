package org.scompiler.syntactic.nodes

import collection.mutable.{ArrayBuffer}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.expression.AbstractExpression
import org.scompiler.syntactic.{NodeTraverseContext, Node, GrammarGraph}

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
      throw new WrongPathException(this)
    }
  }
}