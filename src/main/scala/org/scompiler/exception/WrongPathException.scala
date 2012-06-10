package org.scompiler.exception

import org.scompiler.syntactic.Node

class WrongPathException(val nodeCause: Node) extends Exception
