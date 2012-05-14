package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._
import collection.mutable.ArrayBuffer
import org.scompiler.lexer.Token

object GrammarExpression {
  val SEQ_IDENTIFIER = "~"
  val OR_IDENTIFIER = "|"
  val UNDEFINED_IDENTIFIER = ""

  val ONE_TO_MANY ="+"
  val ZERO_TO_MANY = "*"
  val OPTIONAL = "!"
  val ONLY_ONE = ""
}

import GrammarExpression._

class GrammarExpression(graph: GrammarGraph, initialNode: Option[Node]) extends Node {

  def parseToken(token: Token) {}

  def this(graph: GrammarGraph) = this(graph, None)

  private var listOfNodes = new ArrayBuffer[Node]
  if(initialNode.isDefined) {
    listOfNodes += initialNode.get
  }

  private var operation = UNDEFINED_IDENTIFIER
  private var cardinality = ONLY_ONE

  override def toString: String = {
    val builder = new StringBuilder
    builder.append("(")

    if (cardinality.equals(OPTIONAL)) {
      builder.append("!")
    }

    builder.append(listOfNodes.map{
      node => node match {
        case node: NonTerminalNode => "'" + node.nodeName.name
        case _ => node.toString
      }
    }.mkString(" " + operation + " "))
    builder.append(")")

    if (!cardinality.equals(OPTIONAL)) {
      builder.append(cardinality)
    }

    return builder.toString
  }

  def + : GrammarExpression = {
    cardinality = ONE_TO_MANY
    this
  }

  def * : GrammarExpression = {
    cardinality = ZERO_TO_MANY
    this
  }

  def unary_! : GrammarExpression = {
    cardinality = OPTIONAL
    this
  }

  def ~ (sem: Int): GrammarExpression = this

  def ~ (tokenType: TokenType) : GrammarExpression = {
    val terminalNode = graph.convertTokenTypeToTerminal(tokenType)
    this.~(terminalNode)
  }
  def | (tokenType: TokenType) : GrammarExpression = {
    val terminalNode = graph.convertTokenTypeToTerminal(tokenType)
    this.|(terminalNode)
  }
  def ~ (symbol: Symbol) : GrammarExpression = {
    val nonTerminal = graph.convertSymbolToNonTerminal(symbol)
    this.~(nonTerminal)
  }
  def | (symbol: Symbol) : GrammarExpression = {
    val nonTerminal = graph.convertSymbolToNonTerminal(symbol)
    this.|(nonTerminal)
  }

  def ~ (otherNode: Node) : GrammarExpression = operation match {
    case SEQ_IDENTIFIER | UNDEFINED_IDENTIFIER => {
      operation = SEQ_IDENTIFIER
      listOfNodes += otherNode
      return this
    }
    case OR_IDENTIFIER => {
      val newNode = new GrammarExpression(graph)
      (newNode ~ this) ~ otherNode // Create a new node and do an OR operation with it
      return newNode
    }
  }

  def | (otherNode: Node) : GrammarExpression = operation match {
    case OR_IDENTIFIER | UNDEFINED_IDENTIFIER => {
      operation = OR_IDENTIFIER
      listOfNodes += otherNode
      return this
    }
    case SEQ_IDENTIFIER => {
      val newNode = new GrammarExpression(graph)
      (newNode | this) | otherNode // Create a new node and do an SEQ operation with it
      return newNode
    }
  }
}
