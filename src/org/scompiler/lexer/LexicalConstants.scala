package org.scompiler.lexer

import collection.immutable.Set

object LexicalConstants {
  val letters = ('a' to 'z') union ('A' to 'Z')
  val numbers = ('0' to '9')

  val alphaNumeric = letters union numbers

  val endLineTokens = Array('\0', '\n')

  val endTokens = endLineTokens :+ ' ';

  val operatorsSymbols = Set("+", "-", "*", "/",
    "=", ":=", "<", ">",
    ">=", "<=", "<>")

  val othersSymbols = Set(":", ".", ",", ";", "(", ")",
    "[", "]", "{", "}", "..", "^")

  val reservedSymbols = operatorsSymbols union othersSymbols

  val reservedIdentifiers = Set("AND", "ARRAY", "BEGIN", "CASE", "CONST", "DIV", "DO", "DOWNTO",
    "ELSE", "END", "FILE", "FOR", "FUNCTION", "GOTO", "IF", "IN",
    "LABEL", "MOD", "NIL", "NOT", "OF", "OR", "PACKED", "PROCEDURE    ",
    "PROGRAM", "RECORD", "REPEAT", "SET", "THEN", "TO", "TYPE",
    "UNTIL", "VAR", "WHILE", "WITH", "USES")

  val reservedWords = reservedSymbols union reservedIdentifiers
}
