package org.scompiler.lexer.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.TokenType

class CommentaryState extends State {
  def nextState(actualChar: Char, tokenBuffer: TokenBuffer) : State = actualChar match {
    case '}' => {
      tokenBuffer.registerCharacter(actualChar)
      tokenBuffer.finishToken(TokenType.Commentary)
      return new InitialState
    }
    case '\0' => new NotDefinedState
    case _ => this
  }
}
