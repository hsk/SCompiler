package org.scompiler.states

import org.scompiler.util.TokenBuffer

abstract class State{
  def nextState(actualChar: Char, tokenizer: TokenBuffer): State
}
