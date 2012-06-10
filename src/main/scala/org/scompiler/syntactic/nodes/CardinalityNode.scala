package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.{NodeTraverseContext, Node}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.expression.{ExpressionCardinality, AbstractExpression}
import org.scompiler.syntactic.expression.ExpressionCardinality._

class CardinalityNode(node: Node) extends Node with AbstractExpression {
  lock()

  @throws(classOf[WrongPathException])
  def traverseGraph(context: NodeTraverseContext) {
    cardinality match {
      case ONE_OR_MANY => {

      }
      case ZERO_OR_MANY => {

      }
      case OPTIONAL => {
        val currentPosition = context.currentPosition
        try {
          node.traverseGraph(context)
        } catch {
          case ex: Exception => {
            context.resetToPosition(currentPosition)
          }
        }
      }
    }
  }
}
