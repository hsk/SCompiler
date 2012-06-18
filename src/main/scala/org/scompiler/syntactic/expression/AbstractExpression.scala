package org.scompiler.syntactic.expression

import collection.mutable.ArrayBuffer
import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.{LexicalConstants, Token}
import org.scompiler.syntactic.expression.ExpressionCardinality.{ExpressionCardinality, _}
import org.scompiler.syntactic.expression.ExpressionOperator.{ExpressionOperator, _}
import org.scompiler.syntactic._
import nodes.{CardinalityNode, SeqNode, NonTerminalNode, TerminalNode}
import org.scompiler.exception.WrongPathException
import collection.mutable

trait AbstractExpression {

  protected var listOfNodes = new mutable.MutableList[(Node,Int)]
  private var locked = false;
  protected var operation: ExpressionOperator = UNDEFINED
  protected var cardinality: ExpressionCardinality = ONLY_ONE
  protected var graph: GrammarGraph = null

  def init(graph: GrammarGraph, initialNode: Option[Node]) {
    if(initialNode.isDefined) {
      listOfNodes += Tuple2(initialNode.get, 0)
    }
    this.graph = graph
  }

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
    val newInnerExpr = new CardinalityNode(this.asInstanceOf[Node])
    newInnerExpr.init(graph, None)
    newInnerExpr.cardinality = cardinality
    newInnerExpr.listOfNodes += Tuple2(this.asInstanceOf[Node], 0)
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

  def seqNode(node: Node) : AbstractExpression = this.~(node)

  def altNode(node: Node) : AbstractExpression = this.|(node)

  def ~ (expr: AbstractExpression) : AbstractExpression = {
    if(expr.isInstanceOf[Node]) {
      this.~(expr.asInstanceOf[Node])
    } else {
      throw new RuntimeException
    }
  }

  def | (expr: AbstractExpression) : AbstractExpression = {
    if(expr.isInstanceOf[Node]) {
      this.|(expr.asInstanceOf[Node])
    } else {
      throw new RuntimeException
    }
  }

  def ~ [NodeF <% Node](otherNode: NodeF) : AbstractExpression = operation match {
    case SEQUENCE if !locked => {
      operation = SEQUENCE
      listOfNodes += Tuple2(otherNode, 0)
      return this
    }
    case _ => {
      val newNode = ExpressionFactory.createExpressionNode(graph, SEQUENCE)
      if(!listOfNodes.isEmpty){
        if (listOfNodes.size == 1 && !locked) {
          newNode.listOfNodes += listOfNodes.head
        } else {
          newNode.~(this)
        }
      }

      newNode.seqNode(otherNode)
      newNode
    }
  }

  def | [NodeF <% Node](otherNode: NodeF) : AbstractExpression = operation match {
    case ALTERNATIVE if !locked  => {
      operation = ALTERNATIVE
      listOfNodes += Tuple2(otherNode, 0)
      return this
    }
    case _ => {
      val newNode = ExpressionFactory.createExpressionNode(graph, ALTERNATIVE)
      if (!listOfNodes.isEmpty) {
        if (listOfNodes.size == 1 && !locked) {
          newNode.listOfNodes += listOfNodes.head
        } else {
          newNode.|(this)
        }
      }

      newNode.altNode(otherNode)
    }
  }
}
