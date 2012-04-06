package org.scompiler.states

import org.scompiler.LexicalConstants._
import org.scompiler.{TokenType, Tokenizer}

class IdentifierStateInit extends State {
  def nextState(actualChar: Char, tokenizer: Tokenizer) = actualChar match {
    case letter if alphaNumeric contains letter => this

    case ' ' | ';' | '\0' => {
      tokenizer.finishToken(TokenType.Identifier)
      new InitialState
    }

    case _ => new NotDefinedState
  }
}
