package org.scompiler.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class NumericStateInit extends State {
  var hasPreviousPunctuation = false
  var expectingAnotherNumber = false

  private def doFinishToken(tokenizer: TokenBuffer) {
    if(!expectingAnotherNumber) {
      if (hasPreviousPunctuation) {
        tokenizer.finishToken(TokenType.RealNumber)
      } else {
        tokenizer.finishToken(TokenType.NaturalNumber)
      }
    } else {
      tokenizer.finishToken(TokenType.Undefined)
    }
  }

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

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      doFinishToken(tokenizer)

      return new InitialState
    }

    case ';' => {
      if (!expectingAnotherNumber) {
        doFinishToken(tokenizer)

        tokenizer.registerCompleteToken(TokenType.SemiColon, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case symbol if (!expectingAnotherNumber && reservedSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      doFinishToken(tokenizer)
      return new SymbolStateInit(symbol)
    }

    case _ => new InvalidTokenState
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

    case '+' if (!hasSign && !hasPreviousNumber) => {
      hasSign = true
      return this
    }

    case '-' if (!hasSign && !hasPreviousNumber) => {
      hasSign = true
      return this
    }

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      if(hasPreviousNumber) {
        tokenizer.finishToken(TokenType.ScientificNotationNumber)
      } else {
        tokenizer.finishToken(TokenType.Undefined)
      }
      return new InitialState
    }

    case ';' => {
      if (hasPreviousNumber) {
        tokenizer.finishToken(TokenType.ScientificNotationNumber)

        tokenizer.registerCompleteToken(TokenType.SemiColon, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case symbol if (hasPreviousNumber && reservedSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      tokenizer.finishToken(TokenType.ScientificNotationNumber)
      return new SymbolStateInit(symbol)
    }

    case _ => new InvalidTokenState
  }
}