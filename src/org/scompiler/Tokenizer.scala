package org.scompiler
import scala.collection.mutable._
import org.scompiler.TokenType._

class Tokenizer {
  private var tokens = new ArrayBuffer[Token]
  private var currentToken = ""
  
  def registerLetter(letter: Char) {
    assert(letter != ' ' && letter != ';')
    
    currentToken += letter
  } 
  
  def finishToken(tokenType: TokenType = TokenType.Identifier) {
    if(!currentToken.isEmpty()) {
      tokens + new Token(tokenType, currentToken)
    }
    currentToken = ""
  }
  
  def iterator = tokens.iterator
}