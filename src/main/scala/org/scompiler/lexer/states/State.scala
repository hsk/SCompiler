package org.scompiler.lexer.states

import org.scompiler.util.TokenBuffer

abstract class State{
  def nextState(actualChar: Char, tokenBuffer: TokenBuffer): State
}
