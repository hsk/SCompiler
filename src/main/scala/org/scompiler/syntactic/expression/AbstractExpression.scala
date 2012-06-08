package org.scompiler.syntactic.expression

import collection.mutable.ArrayBuffer
import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.{LexicalConstants, Token}
import org.scompiler.syntactic.expression.ExpressionCardinality.{ExpressionCardinality, _}
import org.scompiler.syntactic.expression.ExpressionOperator.{ExpressionOperator, _}
import org.scompiler.syntactic._

class AbstractExpression(graph: GrammarGraph, initialNode: Option[Node]) extends Node {
  def this(graph: GrammarGraph) = this(graph, None)

  protected var listOfNodes = new ArrayBuffer[(Node,Int)]
  private var locked = false;
  protected var operation: ExpressionOperator = UNDEFINED
  protected var cardinality: ExpressionCardinality = ONLY_ONE

  if(initialNode.isDefined) {
    listOfNodes += Tuple2(initialNode.get, 0)
  }

  def parseToken(token: Token) {}

  def lock() {
    locked = true;
  }

  override def toString: String = {
    val builder = new StringBuilder

    if (cardinality.equals(OPTIONAL)) {
      builder.append("!")
    } else if(cardinality.equals(ONLY_ONE)) {
      builder.append("(")
    }

    builder.append(s = listOfNodes.map {
      node => {
        ((node._1 match {
          case node: NonTerminalNode => "'" + node.nodeName.name
          case _ => node._1.toString
        }) + ((node._2 != 0) match {
          case true => " ~ (" + node._2 + ")";
          case _ => ""
        }))
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

  private def withCardinality(cardinality: ExpressionCardinality) : AbstractExpression = {
    val newInnerExpr = new AbstractExpression(graph)
    newInnerExpr.cardinality = cardinality
    newInnerExpr.listOfNodes += Tuple2(this, 0)

    newInnerExpr.lock()

    return newInnerExpr
  }

  def + : AbstractExpression = withCardinality(ONE_OR_MANY)

  def * : AbstractExpression = withCardinality(ZERO_OR_MANY)

  def unary_! : AbstractExpression = withCardinality(OPTIONAL)

  def ~ (semanticHint: Int): AbstractExpression =  {
    if(!listOfNodes.isEmpty) {
      val lastNode = listOfNodes.last
      listOfNodes = listOfNodes.init :+ (lastNode._1, semanticHint)
    }
    this
  }

  def ~ (tokenType: TokenType) : AbstractExpression = {
    val terminalNode = graph.convertTokenTypeToTerminal(tokenType)
    this.~(terminalNode)
  }
  def | (tokenType: TokenType) : AbstractExpression = {
    val terminalNode = graph.convertTokenTypeToTerminal(tokenType)
    this.|(terminalNode)
  }
  def ~ (symbol: Symbol) : AbstractExpression = {
    var node: Option[Node] = None
    if (LexicalConstants.reservedIdentifiers.contains(symbol.name)) {
      node = Some(new TerminalNode(ReservedWord, Some(symbol.name)))
    }  else {
      node = Some(graph.convertSymbolToNonTerminal(symbol))
    }
    this.~(node.get)
  }
  def | (symbol: Symbol) : AbstractExpression = {

    var node: Option[Node] = None
    if (LexicalConstants.reservedIdentifiers.contains(symbol.name)) {
      node = Some(new TerminalNode(ReservedWord, Some(symbol.name)))
    }  else {
      node = Some(graph.convertSymbolToNonTerminal(symbol))
    }
    this.|(node.get)
  }

  def ~ (otherNode: Node) : AbstractExpression = operation match {
    case SEQUENCE if !locked => {
      operation = SEQUENCE
      listOfNodes += Tuple2(otherNode, 0)
      return this
    }
    case _ => {
      val newNode = ExpressionFactory.createExpressionNode(graph, SEQUENCE)
      if(!listOfNodes.isEmpty){
        if (listOfNodes.size == 1) {
          newNode.listOfNodes += listOfNodes.head
        } else {
          newNode.~(this)
        }
      }

      newNode.~(otherNode)
    }
  }

  def | (otherNode: Node) : AbstractExpression = operation match {
    case ALTERNATIVE if !locked  => {
      operation = ALTERNATIVE
      listOfNodes += Tuple2(otherNode, 0)
      return this
    }
    case _ => {
      val newNode = ExpressionFactory.createExpressionNode(graph, ALTERNATIVE)
      if (!listOfNodes.isEmpty) {
        if (listOfNodes.size == 1) {
          newNode.listOfNodes += listOfNodes.head
        } else {
          newNode.|(this)
        }
      }

      newNode.|(otherNode)
    }
  }
}
