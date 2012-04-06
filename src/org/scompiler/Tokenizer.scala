package org.scompiler
import scala.collection.mutable._
import org.scompiler.TokenType._
import org.scompiler.LexicalConstants._

class Tokenizer {
  private var tokens = new ArrayBuffer[Token]
  private var currentToken = ""
  
  def registerLetter(letter: Char) {
    assert(letter != ' ' && letter != ';')
    
    currentToken += letter
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