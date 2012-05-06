package org.scompiler.lexer.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType
import org.scompiler.lexer.TokenType._

class SymbolStateInit(firstChar: Char) extends State {
  var completeSymbol = firstChar.toString

  def tokenType: TokenType = {
    val token = symbolsTokenType.get(completeSymbol)
    if (token.isEmpty) {
      return TokenType.Symbol
    } else {
      return token.get
    }
  }

  def nextState(actualChar: Char, tokenBuffer: TokenBuffer) : State = actualChar match {
    case symbol if reservedSymbols exists (_.startsWith(completeSymbol+symbol)) => {
      completeSymbol += symbol
      return this
    }

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      tokenBuffer.finishToken(tokenType)
      return new InitialState
    }

    case ';' => {
      if (reservedSymbols contains completeSymbol) {
        tokenBuffer.finishToken(tokenType)

        tokenBuffer.registerCompleteToken(TokenType.SemiColon, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case _ => {
      if (reservedSymbols contains completeSymbol) {
        tokenBuffer.finishToken(tokenType)

        //force processing at InitState
        val initState = new InitialState
        return initState.nextState(actualChar, tokenBuffer)
      } else {
        return new NotDefinedState
      }
    }
  }
}
