package org.scompiler.lexer.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class InvalidTokenState extends State {
  val falseInitState = new InitialState
  def nextState(actualChar: Char, tokenBuffer: TokenBuffer) : State = actualChar match {
    case endLine if endTokens contains endLine =>  {
      tokenBuffer.finishToken(TokenType.Undefined)
      new InitialState
    }
    case ';' => {
      tokenBuffer.finishToken(TokenType.Undefined)

      tokenBuffer.registerCompleteToken(TokenType.SemiColon, ";")

      return new InitialState
    }
    case _ =>  {
      val nextState = falseInitState.nextState(actualChar, tokenBuffer)
      if (nextState.isInstanceOf[InvalidTokenState]) {
        return this
      } else {
        tokenBuffer.finishToken(TokenType.Undefined)
        return nextState
      }
    }
  }
}
