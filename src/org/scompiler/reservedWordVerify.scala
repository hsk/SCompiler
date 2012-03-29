package org.scompiler

object reservedWordVerify {
  private val reservedWords = Array("AND", "ARRAY", "BEGIN", "CASE", "CONST", "DIV", "DO", "DOWNTO",
    "ELSE", "END", "FILE", "FOR", "FUNCTION", "GOTO", "IF", "IN",
    "LABEL", "MOD", "NIL", "NOT", "OF", "OR", "PACKED", "PROCEDURE    ",
    "PROGRAM", "RECORD", "REPEAT", "SET", "THEN", "TO", "TYPE",
    "UNTIL", "VAR", "WHILE", "WITH");

  private val reservedSymbols = Array("+", "-", "*", "/", "=", ":",
    ".", ",", ";", "'", ":=", "<",
    ">", ">=", "<=", "<>", "(", ")",
    "[", "]", "{", "}", "..", "^");

  private val reservedSet: Array[String] = reservedSymbols union reservedWords;

  def verify(word: String): Boolean = reservedSet contains word.toUpperCase()
}
