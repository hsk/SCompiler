package org.scompiler.syntactic

import collection.mutable.{ListBuffer, ArrayBuffer, LinkedList}
import org.scompiler.lexer.{TokenType, Token, LexicalTokenizer}

class NodeTraverseContext(private val tokenizer: LexicalTokenizer) {
  case class Error(token: Token, description: String)

  private var tokenBuffer = new LinkedList[Token]
  private var currentTokenPosition: Int = 0
  var allowError = false
  var errors = new LinkedList[Error]

  def consumeToken(): Option[Token] = consumeToken(true)

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

  def currentPosition: Int = currentTokenPosition

  def resetToPosition(newPosition: Int) {
    // Shall not move position forward
    if(newPosition < currentPosition) {
      currentTokenPosition = newPosition
    }
  }

  def registerError(token: Token, description: String) {
    errors = errors append LinkedList(Error(token, description))
  }

  def hasFinishedTokens = currentPosition.equals(tokenBuffer.size)
}
