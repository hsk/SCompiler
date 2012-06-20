package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{Node, NodeTraverseContext}
import org.scompiler.lexer.Token

class AlternativeSeqNode extends Node with AbstractExpression {
  operation = ExpressionOperator.ALTERNATIVE

  @throws(classOf[WrongPathException])
  override def traverseGraph(context: NodeTraverseContext) {
    val oldAllowError = context.allowError
    var success = false

    context.allowError = false
    success = tryEachNode(context, ignoreFirst = false)

    if (!success) {
      context.allowError = true
      success = tryEachNode(context, ignoreFirst = false)
      if (!success) {
        success = tryEachNode(context, ignoreFirst = true)
        if(!success && !context.ignoreAllMode) {
          context.ignoreAllMode = true
          context.registerError(context.consumeToken(false).get, "Ignoring all until end of statement")
          context.ignoreAllUntilEndToken()

          success = tryEachNode(context, ignoreFirst = true)
        }
      }
    }

    context.allowError = oldAllowError

    if(!success) {
      throw new WrongPathException(this)
    }
  }

  def tryEachNode(context: NodeTraverseContext, ignoreFirst: Boolean): Boolean = {
    val originalPosition = context.currentPosition
    val errorPosition = context.currentErrorPosition
    val iterator = if(context.ignoreAllMode) listOfNodes.reverseIterator else listOfNodes.iterator
    val allowError = context.allowError
    while (iterator.hasNext) {
      try {
        val currentNode = iterator.next()
        val currentToken = context.consumeToken(movePosition = false)
        if (currentToken.isDefined && (ignoreFirst || currentNode._1.isValid(currentToken.get))) {
          context.allowError = allowError
          var shouldExecute = true
          if (context.ignoreAllMode) {
            if(context.accessedNodes contains currentNode._1) {
              shouldExecute = false
            } else {
              context.accessedNodes += currentNode._1
            }
          }
          if (shouldExecute) {
            currentNode._1.traverseGraph(context)
            if (!context.ignoreAllMode) {
              return true
            }
          }
        }
      } catch {
        case wrongPath: WrongPathException => {
          if(!context.ignoreAllMode) {
            context.resetToPosition(originalPosition)
            context.resetErrorToPosition(errorPosition)
          }
        }
      }
    }
    false
  }

  def isValid(token: Token): Boolean = {
    return listOfNodes.exists{ node => node._1.isValid(token) }
  }
}
