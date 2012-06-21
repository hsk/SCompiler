package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.exception.{UnexpectedEndOfFile, WrongPathException}
import org.scompiler.syntactic.{Node, NodeTraverseContext}
import org.scompiler.lexer.Token
import java.rmi.UnexpectedException

class AlternativeSeqNode extends Node with AbstractExpression {
  operation = ExpressionOperator.ALTERNATIVE
  var hasCorrectInitialToken = false

  @throws(classOf[WrongPathException])
  override def traverseGraph(context: NodeTraverseContext) {
    val oldAllowError = context.allowError
    var success = false
    hasCorrectInitialToken = false

    context.allowError = false
    success = tryEachNode(context, ignoreFirst = false)

    if (!success) {
      context.allowError = true
      success = tryEachNode(context, ignoreFirst = false)
      if (!success) {
        success = tryEachNode(context, ignoreFirst = true)
        if(!success && !context.ignoreAllMode) {
          context.ignoreAllMode = true
          if (context.consumeToken(false).isDefined) {
            context.registerError(context.consumeToken(false).get, "Ignoring all until end of statement")
          } else {
            context.registerError(null, "Unexpected end of file")
            throw new UnexpectedEndOfFile
          }

          success = tryEachNode(context, ignoreFirst = !hasCorrectInitialToken)
          context.ignoreAllMode = false
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
    val iterator = listOfNodes.iterator
    val allowError = context.allowError
    while (iterator.hasNext) {
      try {
        val currentNode = iterator.next()
        val currentToken = context.consumeToken(movePosition = false)
        if(currentNode._1.isValid(currentToken.get)) {
          hasCorrectInitialToken = true
        }
        if (currentToken.isDefined && (ignoreFirst || currentNode._1.isValid(currentToken.get))) {
          context.allowError = allowError

          if (context.ignoreAllMode) {
            if(context.accessedNodes contains currentNode._1) {
              throw new WrongPathException(this)
            } else {
              context.accessedNodes += currentNode._1
            }
          }
          currentNode._1.traverseGraph(context)
          return true
        }
      } catch {
        case wrongPath: WrongPathException => {
          context.resetToPosition(originalPosition)
          context.resetErrorToPosition(errorPosition)
        }
      }
    }
    false
  }

  override def isValid(token: Token, accessedNodes: Set[Node]): Boolean = {
    if(accessedNodes contains this) {
      return false
    }
    return listOfNodes.exists{ node => node._1.isValid(token, accessedNodes + this) }
  }
}
