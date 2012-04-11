package org.scompiler.lexer

object TokenType extends Enumeration {

  type TokenType = Value

  val Undefined = Value("Undefined")
  val NaturalNumber = Value("NaturalNumber")
  val RealNumber = Value("RealNumber")
  val ScientificNotationNumber = Value("ScientificNotationNumber")
  val ReservedWord = Value("ReservedWord")
  val Symbol = Value("Symbol")
  val Identifier = Value("Identifier")
  val String = Value("String")
  val Commentary = Value("Commentary")

  //Simbols:
  val AddOperator = Value("AddOperator")
  val MinusOperator = Value("MinusOperator")
  val MultiplicationOperator = Value("MultiplicationOperator")
  val DivisionOperator = Value("DivisionOperator")
  val AttributionOperator = Value("AttributionOperator")

  val EqualsOperator = Value("EqualsOperator")
  val NotEqualsOperator = Value("NotEqualsOperator")

  val SmallerThenOperator = Value("SmallerThenOperator")
  val GreaterThenOperator = Value("GreaterThenOperator")
  val GreaterEqualsThenOperator = Value("GreaterEqualsThenOperator")
  val SmallerEqualsThenOperator = Value("SmallerEqualsThenOperator")

  // ..
  val Range = Value("Range")
  // ^
  val Pointer = Value("Pointer")

  // .
  val Dot = Value("Dot")
  // ;
  val SemiColon = Value("SemiColon")
  // :
  val Colon = Value("Colon")
  // ,
  val Comma = Value("Comma")

  // ( e )
  val ParenthesisOpen = Value("ParenthesisOpen")
  val ParenthesisClose = Value("ParenthesisClose")

  // [ e ]
  val BracketOpen = Value("BracketOpen")
  val BracketClose = Value("BracketClose")

  // { }
  val BraceOpen = Value("BraceOpen")
  val BraceClose = Value("BraceClose")
}