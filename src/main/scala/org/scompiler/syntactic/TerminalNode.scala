package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.Token

class TerminalNode(tokenType : TokenType) extends Node {
  def parseToken(token: Token) {}
  override def toString = tokenType.toString
}

/**
 * Companion Object(hold static methods)
 * In this case the auto-conversion method
 */
object TerminalNode {
  /**
   * Auto convert any instance of TokenType to his correspondent TerminalToken
   *
   * TODO: Hold a map of terminalNode, so that one tokenType can have only one instance of TerminalNode
   *
   * @param tokenType
   * @return
   */
  implicit def convertTokenToTerminalNode(tokenType: TokenType) : TerminalNode = new TerminalNode(tokenType);
}
