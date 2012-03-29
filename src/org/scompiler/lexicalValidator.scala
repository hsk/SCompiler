package org.scompiler
import org.scompiler.states.InitialState
import org.scompiler.states.NotDefinedState
import org.scompiler.states.State

object lexicalValidator {
  def getTokens(input: Iterator[Char]): Iterator[String] = {
    var state: State = new InitialState;
    val tokenizer : Tokenizer = new Tokenizer;
    input.takeWhile(_ => !state.isInstanceOf[NotDefinedState]).foreach {
      letter => state = state.nextState(letter, tokenizer);
    };
    return null
  }
}

