package org.scompiler

import org.scompiler.states.InitialState
import org.scompiler.states.NotDefinedState
import org.scompiler.states.State

class LexicalValidator {
  def processTokens(input: Iterator[Char]): Iterator[Token] = {

    var actualCharacter: Char = '\0'
    var state: State = new InitialState
    val tokenizer: Tokenizer = new Tokenizer

    while(input.hasNext) {
      if(state.isInstanceOf[NotDefinedState]) {
        tokenizer.finishToken(TokenType.Undefined)
        state = new InitialState
      } else {
        actualCharacter = input.next()
      }

      state = state.nextState(actualCharacter, tokenizer)

      if (!state.isInstanceOf[InitialState] && !state.isInstanceOf[NotDefinedState]) {
        tokenizer.registerCharacter(actualCharacter)
      }
    }

    state = state.nextState('\0', tokenizer)

    if (!state.isInstanceOf[InitialState]) {
      tokenizer.finishToken(TokenType.Undefined)
    }

    return tokenizer.iterator
  }
}
