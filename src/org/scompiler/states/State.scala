package org.scompiler.states
import org.scompiler.Tokenizer

abstract class State{
  def nextState(letter: Char, tokenizer: Tokenizer): State;
  def isFinalState : Boolean;
}
