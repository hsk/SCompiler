package org.scompiler.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.LexicalTokenizer
import org.scompiler.syntactic.{NodeTraverseContext, GrammarGraph}
import org.scompiler.exception.WrongPathException


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

        setInitialNode('statement)
      }

      grammarGraph.getNonTerminal('numbers).get.toString should be("(NaturalNumber | RealNumber | ScientificNotationNumber)")
      grammarGraph.getNonTerminal('operation).get.toString should be("('numbers ~ (AddOperator | MinusOperator) ~ 'numbers)")
      grammarGraph.getNonTerminal('statement).get.toString should be("('variable ~ AttributionOperator ~ (10) ~ 'operation ~ SemiColon)+")
    }

    it("should be able to traverse the grammar graph") {
      val grammarGraph = new GrammarGraph {
        'variable ~> { Identifier }
        'numbers ~> { NaturalNumber | RealNumber | ScientificNotationNumber }
        'operation ~> { 'numbers ~ (AddOperator | MinusOperator) ~ 'numbers }
        'statement ~> { 'variable ~ AttributionOperator ~(10)~ 'operation }
        'statement_list ~> { 'statement ~ !(SemiColon ~ 'statement_list) }

        setInitialNode('statement_list)
      }

      val input =
        """
          myvar := 10 - 10.2;
          myvar2 := 10.2 + 1
        """
      val tokenizer = new LexicalTokenizer(input.iterator)
      val context = new NodeTraverseContext(tokenizer)

      try {
        grammarGraph.getNonTerminal('statement_list).get.traverseGraph(context)
      } catch {
        case ex: WrongPathException => fail("no valid path, expecting " + ex.nodeCause)
      }
      tokenizer.hasNext should be(false)
      context.hasFinishedTokens should be(true)
    }
  }
}
