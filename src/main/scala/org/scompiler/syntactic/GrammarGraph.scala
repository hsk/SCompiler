package org.scompiler.syntactic

import org.scompiler.lexer.TokenType._
import collection.mutable.HashMap

trait GrammarGraph {
  private val terminals = new HashMap[TokenType, TerminalNode]
  private val nonTerminals = new HashMap[Symbol, NonTerminalNode]

  def getNonTerminal(nodeName: Symbol): Option[NonTerminalNode] = nonTerminals.get(nodeName)
  def getTerminal (tokenType: TokenType) : Option[TerminalNode] = terminals.get(tokenType)

  def setNonTerminal(nodeName: Symbol, node: NonTerminalNode) {
    nonTerminals.put(nodeName, node)
  }

  def setTerminal(tokenType: TokenType, node: TerminalNode) {
    terminals.put(tokenType, node)
  }

  implicit def convertSymbolToNonTerminal(symbol: Symbol): NonTerminalNode = {
    getNonTerminal(symbol).getOrElse(new NonTerminalNode(symbol, this))
  }

  implicit def convertSymbolToExpression(symbol: Symbol): GrammarExpression = {
    val expr = new GrammarExpression(this, Some(convertSymbolToNonTerminal(symbol)))

    return expr
  }

  implicit def convertTokenTypeToTerminal(tokenType: TokenType): TerminalNode = {
    val node = getTerminal(tokenType).getOrElse(new TerminalNode(tokenType))
    setTerminal(tokenType, node)

    return node
  }

  implicit def convertTokenTypeToExpression(tokenType: TokenType): GrammarExpression = {
    val expr = new GrammarExpression(this, Some(convertTokenTypeToTerminal(tokenType)))

    return expr
  }
}
