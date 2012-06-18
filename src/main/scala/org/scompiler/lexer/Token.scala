package org.scompiler.lexer

import org.scompiler.lexer.TokenType._

case class Token(tokenType: TokenType, name: String, position: (Int, Int) = (0, 0)) {
  override def toString: String = "(" + tokenType.toString + " '" + name + "' at pos " + position + ")"
}