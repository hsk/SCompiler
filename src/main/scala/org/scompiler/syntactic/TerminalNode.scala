package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.Token

class TerminalNode(tokenType: TokenType, expectedValue: Option[String]) extends Node {

  def this(tokenType: TokenType) = this(tokenType, None)

  def parseToken(token: Token) {}
  override def toString = expectedValue.getOrElse(tokenType.toString)
}

/**
 * Companion Object(hold static methods)
 * In this case the auto-conversion method
 */
object TerminalNode {
  /**
   * Auto convert any instance of TokenType to his correspondent TerminalToken
   *
   * @param tokenType
   * @return
   */
  implicit def convertTokenToTerminalNode(tokenType: TokenType) : TerminalNode = new TerminalNode(tokenType);
}
