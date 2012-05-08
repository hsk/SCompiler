package org.scompiler.syntactic

import org.scompiler.lexer.Token
import org.scompiler.lexer.TokenType._
import collection.mutable.{ArrayBuffer}

object NonTerminalNode {
  val SEQ_IDENTIFIER = "~"
  val OR_IDENTIFIER = "|"
  val UNDEFINED_IDENTIFIER = ""

  val ONE_TO_MANY ="+"
  val ZERO_TO_MANY = "*"
  val OPTIONAL = "!"
  val ONLY_ONE = ""


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
  private var operation = UNDEFINED_IDENTIFIER
  private var cardinality = ONLY_ONE

  def parseToken(token: Token) {}

  override def toString: String = {
    val builder = new StringBuilder
    builder.append("(")

    if (cardinality.equals(OPTIONAL)) {
      builder.append("!")
    }

    builder.append(listOfNodes.mkString(" " + operation + " "))
    builder.append(")")

    if (!cardinality.equals(OPTIONAL)) {
      builder.append(cardinality)
    }

    return builder.toString
  }

  def setInitialNode(node: TerminalNode) {
    listOfNodes.clear()
    listOfNodes += node
  }

  def + : NonTerminalNode = {
    cardinality = ONE_TO_MANY
    this
  }

  def * : NonTerminalNode = {
    cardinality = ZERO_TO_MANY
    this
  }

  def unary_! : NonTerminalNode = {
    cardinality = OPTIONAL
    this
  }


  def ~ (tokenType: TokenType) : NonTerminalNode = {
    val terminalNode= TerminalNode.convertTokenToTerminalNode(tokenType)
    this.~(terminalNode)
  }
  def | (tokenType: TokenType) : NonTerminalNode = {
    val terminalNode = TerminalNode.convertTokenToTerminalNode(tokenType)
    this.|(terminalNode)
  }

  def ~ (otherNode: Node) : NonTerminalNode = operation match {
    case SEQ_IDENTIFIER | UNDEFINED_IDENTIFIER => {
      operation = SEQ_IDENTIFIER
      listOfNodes += otherNode
      return this
    }
    case OR_IDENTIFIER => {
      val newNode = new NonTerminalNode
      (newNode ~ this) ~ otherNode // Create a new node and do an OR operation with it
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