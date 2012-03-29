import scala.collection.immutable.Set

object ReservedWordExample {
  val reservedWords = Array("AND", "ARRAY", "BEGIN", "CASE", "CONST", "DIV", "DO", "DOWNTO",
    "ELSE", "END", "FILE", "FOR", "FUNCTION", "GOTO", "IF", "IN",
    "LABEL", "MOD", "NIL", "NOT", "OF", "OR", "PACKED", "PROCEDURE    ",
    "PROGRAM", "RECORD", "REPEAT", "SET", "THEN", "TO", "TYPE",
    "UNTIL", "VAR", "WHILE", "WITH");

  val reservedSymbols = Array("+", "-", "*", "/", "=", ":",
    ".", ",", ";", "'", ":=", "<",
    ">", ">=", "<=", "<>", "(", ")",
    "[", "]", "{", "}", "..", "^");

  val reservedSet: Array[String] = reservedSymbols union reservedWords;

  def printVerify(word: String) = Console.println(verify(word))

  def verify(word: String) = if (reservedSet.contains(word.toUpperCase())) "Reservado" else "Não Reservado"

  def main(args: Array[String]) = Iterator.continually(Console.readLine()).takeWhile(!equals(_, "")).foreach(printVerify);
}
