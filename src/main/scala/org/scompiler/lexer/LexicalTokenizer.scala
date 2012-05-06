package org.scompiler.lexer

import org.scompiler.lexer.states._
import org.scompiler.util.TokenBuffer
import states.InitialState

class LexicalTokenizer(input: Iterator[Char]) extends Iterator[Token] {

  private var actualCharacter: Char = '\0'
  private var state: State = new InitialState
  private val tokenBuffer: TokenBuffer = new TokenBuffer

  private def processToken() {

    while (input.hasNext) {
      if (state.isInstanceOf[NotDefinedState]) {
        tokenBuffer.finishToken(TokenType.Undefined)
        state = new InitialState
      } else {
        actualCharacter = input.next()
      }

      state = state.nextState(actualCharacter, tokenBuffer)

      if (!state.isInstanceOf[InitialState] && !state.isInstanceOf[NotDefinedState]) {
        tokenBuffer.registerCharacter(actualCharacter)
      }

      if (tokenBuffer.hasFinishedToken && input.hasNext) return;
    }

    state = state.nextState('\0', tokenBuffer)

    if (!state.isInstanceOf[InitialState]) {
      tokenBuffer.finishToken(TokenType.Undefined)
    }
  }

  def hasNext = input.hasNext || tokenBuffer.hasFinishedToken

  def next(): Token = {
    if (!tokenBuffer.hasFinishedToken) {
      processToken()
    }

    return tokenBuffer.consumeToken()
  }
}
