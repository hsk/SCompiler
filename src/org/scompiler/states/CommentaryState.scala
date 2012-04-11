package org.scompiler.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.TokenType

class CommentaryState extends State {
  def nextState(actualChar: Char, tokenizer: TokenBuffer) : State = actualChar match {
    case '}' => {
      tokenizer.registerCharacter(actualChar)
      tokenizer.finishToken(TokenType.Commentary)
      return new InitialState
    }
    case '\0' => new NotDefinedState
    case _ => this
  }
}
