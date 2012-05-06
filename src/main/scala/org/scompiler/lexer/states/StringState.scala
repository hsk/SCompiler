package org.scompiler.lexer.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class StringState extends State {
  def nextState(actualChar: Char, tokenBuffer: TokenBuffer) : State = actualChar match {
    case '\'' => new StringEscapeState
    case endLine if endLineTokens contains endLine => new NotDefinedState
    case _ => this
  }
}

class StringEscapeState extends State {
  def nextState(actualChar: Char, tokenBuffer: TokenBuffer) : State = actualChar match {
    case '\'' => new StringState
    case _ => {
      tokenBuffer.finishToken(TokenType.String)

      val initialState = new InitialState
      return initialState.nextState(actualChar, tokenBuffer)
    }
  }
}