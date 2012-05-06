package org.scompiler.lexer.states

import org.scompiler.util.TokenBuffer


class NotDefinedState extends State {
  def nextState(letter : Char, tokenBuffer: TokenBuffer) : State = this
}