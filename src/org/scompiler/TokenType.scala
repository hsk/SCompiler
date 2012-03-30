package org.scompiler

object TokenType extends Enumeration {
  type TokenType = Value

  val Indefined = Value("Indefined")
  val Number = Value("Number")
  val ReservedWord = Value("ReservedWord")
  val Symbol = Value("Symbol")
  val Identifier = Value("Identifier")
}