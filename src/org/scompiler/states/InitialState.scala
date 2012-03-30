package org.scompiler.states
import org.scompiler.Tokenizer

class InitialState extends State {
  private val letters = ('a' to 'z') union ('A' to 'Z')
  private val alfaNumericRange = letters union ('0' to '9')
  private val reservedSymbols = Array("+", "-", "*", "/", "=", ":",
    ".", ",", ";", "'", ":=", "<",
    ">", ">=", "<=", "<>", "(", ")",
    "[", "]", "{", "}", "..", "^");

  def nextState(letter: Char, tokenizer: Tokenizer): State = letter match {
    case '\'' => new NotDefinedState // Not implemented

    case digit if ('0' to '9').contains(digit) => new NumericStateInit(false);

    case '-' => new NumericStateInit(true);

    case symbol if (!alfaNumericRange.contains(symbol)) => new NotDefinedState // Not implemented

    case _ => new NotDefinedState;
  }

  def isFinalState: Boolean = false;
}