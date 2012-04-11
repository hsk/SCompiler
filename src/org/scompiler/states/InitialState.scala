package org.scompiler.states

import org.scompiler.util.TokenBuffer
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.TokenType

class InitialState extends State {
  def nextState(actualChar: Char, tokenizer: TokenBuffer): State = actualChar match {
    case '{' => new CommentaryState

    // String
    case '\'' => new StringState

    // Numbers
    case digit if numbers contains (digit) => new NumericStateInit

    //Verify if there are any symbol that starts with the given character
    case symbol if reservedSymbols exists (_.startsWith(symbol.toString)) => {
      new SymbolStateInit(symbol)
    }

    //Identifier
    case letter if letters contains letter => new IdentifierStateInit

    //Ignore spaces and line-breaks
    case endLine if endTokens contains endLine => this

    case ';' => {
      tokenizer.registerCompleteToken(TokenType.SemiColon, ";")

      return this
    }

    //Everything else is a error
    case _ => new InvalidTokenState
  }
}
