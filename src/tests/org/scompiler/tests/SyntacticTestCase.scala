package org.scompiler.tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.LexicalTokenizer
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{PascalGrammarGraph, NodeTraverseContext, GrammarGraph}


@RunWith(classOf[JUnitRunner])
class SyntacticTestCase extends FunSpec with ShouldMatchers {
  def testTraverse(graph: GrammarGraph, input: String, initialNode: Symbol) {
    val tokenizer = new LexicalTokenizer(input.iterator)
    val context = new NodeTraverseContext(tokenizer)

    graph.getNonTerminal(initialNode).get.traverseGraph(context)
    tokenizer.hasNext should be(false)
    context.hasFinishedTokens should be(true)
  }

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

      val input = " myvar := 10 - 10.2; myvar := 10 + 10.2; myvar := 2.2 - 1"
      testTraverse(grammarGraph, input, 'statement_list)

      val input2 = " myvar := 10 - 10.2; myvar := 10 - 10.2"
      testTraverse(grammarGraph, input2, 'statement_list)

      val input3 = " myvar := 10 - 10.2"
      testTraverse(grammarGraph, input3, 'statement_list)
    }

    it("should be able to traverse a grammar graph with cardinality") {
      val grammarGraph = new GrammarGraph {
        'variable ~> { Identifier }
        'numbers ~> { NaturalNumber | RealNumber | ScientificNotationNumber }
        'operation ~> { 'numbers ~ (AddOperator | MinusOperator) ~ 'numbers }
        'statement ~> { 'variable ~ AttributionOperator ~(10)~ 'operation }
        'statement_list ~> { 'statement ~ (SemiColon ~ 'statement).+ }

        setInitialNode('statement_list)
      }

      val input = " myvar := 10 - 10.2; myvar := 10 + 10.2; myvar := 2.2 - 1"
      testTraverse(grammarGraph, input, 'statement_list)

      val input2 = " myvar := 10 - 10.2; myvar := 10 - 10.2"
      testTraverse(grammarGraph, input2, 'statement_list)

      val input3 = " myvar := 10 - 10.2"
      try {
        testTraverse(grammarGraph, input3, 'statement_list)
        fail("expect two statements at minimum")
      } catch {
        case ex: WrongPathException => {/* Expected */ }
      }
    }

    it ("Should recognize an entire Pascal program"){
      val pascalGrammar = new PascalGrammarGraph

      val input = "program test; begin end. "
      try {
        testTraverse(pascalGrammar, input, 'program)
      } catch {
        case ex: WrongPathException => fail("fail at node " + ex.nodeCause)
      }

    }
  }
}
