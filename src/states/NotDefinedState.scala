package states


class NotDefinedState extends State {
  def nextState(letter : Char) = this;
  def isFinalState = false;
}