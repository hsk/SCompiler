package org.scompiler.syntactic

import org.scompiler.lexer.Token
import org.scompiler.lexer.TokenType._
import collection.mutable.{ArrayBuffer}

object NonTerminalNode {
  val SEQ_IDENTIFIER = "+";
  val OR_IDENTIFIER = "|";
  val UNDEFINED_IDENTIFIER = "";

  implicit def convTokenTypeToNonTerminal(tokenType: TokenType) : NonTerminalNode =  {
    val terminal = TerminalNode.convertTokenToTerminalNode(tokenType)
    return convTerminalToNonTerminal(terminal)
  }

  implicit def convTerminalToNonTerminal(terminalNode: TerminalNode) : NonTerminalNode =  {
    val nonTerminal = new NonTerminalNode
    nonTerminal.setInitialNode(terminalNode)
    return nonTerminal
  }
}

import NonTerminalNode._

class NonTerminalNode extends Node{
  private var listOfNodes = new ArrayBuffer[Node]
  var operation = UNDEFINED_IDENTIFIER

  def parseToken(token: Token) {}

  override def toString = "(" + listOfNodes.mkString(" " + operation + " ") + ")"

  def setInitialNode(node: TerminalNode) {
    listOfNodes.clear()
    listOfNodes += node
  }

  def + (tokenType: TokenType) : NonTerminalNode = {
    val terminalNode= TerminalNode.convertTokenToTerminalNode(tokenType)
    this.+(terminalNode)
  }
  def | (tokenType: TokenType) : NonTerminalNode = {
    val terminalNode = TerminalNode.convertTokenToTerminalNode(tokenType)
    this.|(terminalNode)
  }

  def + (otherNode: Node) : NonTerminalNode = operation match {
    case SEQ_IDENTIFIER | UNDEFINED_IDENTIFIER => {
      operation = SEQ_IDENTIFIER
      listOfNodes += otherNode
      return this
    }
    case OR_IDENTIFIER => {
      val newNode = new NonTerminalNode
      (newNode + this) + otherNode // Create a new node and do an OR operation with it
      return newNode
    }
  }

  def | (otherNode: Node) : NonTerminalNode = operation match {
    case OR_IDENTIFIER | UNDEFINED_IDENTIFIER => {
      operation = OR_IDENTIFIER
      listOfNodes += otherNode
      return this
    }
    case SEQ_IDENTIFIER => {
      val newNode = new NonTerminalNode
      (newNode | this) | otherNode // Create a new node and do an SEQ operation with it
      return newNode
    }
  }
}