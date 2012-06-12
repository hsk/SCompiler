package org.scompiler.syntactic

import collection.mutable.{ListBuffer, ArrayBuffer, LinkedList}
import org.scompiler.lexer.{TokenType, Token, LexicalTokenizer}

class NodeTraverseContext(private val tokenizer: LexicalTokenizer) {
  case class Error(token: Token, line: Int, column: Int)

  private val errors = new LinkedList[Error]
  private var tokenBuffer = new LinkedList[Token]
  private var currentTokenPosition: Int = 0

  def currentToken: Option[Token] = {
    while (currentTokenPosition >= tokenBuffer.size && tokenizer.hasNext) {
      val token = tokenizer.next()
      if (token != null && token.tokenType != TokenType.Commentary) {
        tokenBuffer = tokenBuffer append LinkedList(token)
      }
    }

    val actualToken = tokenBuffer.get(currentTokenPosition)

    currentTokenPosition += 1

    return actualToken
  }

  def currentPosition: Int = currentTokenPosition

  def resetToPosition(newPosition: Int) {
    // Shall not move position forward
    if(newPosition < currentPosition) {
      currentTokenPosition = newPosition
    }
  }

  def registerError() {
    val token = tokenBuffer.get(currentPosition).get

    errors append LinkedList(Error(token, 0, 0))
  }

  def hasFinishedTokens = currentPosition.equals(tokenBuffer.size)
}
