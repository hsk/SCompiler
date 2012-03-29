package states

abstract class State{
  def nextState(letter: Char): State;
  def isFinalState : Boolean;
}
