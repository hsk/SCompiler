package org.scompiler

object TokenType extends Enumeration {
  type TokenType = Value

  val Undefined = Value("Undefined")
  val Number = Value("Number")
  val ReservedWord = Value("ReservedWord")
  val Symbol = Value("Symbol")
  val Identifier = Value("Identifier")
}