package org.scompiler.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scompiler.lexer.TokenType
import org.scompiler.lexer.TokenType._
import org.scompiler.syntactic.{PascalGrammarGraph, GrammarGraph}


@RunWith(classOf[JUnitRunner])
class SyntacticTestCase extends FunSpec with ShouldMatchers {
  describe("A Syntatic component of a compiler") {

    it("should be able to build a simple grammar graph") {

      val grammarGraph = new GrammarGraph {
        'variable ~> { Identifier }
        'numbers ~> { NaturalNumber | RealNumber | ScientificNotationNumber }
        'operation ~> { 'numbers ~ (AddOperator | MinusOperator) ~ 'numbers }
        'statement ~> { ('variable ~ AttributionOperator ~ 'operation ~ SemiColon)+ }
      }

      grammarGraph.getNonTerminal('numbers).get.toString should be("(NaturalNumber | RealNumber | ScientificNotationNumber)")
      grammarGraph.getNonTerminal('operation).get.toString should be("('numbers ~ (AddOperator | MinusOperator) ~ 'numbers)")
      grammarGraph.getNonTerminal('statement).get.toString should be("('variable ~ AttributionOperator ~ 'operation ~ SemiColon)+")
    }

    it("should be able to specify semantic hint information") {

      val grammarGraph = new GrammarGraph {
        'variable ~> { Identifier }
        'numbers ~> { NaturalNumber | RealNumber | ScientificNotationNumber }
        'operation ~> { 'numbers ~ (AddOperator | MinusOperator) ~ 'numbers }
        'statement ~> { ('variable ~ AttributionOperator ~(10)~ 'operation ~ SemiColon)+ }
      }

      grammarGraph.getNonTerminal('numbers).get.toString should be("(NaturalNumber | RealNumber | ScientificNotationNumber)")
      grammarGraph.getNonTerminal('operation).get.toString should be("('numbers ~ (AddOperator | MinusOperator) ~ 'numbers)")
      grammarGraph.getNonTerminal('statement).get.toString should be("('variable ~ AttributionOperator ~ (10)~ 'operation ~ SemiColon)+")
    }
  }
}
