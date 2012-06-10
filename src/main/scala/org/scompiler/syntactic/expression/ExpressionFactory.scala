package org.scompiler.syntactic.expression

import org.scompiler.syntactic.expression.ExpressionOperator.{ExpressionOperator, _}
import org.scompiler.syntactic.nodes.{SeqNode, AlternativeSeqNode}
import org.scompiler.syntactic.{GrammarGraph}

object ExpressionFactory {
  def createExpressionNode(graph: GrammarGraph, operator: ExpressionOperator): AbstractExpression = {
    val expr = operator match {
      case SEQUENCE => new SeqNode
      case ALTERNATIVE => new AlternativeSeqNode
      case _ => throw new RuntimeException("Unsuported operator '" + operator + "'")
    }

    expr.init(graph, None)
    expr
  }
}
