package org.scompiler.states
import org.scompiler.Tokenizer
import org.scompiler.TokenType
import org.scompiler.LexicalConstants._

class NumericStateInit(initWithSign: Boolean = false) extends State {
  var hasPreviousPunctuation = false
  var expectingAnotherNumber = initWithSign

  def nextState(letter: Char, tokenizer: Tokenizer): State = letter match {
    case digit if ('0' to '9').contains(digit) => {
      expectingAnotherNumber = false
      return this
    }

    case '.' if !hasPreviousPunctuation => {
      hasPreviousPunctuation = true
      expectingAnotherNumber = true
      return this
    }

    case symbol if (!expectingAnotherNumber && operatorsSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      tokenizer.finishToken(TokenType.Number)
      return new SymbolStateInit(symbol)
    }

    case 'E' if (!expectingAnotherNumber) => new NumericStateScientificNotation

    case endTokenSymbol if (!expectingAnotherNumber && endTokens.contains(endTokenSymbol)) => {
      tokenizer.finishToken(TokenType.Number)
      return new InitialState
    }

    case ';' => {
      if (!expectingAnotherNumber) {
        tokenizer.finishToken(TokenType.Number)

        tokenizer.registerToken(TokenType.Symbol, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case _ => new NotDefinedState
  }
}

class NumericStateScientificNotation extends State {
  var hasPreviousNumber: Boolean = false
  var hasSign: Boolean = false

  def nextState(letter: Char, tokenizer: Tokenizer): State = letter match {
    case digit if ('0' to '9').contains(digit) => {
      hasPreviousNumber = true
      return this
    }

    case '-' if (!hasSign && !hasPreviousNumber) => {
      hasSign = true
      return this
    }

    case symbol if (hasPreviousNumber && operatorsSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      tokenizer.finishToken(TokenType.Number)
      return new SymbolStateInit(symbol)
    }

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      tokenizer.finishToken(TokenType.Number)
      return new InitialState
    }

    case ';' => {
      if (hasPreviousNumber) {
        tokenizer.finishToken(TokenType.Number)

        tokenizer.registerToken(TokenType.Symbol, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case _ => new NotDefinedState
  }
}