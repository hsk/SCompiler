package org.scompiler.states
import org.scompiler.Tokenizer

abstract class State{
  def nextState(actualChar: Char, tokenizer: Tokenizer): State
}
