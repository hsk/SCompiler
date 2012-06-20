package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.{NodeTraverseContext, Node}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.expression.{ExpressionCardinality, AbstractExpression}
import org.scompiler.syntactic.expression.ExpressionCardinality._
import org.scompiler.lexer.Token

class CardinalityNode(node: Node) extends Node with AbstractExpression {

  def isOptional: Boolean = cardinality != ONE_OR_MANY

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
        val currentToken = context.consumeToken(movePosition = false)
        if (currentToken.isDefined && node.isValid(currentToken.get)) {
          node.traverseGraph(context)
          return true
        } else {
          return false
        }
      } catch {
        case ex: WrongPathException => {
          context.resetToPosition(currentPosition)
          return false
        }
      }
    }
  }

  def isValid(token: Token): Boolean = {
    return node.isValid(token)
  }
}
