import states.{InitialState, State, NotDefinedState}

object NumericValidatorExample {
  def main(args : Array[String]) {    
    Iterator.continually(Console.readLine()).takeWhile(_ != null).foreach{ line =>
      var state : State = new InitialState;
      
      line.takeWhile(_ => !state.isInstanceOf[NotDefinedState] ).foreach {
        letter => state = state.nextState(letter);
      };
      
      Console.println((if (state.isFinalState) "    É Numero" else "Nao É Numero") + " -> " + line);
    }
  }
}

