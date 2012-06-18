package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{Node, NodeTraverseContext, GrammarGraph}
import org.scompiler.lexer.Token

class AlternativeSeqNode extends Node with AbstractExpression {
  operation = ExpressionOperator.ALTERNATIVE

  @throws(classOf[WrongPathException])
  override def traverseGraph(context: NodeTraverseContext) {
    val oldAllowError = context.allowError
    var success = false

    context.allowError = false
    success = tryEachNode(context, false)

    if (!success) {
      context.allowError = true
      success = tryEachNode(context, false)
      if (!success) {
        success = tryEachNode(context, true)
      }
    }

    context.allowError = oldAllowError

    if(!success) {
      throw new WrongPathException(this)
    }
  }

  def tryEachNode(context: NodeTraverseContext, ignoreFirst: Boolean): Boolean = {
    val originalPosition = context.currentPosition
    val iterator = listOfNodes.iterator
    val allowError = context.allowError
    while (iterator.hasNext) {
      try {
        val currentNode = iterator.next()
        val currentToken = context.consumeToken(false)
        if (currentToken.isDefined && (ignoreFirst || currentNode._1.isValid(currentToken.get))) {
          context.allowError = allowError
          currentNode._1.traverseGraph(context)
          return true
        }
      } catch {
        case wrongPath: WrongPathException => {
          context.resetToPosition(originalPosition)
        }
      }
    }
    false
  }

  def isValid(token: Token): Boolean = {
    return listOfNodes.exists{ node => node._1.isValid(token) }
  }
}
