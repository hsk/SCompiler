package org.scompiler.syntactic

import expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.lexer.Token


/**
 * Sequence of nodes
 */
class SeqNode(graph: GrammarGraph) extends AbstractExpression(graph) {
  operation = ExpressionOperator.SEQUENCE;

  override def parseToken(token: Token) {
  }
}
