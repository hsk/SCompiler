package states

class InitialState extends State {
  def nextState(letter: Char): State = letter match {
    case digit if ('0' to '9').contains(digit) => new NumericStateInit;
    case '-' => new NumericStateInit(true);
    case _ => new NotDefinedState;
  }

  def isFinalState : Boolean = false;
}