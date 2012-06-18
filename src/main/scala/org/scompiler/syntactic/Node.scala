package org.scompiler.syntactic

import org.scompiler.exception.WrongPathException
import org.scompiler.lexer.Token

abstract class Node {
  @throws(classOf[WrongPathException])
  def traverseGraph(context: NodeTraverseContext)

  def isValid(token: Token): Boolean
}
