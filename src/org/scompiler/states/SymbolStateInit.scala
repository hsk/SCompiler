package org.scompiler.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class SymbolStateInit(firstChar: Char) extends State {
  var completeSymbol = firstChar.toString

  def nextState(actualChar: Char, tokenizer: TokenBuffer) : State = actualChar match {
    case symbol if reservedSymbols exists (_.startsWith(completeSymbol+symbol)) => {
      completeSymbol += symbol
      return this
    }

    case endTokenSymbol if endTokens contains endTokenSymbol => {
      tokenizer.finishToken(TokenType.Symbol)
      return new InitialState
    }

    case ';' => {
      if (reservedSymbols contains completeSymbol) {
        tokenizer.finishToken(TokenType.Symbol)

        tokenizer.registerCompleteToken(TokenType.Symbol, ";")

        return new InitialState
      } else {
        return new NotDefinedState
      }
    }

    case _ => {
      if (reservedSymbols contains completeSymbol) {
        tokenizer.finishToken(TokenType.Symbol)

        //force processing at InitState
        val initState = new InitialState
        return initState.nextState(actualChar, tokenizer)
      } else {
        return new NotDefinedState
      }
    }

  }
}
