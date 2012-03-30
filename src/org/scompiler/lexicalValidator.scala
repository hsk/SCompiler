package org.scompiler
import scala.collection.mutable.LinkedList

import org.scompiler.states.InitialState
import org.scompiler.states.NotDefinedState
import org.scompiler.states.State

object lexicalValidator {
  def getTokens(input: Iterator[Char]): Iterator[Token] = {
    
    var state: State = new InitialState;
    val tokenizer : Tokenizer = new Tokenizer;
    val words = new LinkedList[String]
    
    input.takeWhile(_ => !state.isInstanceOf[NotDefinedState]).foreach ( letter => {
      state = state.nextState(letter, tokenizer);
      if(!state.isInstanceOf[InitialState]) {
        tokenizer.registerLetter(letter)
      }
    });
    
    state.nextState('\0', tokenizer);
    
    return tokenizer.iterator
  }
}