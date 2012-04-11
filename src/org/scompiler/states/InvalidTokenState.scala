package org.scompiler.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class InvalidTokenState extends State {
  val falseInitState = new InitialState
  def nextState(actualChar: Char, tokenizer: TokenBuffer) : State = actualChar match {
    case endLine if endTokens contains endLine =>  {
      tokenizer.finishToken(TokenType.Undefined)
      new InitialState
    }
    case ';' => {
      tokenizer.finishToken(TokenType.Undefined)

      tokenizer.registerCompleteToken(TokenType.SemiColon, ";")

      return new InitialState
    }
    case _ =>  {
      val nextState = falseInitState.nextState(actualChar, tokenizer)
      if (nextState.isInstanceOf[InvalidTokenState]) {
        return this
      } else {
        tokenizer.finishToken(TokenType.Undefined)
        return nextState
      }
    }
  }
}
