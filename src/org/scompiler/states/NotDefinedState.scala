package org.scompiler.states
import org.scompiler.Tokenizer


class NotDefinedState extends State {
  def nextState(letter : Char, tokenizer: Tokenizer) : State = this
}