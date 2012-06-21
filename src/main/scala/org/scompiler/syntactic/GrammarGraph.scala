package org.scompiler.syntactic

import expression.AbstractExpression
import nodes.{SeqNode, NonTerminalNode, TerminalNode}
import org.scompiler.lexer.TokenType._
import collection.mutable.HashMap
import org.scompiler.lexer.LexicalConstants
import com.sun.org.apache.xml.internal.utils.WrongParserException
import org.scompiler.exception.{UnexpectedEndOfFile, WrongPathException}

trait GrammarGraph {
  private val terminals = new HashMap[TokenType, TerminalNode]
  private val nonTerminals = new HashMap[Symbol, NonTerminalNode]

  var initialNode : Node = null
  def setInitialNode(symbol: Symbol) {
    initialNode = convertSymbolToNonTerminal(symbol)
  }

  def getNonTerminal(nodeName: Symbol): Option[NonTerminalNode] = nonTerminals.get(nodeName)
  def getTerminal (tokenType: TokenType) : Option[TerminalNode] = terminals.get(tokenType)

  def setNonTerminal(nodeName: Symbol, node: NonTerminalNode) {
    nonTerminals.put(nodeName, node)
  }

  def setTerminal(tokenType: TokenType, node: TerminalNode) {
    terminals.put(tokenType, node)
  }

  implicit def convertStringToNode(value: String): TerminalNode = {
    return new TerminalNode(ReservedWord, Some(value))
  }

  implicit def convertTokenTypeToTerminal(tokenType: TokenType): TerminalNode = {
    val node = getTerminal(tokenType).getOrElse(new TerminalNode(tokenType))
    setTerminal(tokenType, node)

    return node
  }

  implicit def convertSymbolToNonTerminal (symbol : Symbol) : NonTerminalNode = {
    val node = getNonTerminal(symbol).getOrElse(new NonTerminalNode(symbol, this))
    setNonTerminal(symbol, node)
    return node
  }

  implicit def convertNodeToExpression [NodeExpr <% Node](node: NodeExpr): AbstractExpression = {
    if (node.isInstanceOf[AbstractExpression]) {
      return node.asInstanceOf[AbstractExpression]
    }
    val expr = new SeqNode
    expr.init(this, Some(node))
    return expr
  }


  def traverse(symbol: Symbol, context: NodeTraverseContext): Boolean = {
    try {
      try {
        getNonTerminal(symbol).get.traverseGraph(context)
        return true
      } catch {
        case err: WrongPathException => {
          try {
            tryTraverse(context, symbol, allowError = true, ignoreAllMode = false)
          } catch{
            case err:WrongPathException => {
              tryTraverse(context, symbol, allowError = true, ignoreAllMode = true)
            }
          }
        }
      }
    } catch {
      case err: WrongPathException => {
      }
      case err: UnexpectedEndOfFile => {
        println("Err - Unexpected end of file")
      }
      case err: StackOverflowError => {
        println("Err - Unexpected end of file")
      }
    }
    return false
  }

  def tryTraverse(context: NodeTraverseContext, symbol: Symbol, allowError: Boolean, ignoreAllMode: Boolean) {
    context.resetToPosition(0)
    context.resetErrorToPosition(0)
    context.allowError = allowError
    context.ignoreAllMode = ignoreAllMode
    getNonTerminal(symbol).get.traverseGraph(context)
  }
}
