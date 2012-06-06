package org.scompiler.lexer

import org.scompiler.lexer.TokenType._

case class Token(val tokenType: TokenType, val tokenName: String) {
  override def toString: String = "(" + tokenType.toString + ", " + tokenName + " )"
}