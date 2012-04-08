package org.scompiler.util

import scala.collection.mutable._
import org.scompiler.lexer.{TokenType, Token}
import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.LexicalConstants._

class TokenBuffer {
  private var tokens = new ArrayBuffer[Token]
  private var currentToken = ""

  def registerCharacter(character: Char) {
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
          tokens += (new Token(TokenType.ReservedWord, currentToken))
          currentToken = ""
          return
        }
      }
      tokens += (new Token(tokenType, currentToken))
    }
    currentToken = ""
  }

  def hasFinishedToken: Boolean = !tokens.isEmpty

  def consumeToken(): Token = {
    if (!tokens.isEmpty) {
      val token = tokens.head
      tokens.remove(0);
      return token
    }

    return null
  }
}