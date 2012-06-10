package org.scompiler.syntactic.nodes

import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.Token
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{NodeTraverseContext, Node}

class TerminalNode(tokenType: TokenType, expectedValue: Option[String]) extends Node {

  def this(tokenType: TokenType) = this(tokenType, None)

  override def traverseGraph(context: NodeTraverseContext) {
    val token = context.currentToken.get
    if (!tokenType.equals(token.tokenType)) {
      throw new WrongPathException(this)
    }
    if (expectedValue.isDefined && !expectedValue.get.equalsIgnoreCase(token.tokenName)) {
      throw new WrongPathException(this)
    }
  }

  override def toString = expectedValue.getOrElse(tokenType.toString)
}