package org.scompiler.syntactic

import org.scompiler.lexer.Token

abstract class Node {
  def parseToken(token: Token);
}
