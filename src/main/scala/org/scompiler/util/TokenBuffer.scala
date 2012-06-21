package org.scompiler.util

import scala.collection.mutable._
import org.scompiler.lexer.{TokenType, Token}
import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.LexicalConstants._

class TokenBuffer {

  private var tokens = new ArrayBuffer[Token]
  private var currentToken = ""
  private var currentPosition: (Int, Int) = (1,0)
  private var tokenPosition: (Int, Int) = currentPosition

  def setCurrentPosition(line: Int, position: Int) {
    currentPosition = (line, position)
  }

  def registerCharacter(character: Char) {
    if (currentToken.isEmpty) {
      tokenPosition = currentPosition
    }
    currentToken += character
  }

  def registerCompleteToken(tokenType: TokenType, wordToken: String) {
    currentToken = wordToken
    finishToken(tokenType)
  }

  def finishToken(tokenType: TokenType = TokenType.Identifier) {
    if (!currentToken.isEmpty) {
      if (tokenType == TokenType.Identifier) {
        if (reservedWords contains currentToken.toUpperCase) {
          tokens += (new Token(TokenType.ReservedWord, currentToken, tokenPosition))
          currentToken = ""
          return
        }
      }
      tokens += (new Token(tokenType, currentToken, tokenPosition))
    }
    currentToken = ""
  }

  def hasFinishedToken: Boolean = !tokens.isEmpty

  def consumeToken(): Token = {
    if (!tokens.isEmpty) {
      val token = tokens.head
      tokens.remove(0)
      return token
    }

    return null
  }
}