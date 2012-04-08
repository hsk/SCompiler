package org.scompiler.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class NumericStateInit(initWithSign: Boolean = false) extends State {
  var hasPreviousPunctuation = false
  var expectingAnotherNumber = initWithSign

  def nextState(letter: Char, tokenizer: TokenBuffer): State = letter match {
    case digit if numbers contains digit => {
      expectingAnotherNumber = false
      return this
    }

    case '.' if !hasPreviousPunctuation => {
      hasPreviousPunctuation = true
      expectingAnotherNumber = true
      return this
    }

    case 'E' if (!expectingAnotherNumber) => new NumericStateScientificNotation

    case endTokenSymbol if (!expectingAnotherNumber && endTokens.contains(endTokenSymbol)) => {
      tokenizer.finishToken(TokenType.Number)
      return new InitialState
    }

    case ';' => {
      if (!expectingAnotherNumber) {
        tokenizer.finishToken(TokenType.Number)

        tokenizer.registerCompleteToken(TokenType.Symbol, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case symbol if (!expectingAnotherNumber && reservedSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      tokenizer.finishToken(TokenType.Number)
      return new SymbolStateInit(symbol)
    }

    case _ => new NotDefinedState
  }
}

class NumericStateScientificNotation extends State {
  var hasPreviousNumber: Boolean = false
  var hasSign: Boolean = false

  def nextState(letter: Char, tokenizer: TokenBuffer): State = letter match {
    case digit if ('0' to '9').contains(digit) => {
      hasPreviousNumber = true
      return this
    }

    case '-' if (!hasSign && !hasPreviousNumber) => {
      hasSign = true
      return this
    }

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      tokenizer.finishToken(TokenType.Number)
      return new InitialState
    }

    case ';' => {
      if (hasPreviousNumber) {
        tokenizer.finishToken(TokenType.Number)

        tokenizer.registerCompleteToken(TokenType.Symbol, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case symbol if (hasPreviousNumber && reservedSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      tokenizer.finishToken(TokenType.Number)
      return new SymbolStateInit(symbol)
    }

    case _ => new NotDefinedState
  }
}