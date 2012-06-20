package org.scompiler.syntactic.nodes

import org.scompiler.syntactic.expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{Node, NodeTraverseContext, GrammarGraph}
import org.scompiler.lexer.{TokenType, Token}

/**
 * Sequence of nodes
 */
class SeqNode extends Node with AbstractExpression {
  operation = ExpressionOperator.SEQUENCE

  @throws(classOf[WrongPathException])
  override def traverseGraph(context: NodeTraverseContext) {
    var currentPosition = 0
    var nodePosition = 0

    while(nodePosition < listOfNodes.size) {
      val currentNode = listOfNodes.get(nodePosition).get._1
      try {
        currentPosition = context.currentPosition
        currentNode.traverseGraph(context)
        nodePosition += 1
      } catch {
        case ex:WrongPathException => {

          var fixed = false
          if (context.allowError && !context.ignoreAllMode) {
            //Errors treatments
            context.resetToPosition(currentPosition)

            val nextSyntactic = listOfNodes.get(nodePosition+1)
            val currentLexicalToken = context.consumeToken(movePosition = false)

            if(currentLexicalToken.isDefined) {

              // Check if next node in graph is valid for the current lexical token
              if (nextSyntactic.isDefined && nextSyntactic.get._1.isValid(currentLexicalToken.get)) {
                // Register error, and continue as if the missing token existed
                fixed = true
                nodePosition += 1
                context.registerError(currentLexicalToken.get, "missing token")
              } else {
                context.consumeToken() //Ignore One token
                val nextLexicalToken = context.consumeToken(false)
                if(nextLexicalToken.isDefined && nextSyntactic.isDefined) {
                  // if next lexical token is valid for next graph node, than should do a substitution
                  if(nextSyntactic.get._1.isValid(nextLexicalToken.get)) {
                    // Register error, and continue as if the current token was the one expected
                    fixed = true
                    nodePosition += 1
                    context.registerError(currentLexicalToken.get, "wrong token")
                  }
                }
              }
            }
          }

          if(context.ignoreAllMode) {
            nodePosition += 1
          } else if (!fixed) {
            throw new WrongPathException(this )
          }
        }
      }
    }
  }

  override def isValid(token: Token, accessedNodes: Set[Node]): Boolean = {
    if(accessedNodes contains this) {
      return false
    }
    val head: Node = listOfNodes.head._1
    if (listOfNodes.size > 0 && head.isInstanceOf[CardinalityNode] && head.asInstanceOf[CardinalityNode].isOptional) {
      if (accessedNodes.contains(head) && head.isValid(token, accessedNodes + this)) {
        return true
      }

      return listOfNodes.get(1).get._1.isValid(token, accessedNodes + this)
    }
    return head.isValid(token, accessedNodes + this)
  }
}
