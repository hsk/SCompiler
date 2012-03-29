package org.scompiler.states
import org.scompiler.Tokenizer

class InitialState extends State {
  def nextState(letter: Char, tokenizer: Tokenizer): State = letter match {
    case digit if ('0' to '9').contains(digit) => new NumericStateInit(false);
    case '-' => new NumericStateInit(true);
    case _ => new NotDefinedState;
  }

  def isFinalState : Boolean = false;
}