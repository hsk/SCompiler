package org.scompiler.syntactic.expression

object ExpressionCardinality extends Enumeration {

  type ExpressionCardinality = Value

  val ONE_OR_MANY = Value("+")
  val ZERO_OR_MANY = Value("*")
  val OPTIONAL = Value("!")
  val ONLY_ONE = Value("")
}
