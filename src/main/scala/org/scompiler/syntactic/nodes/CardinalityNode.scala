package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.{NodeTraverseContext, Node}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.expression.{ExpressionCardinality, AbstractExpression}
import org.scompiler.syntactic.expression.ExpressionCardinality._

class CardinalityNode(node: Node) extends Node with AbstractExpression {

  @throws(classOf[WrongPathException])
  def traverseGraph(context: NodeTraverseContext) {
    cardinality match {
      case ONE_OR_MANY => {
         node.traverseGraph(context)
         while(tryTraverse()){}
      }
      case ZERO_OR_MANY => {
        while(tryTraverse()){}
      }
      case OPTIONAL => {
        tryTraverse()
      }
    }

    def tryTraverse(): Boolean = {
      val currentPosition = context.currentPosition
      try {
        node.traverseGraph(context)
        return true
      } catch {
        case ex: WrongPathException => {
          context.resetToPosition(currentPosition)
          return false
        }
      }
    }
  }
}
