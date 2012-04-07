package org.scompiler
import scala.collection.mutable._
import org.scompiler.TokenType._
import org.scompiler.LexicalConstants._

class Tokenizer {
  private var tokens = new ArrayBuffer[Token]
  private var currentToken = ""
  
  def registerLetter(character: Char) {
    assert(!(endTokens contains character))

    currentToken += character
  }

  def registerToken(tokenType: TokenType, wordToken: String) {
    currentToken = wordToken
    finishToken(tokenType)
  }
  
  def finishToken(tokenType: TokenType = TokenType.Identifier) {
    if(!currentToken.isEmpty) {
      if(tokenType == TokenType.Identifier) {
        if (reservedWords contains currentToken) {
          tokens += (new Token(TokenType.ReservedWord, currentToken))
          return
        }
      }
      tokens += (new Token(tokenType, currentToken))
    }
    currentToken = ""
  }
  
  def iterator = tokens.iterator
}