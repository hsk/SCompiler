package org.scompiler.lexer.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class NumericStateInit extends State {
  var hasPreviousPunctuation = false
  var expectingAnotherNumber = false

  private def doFinishToken(tokenBuffer: TokenBuffer) {
    if(!expectingAnotherNumber) {
      if (hasPreviousPunctuation) {
        tokenBuffer.finishToken(TokenType.RealNumber)
      } else {
        tokenBuffer.finishToken(TokenType.NaturalNumber)
      }
    } else {
      tokenBuffer.finishToken(TokenType.Undefined)
    }
  }

  def nextState(letter: Char, tokenBuffer: TokenBuffer): State = letter match {
    case digit if numbers contains digit => {
      expectingAnotherNumber = false
      return this
    }

    case '.' => {
      if(!hasPreviousPunctuation) {
        hasPreviousPunctuation = true
        expectingAnotherNumber = true
        return this
      } else if(expectingAnotherNumber) {
        tokenBuffer.finishToken(TokenType.NaturalNumber)
        tokenBuffer.registerCompleteToken(TokenType.Range, "..")
        return new InitialState
      } else {
        return new InvalidTokenState
      }
    }

    case 'E' | 'e' if (!expectingAnotherNumber) => new NumericStateScientificNotation

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      doFinishToken(tokenBuffer)

      return new InitialState
    }

    case ';' => {
      if (!expectingAnotherNumber) {
        doFinishToken(tokenBuffer)

        tokenBuffer.registerCompleteToken(TokenType.SemiColon, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case symbol if (!expectingAnotherNumber && reservedSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      doFinishToken(tokenBuffer)
      return new SymbolStateInit(symbol)
    }

    case _ => {
      if (!expectingAnotherNumber) {
        doFinishToken(tokenBuffer)
        return new NotDefinedState
      } else {
        return new InvalidTokenState
      }
    }
  }
}

class NumericStateScientificNotation extends State {
  var hasPreviousNumber: Boolean = false
  var hasSign: Boolean = false

  def nextState(letter: Char, tokenBuffer: TokenBuffer): State = letter match {
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
        tokenBuffer.finishToken(TokenType.ScientificNotationNumber)
      } else {
        tokenBuffer.finishToken(TokenType.Undefined)
      }
      return new InitialState
    }

    case ';' => {
      if (hasPreviousNumber) {
        tokenBuffer.finishToken(TokenType.ScientificNotationNumber)

        tokenBuffer.registerCompleteToken(TokenType.SemiColon, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case symbol if (hasPreviousNumber && reservedSymbols.exists( _.startsWith(symbol.toString) ) ) => {
      tokenBuffer.finishToken(TokenType.ScientificNotationNumber)
      return new SymbolStateInit(symbol)
    }

    case _ => {
      if (hasPreviousNumber) {
        tokenBuffer.finishToken(TokenType.ScientificNotationNumber)
        return new NotDefinedState
      } else {
        return new InvalidTokenState
      }
    }
  }
}