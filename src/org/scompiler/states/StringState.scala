package org.scompiler.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class StringState extends State {
  def nextState(actualChar: Char, tokenizer: TokenBuffer) : State = actualChar match {
    case '\'' => new StringEscapeState
    case endLine if endLineTokens contains endLine => new NotDefinedState
    case _ => this
  }
}

class StringEscapeState extends State {
  def nextState(actualChar: Char, tokenizer: TokenBuffer) : State = actualChar match {
    case '\'' => new StringState
    case _ => {
      tokenizer.finishToken(TokenType.String)

      val initialState = new InitialState
      return initialState.nextState(actualChar, tokenizer)
    }
  }
}