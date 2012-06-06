package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._
import collection.mutable.ArrayBuffer
import org.scompiler.lexer.{LexicalConstants, Token}

object GrammarExpression {
  val SEQ_IDENTIFIER = "~"
  val OR_IDENTIFIER = "|"
  val UNDEFINED_IDENTIFIER = ""

  val ONE_OR_MANY ="+"
  val ZERO_OR_MANY = "*"
  val OPTIONAL = "!"
  val ONLY_ONE = ""
}

import GrammarExpression._

class GrammarExpression(graph: GrammarGraph, initialNode: Option[Node]) extends Node {

  private var locked = false;
  private var semanticHint: Int = 0;

  def lock() {
   locked = true;
  }

  def parseToken(token: Token) {}

  def this(graph: GrammarGraph) = this(graph, None)

  private var listOfNodes = new ArrayBuffer[Tuple2[Int,Node]]
  if(initialNode.isDefined) {
    listOfNodes += Tuple2[Int,Node](0, initialNode.get)
  }

  private var operation = UNDEFINED_IDENTIFIER
  private var cardinality = ONLY_ONE

  override def toString: String = {
    val builder = new StringBuilder

    if (cardinality.equals(OPTIONAL)) {
      builder.append("!")
    } else if(cardinality.equals(ONLY_ONE)) {
      builder.append("(")
    }

    builder.append(s = listOfNodes.map {
      node => {
        ((node._1 != 0) match {
          case true => "(" + node._1 + ")~ ";
          case _ => ""
        }) + (node._2 match {
          case node: NonTerminalNode => "'" + node.nodeName.name
          case _ => node._2.toString
        })
      }
    }.mkString(" " + operation + " "))

    if (!cardinality.equals(OPTIONAL)) {
      if(cardinality.equals(ONLY_ONE)) {
        builder.append(")")
      }
      builder.append(cardinality)
    }

    return builder.toString
  }

  private def withCardinality(cardinality: String) : GrammarExpression = {
    val newInnerExpr = new GrammarExpression(graph)
    newInnerExpr.cardinality = cardinality
    newInnerExpr.listOfNodes += Tuple2[Int,Node](0, this)

    newInnerExpr.lock()

    return newInnerExpr
  }

  def + : GrammarExpression = withCardinality(ONE_OR_MANY)

  def * : GrammarExpression = withCardinality(ZERO_OR_MANY)

  def unary_! : GrammarExpression = withCardinality(OPTIONAL)

  def ~ (sem: Int): GrammarExpression =  {
    semanticHint = sem
    this
  }

  def ~ (tokenType: TokenType) : GrammarExpression = {
    val terminalNode = graph.convertTokenTypeToTerminal(tokenType)
    this.~(terminalNode)
  }
  def | (tokenType: TokenType) : GrammarExpression = {
    val terminalNode = graph.convertTokenTypeToTerminal(tokenType)
    this.|(terminalNode)
  }
  def ~ (symbol: Symbol) : GrammarExpression = {
    var node: Option[Node] = None
    if (LexicalConstants.reservedIdentifiers.contains(symbol.name)) {
      node = Some(new TerminalNode(ReservedWord, Some(symbol.name)))
    }  else {
      node = Some(graph.convertSymbolToNonTerminal(symbol))
    }
    this.~(node.get)
  }
  def | (symbol: Symbol) : GrammarExpression = {

    var node: Option[Node] = None
    if (LexicalConstants.reservedIdentifiers.contains(symbol.name)) {
      node = Some(new TerminalNode(ReservedWord, Some(symbol.name)))
    }  else {
      node = Some(graph.convertSymbolToNonTerminal(symbol))
    }
    this.|(node.get)
  }

  def ~ (otherNode: Node) : GrammarExpression = operation match {
    case SEQ_IDENTIFIER | UNDEFINED_IDENTIFIER if !locked => {
      operation = SEQ_IDENTIFIER
      listOfNodes += Tuple2[Int, Node](semanticHint, otherNode)
      semanticHint = 0
      return this
    }
    case _ => {
      val newNode = new GrammarExpression(graph)
      (newNode ~ this) ~ otherNode // Create a new node and do an OR operation with it
      return newNode
    }
  }

  def | (otherNode: Node) : GrammarExpression = operation match {
    case OR_IDENTIFIER | UNDEFINED_IDENTIFIER if !locked  => {
      operation = OR_IDENTIFIER
      listOfNodes += Tuple2[Int, Node](semanticHint, otherNode)
      semanticHint = 0
      return this
    }
    case _ => {
      val newNode = new GrammarExpression(graph)
      (newNode | this) | otherNode // Create a new node and do an SEQ operation with it
      return newNode
    }
  }
}
