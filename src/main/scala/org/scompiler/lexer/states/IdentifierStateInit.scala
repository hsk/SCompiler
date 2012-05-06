package org.scompiler.lexer.states

import org.scompiler.lexer.LexicalConstants._
import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.TokenType

class IdentifierStateInit extends State {
  def nextState(actualChar: Char, tokenBuffer: TokenBuffer) : State = actualChar match {
    case letter if alphaNumeric contains letter => this

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      tokenBuffer.finishToken(TokenType.Identifier)
      return new InitialState
    }

    case ';' => {
      tokenBuffer.finishToken(TokenType.Identifier)

      tokenBuffer.registerCompleteToken(TokenType.SemiColon, ";")

      return new InitialState
    }

    case symbol if reservedSymbols.exists( _.startsWith(symbol.toString) ) => {
      tokenBuffer.finishToken(TokenType.Identifier)
      return new SymbolStateInit(symbol)
    }

    case _ => {
      tokenBuffer.finishToken(TokenType.Identifier)

      return new InvalidTokenState
    }
  }
}
