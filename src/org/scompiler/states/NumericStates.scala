package org.scompiler.states
import org.scompiler.Tokenizer
import org.scompiler.TokenType

class NumericStateInit(initWithSign: Boolean = false) extends State {
  var hasPreviousPunctuation = false
  var expectingAnotherNumber = initWithSign

  def nextState(letter: Char, tokenizer: Tokenizer): State = letter match {
    case digit if ('0' to '9').contains(digit) =>
      {
        expectingAnotherNumber = false
        this
      };
    case '.' if !hasPreviousPunctuation => {
      hasPreviousPunctuation = true
      expectingAnotherNumber = true
      this
    }
    case 'E' if (!expectingAnotherNumber) => new NumericStateScientificNotation
    case ' ' | ';' | '\0' => {
      tokenizer.finishToken(TokenType.Number)
      new InitialState
    }
    case _ => new NotDefinedState;
  }

  def isFinalState: Boolean = !expectingAnotherNumber;
}

class NumericStateScientificNotation extends State {
  var hasPreviousNumber: Boolean = false;
  var hasSign: Boolean = false;

  def nextState(letter: Char, tokenizer: Tokenizer): State = letter match {
    case digit if ('0' to '9').contains(digit) => {
      hasPreviousNumber = true;
      return this;
    }

    case '-' if (!hasSign && !hasPreviousNumber) => {
      hasSign = true
      return this
    }

    case ' ' | ';' | '\0' => {
      tokenizer.finishToken(TokenType.Number)
      new InitialState
    }

    case _ => new NotDefinedState
  }

  def isFinalState: Boolean = hasPreviousNumber;
}