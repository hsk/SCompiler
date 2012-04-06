package org.scompiler.states
import org.scompiler.Tokenizer
import org.scompiler.LexicalConstants._

class InitialState extends State {
  def nextState(actualChar: Char, tokenizer: Tokenizer): State = actualChar match {
    // String - Not Implemented
    case '\'' => new NotDefinedState

    // Numbers
    case digit if numbers contains (digit) => new NumericStateInit(false)

    // Possible Number(or symbol) - Partial implemented(only numeric behaviour)
    case '-' => new NumericStateInit(true)

    //Verify if there are any symbol that starts with the given character
    case symbol if reservedSymbols exists ( _.startsWith( symbol.toString ) )  => {

      //Get the list of possible symbols of the given character
      val possibleSymbols = reservedSymbols filter ( _.startsWith(symbol.toString) )
      // new SymbolStateInit(possibleSymbols)
      new NotDefinedState
    }

    //Identifier
    case letter if letters contains letter => new IdentifierStateInit

    //Ignore spaces and line-breaks
    case ' ' | '\n' => this

    //Everything else is a error
    case _ => new NotDefinedState
  }
}
