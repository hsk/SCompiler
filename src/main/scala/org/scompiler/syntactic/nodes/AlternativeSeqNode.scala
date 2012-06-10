package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{Node, NodeTraverseContext, GrammarGraph}

class AlternativeSeqNode extends Node with AbstractExpression {
  operation = ExpressionOperator.ALTERNATIVE

  @throws(classOf[WrongPathException])
  override def traverseGraph(context: NodeTraverseContext) {
    val originalPosition = context.currentPosition
    val iterator = listOfNodes.iterator
    while(iterator.hasNext) {
      context.resetToPosition(originalPosition)
      try {
        iterator.next()._1.traverseGraph(context)
        return
      } catch {
        case wrongPath: WrongPathException => {}
      }
    }
    throw new WrongPathException(this)
  }
}
