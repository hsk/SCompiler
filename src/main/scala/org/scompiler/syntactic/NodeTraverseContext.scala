package org.scompiler.syntactic

import collection.mutable.{ListBuffer, ArrayBuffer, LinkedList}
import org.scompiler.lexer.{TokenType, Token, LexicalTokenizer}
import org.scompiler.exception.WrongPathException
import org.scompiler.lexer.TokenType._
import collection.mutable

class NodeTraverseContext(private val tokenizer: LexicalTokenizer) {
  case class Error(token: Token, description: String)

  private var tokenBuffer = new mutable.LinkedList[Token]
  private var currentTokenPosition = 0
  private var errorPosition = 0
  val accessedNodes = new mutable.ArrayBuffer[Node]

  var errors = new mutable.LinkedList[Error]
  var allowError = false
  var ignoreAllMode = false

  def consumeToken(): Option[Token] = consumeToken(movePosition = true)

  def consumeToken(movePosition: Boolean): Option[Token] = {
    while (currentTokenPosition >= tokenBuffer.size && tokenizer.hasNext) {
      val token = tokenizer.next()
      if (token != null && token.tokenType != TokenType.Commentary) {
        tokenBuffer = tokenBuffer append LinkedList(token)
      }
    }

    val actualToken = tokenBuffer.get(currentTokenPosition)

    if (movePosition) {
      currentTokenPosition += 1
    }

    return actualToken
  }

  def ignoreAllUntilEndToken(expectTokenValue: String) {
    try {
      Iterator.continually(consumeToken).takeWhile{
        t=> !(t.get.name.equals(expectTokenValue))
      }.toList
    } catch {
      case ex: Exception => {
//        throw new RuntimeException //TODO: Change to a different exception
      }
    }
    accessedNodes.clear()
  }

  def currentPosition: Int = currentTokenPosition

  def currentErrorPosition: Int = errorPosition

  def resetErrorToPosition(newPosition: Int) {
    // Shall not move position forward
    if(newPosition <= errorPosition) {
      errorPosition = newPosition
      errors = errors.take(newPosition)
    } else {
      throw new RuntimeException("Invalid Operation, cant move error forward")
    }
  }

  def resetToPosition(newPosition: Int) {
    // Shall not move position forward
    if(newPosition <= currentPosition) {
      currentTokenPosition = newPosition
    } else {
      throw new RuntimeException("Invalid Operation, cant force moving forward in stack")
    }
  }

  def registerError(token: Token, description: String) {
    errors = errors append LinkedList(Error(token, description))
    errorPosition += 1
  }

  def hasFinishedTokens = currentPosition.equals(tokenBuffer.size)
}
