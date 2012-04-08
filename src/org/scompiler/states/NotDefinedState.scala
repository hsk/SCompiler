package org.scompiler.states

import org.scompiler.util.TokenBuffer


class NotDefinedState extends State {
  def nextState(letter : Char, tokenizer: TokenBuffer) : State = this
}