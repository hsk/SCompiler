package org.scompiler.syntactic.expression

import org.scompiler.syntactic.expression.ExpressionOperator.{ExpressionOperator, _}
import org.scompiler.syntactic.{AlternativeSeqNode, SeqNode, GrammarGraph}

object ExpressionFactory {
  def createExpressionNode(graph: GrammarGraph, operator: ExpressionOperator): AbstractExpression = {
    operator match {
      case SEQUENCE => new SeqNode(graph)
      case ALTERNATIVE => new AlternativeSeqNode(graph)
      case _ => throw new RuntimeException("Unsuported operator '" + operator + "'")
    }
  }
}
