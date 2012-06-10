package org.scompiler.syntactic

import org.scompiler.exception.WrongPathException

abstract class Node {
  @throws(classOf[WrongPathException])
  def traverseGraph(context: NodeTraverseContext)
}
