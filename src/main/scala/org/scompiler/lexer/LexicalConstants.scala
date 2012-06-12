package org.scompiler.lexer

import collection.immutable.Set
import collection.immutable.HashMap

object LexicalConstants {
  val letters = ('a' to 'z') union ('A' to 'Z')
  val numbers = ('0' to '9')

  val alphaNumeric = letters union numbers

  val endLineTokens = Array('\0', '\n')

  val endTokens = endLineTokens ++ Array(' ', '\t')

  val operatorsSymbols = Set("+", "-", "*", "/",
    "=", ":=", "<", ">",
    ">=", "<=", "<>")

  val othersSymbols = Set(":", ".", ",", ";", "(", ")",
    "[", "]", "{", "}", "..", "^")

  val reservedSymbols = operatorsSymbols union othersSymbols

  val reservedIdentifiers = Set("AND", "ARRAY", "BEGIN", "CASE", "CONST", "DIV", "DO", "DOWNTO",
    "ELSE", "END", "FILE", "FOR", "FUNCTION", "GOTO", "IF", "IN",
    "LABEL", "MOD", "NIL", "NOT", "OF", "OR", "PACKED", "PROCEDURE",
    "PROGRAM", "RECORD", "REPEAT", "SET", "THEN", "TO", "TYPE",
    "UNTIL", "VAR", "WHILE", "WITH", "USES")

  val reservedWords = reservedSymbols union reservedIdentifiers

  val symbolsTokenType = HashMap[String, TokenType.TokenType](
    "+" -> TokenType.AddOperator,"-" -> TokenType.MinusOperator,
    "*" -> TokenType.MultiplicationOperator, "/" -> TokenType.DivisionOperator,
    "=" -> TokenType.EqualsOperator, ":=" -> TokenType.AttributionOperator,
    "<" -> TokenType.SmallerThenOperator, "<=" -> TokenType.GreaterEqualsThenOperator,
    ">" -> TokenType.GreaterThenOperator, ">=" -> TokenType.GreaterEqualsThenOperator,
    "<>" -> TokenType.NotEqualsOperator, ":" -> TokenType.Colon,
    "." -> TokenType.Dot, "," -> TokenType.Comma, ";" -> TokenType.SemiColon,
    "(" -> TokenType.ParenthesisOpen, ")" -> TokenType.ParenthesisClose,
    "[" -> TokenType.BracketOpen, "]" -> TokenType.BracketClose,
    "{" -> TokenType.BraceOpen, "}" -> TokenType.BraceClose,
    ".." -> TokenType.Range, "^" -> TokenType.Pointer
  )
}
