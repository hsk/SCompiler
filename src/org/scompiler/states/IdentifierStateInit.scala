package org.scompiler.states

import org.scompiler.LexicalConstants._
import org.scompiler.{TokenType, Tokenizer}

class IdentifierStateInit extends State {
  def nextState(actualChar: Char, tokenizer: Tokenizer) : State = actualChar match {
    case letter if alphaNumeric contains letter => this

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      tokenizer.finishToken(TokenType.Identifier)
      return new InitialState
    }

    case ';' => {
      tokenizer.finishToken(TokenType.Identifier)

      tokenizer.registerCompleteToken(TokenType.Symbol, ";")

      return new InitialState
    }

    case symbol if reservedSymbols.exists( _.startsWith(symbol.toString) ) => {
      tokenizer.finishToken(TokenType.Identifier)
      return new SymbolStateInit(symbol)
    }

    case _ => new NotDefinedState
  }
}
