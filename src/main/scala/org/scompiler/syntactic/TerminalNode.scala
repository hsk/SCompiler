package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.Token
import org.scompiler.exception.WrongPathException

class TerminalNode(tokenType: TokenType, expectedValue: Option[String]) extends Node {

  def this(tokenType: TokenType) = this(tokenType, None)

  def parseToken(token: Token) {
    if(!tokenType.equals(token.tokenType)) {
      throw new WrongPathException(this)
    }
  }
  override def toString = expectedValue.getOrElse(tokenType.toString)
}