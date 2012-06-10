package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{Node, NodeTraverseContext, GrammarGraph}


/**
 * Sequence of nodes
 */
class SeqNode extends Node with AbstractExpression {
  operation = ExpressionOperator.SEQUENCE;

  @throws(classOf[WrongPathException])
  override def traverseGraph(context: NodeTraverseContext) {
    listOfNodes.foreach{
      node => node._1.traverseGraph(context)
    }
  }
}
