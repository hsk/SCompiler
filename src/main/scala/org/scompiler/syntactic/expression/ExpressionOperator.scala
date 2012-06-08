package org.scompiler.syntactic.expression

object ExpressionOperator extends Enumeration {

  type ExpressionOperator = Value

  val SEQUENCE = Value("~")
  val ALTERNATIVE = Value("|")
  val UNDEFINED = Value("")
}
