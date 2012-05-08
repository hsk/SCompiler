package org.scompiler.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scompiler.syntactic.NonTerminalNode._
import org.scompiler.lexer.TokenType._
import org.scompiler.syntactic.NonTerminalNode


@RunWith(classOf[JUnitRunner])
class SyntacticTestCase extends FunSpec with ShouldMatchers {
  describe("A Syntatic component of a compiler") {

    it("shoud be able to build a simple grammar tree") {

      val variable = Identifier
      val numbers = NaturalNumber | RealNumber | ScientificNotationNumber
      val operation =  numbers ~ ( AddOperator | MinusOperator) ~ numbers
      val statement = (variable ~ AttributionOperator ~ operation ~ SemiColon)+

      numbers.toString should be("(NaturalNumber | RealNumber | ScientificNotationNumber)")
      operation.toString should be("((NaturalNumber | RealNumber | ScientificNotationNumber) ~ (AddOperator | MinusOperator) ~ (NaturalNumber | RealNumber | ScientificNotationNumber))")
      statement.toString should be("(Identifier ~ AttributionOperator ~ ((NaturalNumber | RealNumber | ScientificNotationNumber) ~ (AddOperator | MinusOperator) ~ (NaturalNumber | RealNumber | ScientificNotationNumber)) ~ SemiColon)+")
    }
  }
}
