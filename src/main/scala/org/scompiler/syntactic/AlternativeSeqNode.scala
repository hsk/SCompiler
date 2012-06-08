package org.scompiler.syntactic

import expression.{ExpressionOperator, AbstractExpression}
import org.scompiler.lexer.Token

class AlternativeSeqNode(graph: GrammarGraph) extends AbstractExpression(graph) {
  operation = ExpressionOperator.ALTERNATIVE;

  override def parseToken(token: Token) {

  }
}
