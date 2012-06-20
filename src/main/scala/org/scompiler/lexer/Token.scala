package org.scompiler.lexer

import org.scompiler.lexer.TokenType._

case class Token(tokenType: TokenType, name: String, position: (Int, Int) = (0, 0)) {
  override def toString: String = "(" + tokenType.toString + " '" + name + "' at pos " + position + ")"

  override def equals(obj: Any): Boolean = {
    if (obj == null || !obj.isInstanceOf[Token]) {
      return false
    }

    val otherToken = obj.asInstanceOf[Token]

    return tokenType.equals(otherToken.tokenType) && name.equals(otherToken.name)
  }
}