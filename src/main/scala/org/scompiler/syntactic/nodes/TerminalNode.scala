package org.scompiler.syntactic.nodes

import org.scompiler.lexer.TokenType._
import org.scompiler.lexer.LexicalConstants._
import org.scompiler.lexer.Token
import org.scompiler.exception.WrongPathException
import org.scompiler.syntactic.{NodeTraverseContext, Node}

class TerminalNode(tokenType: TokenType, expectedValue: Option[String]) extends Node {

  def this(tokenType: TokenType) = this(tokenType, None)

  private val endOfStatementTokens = Array(SemiColon, ReservedWord)

  override def traverseGraph(context: NodeTraverseContext) {
    if (context.ignoreAllMode) {
      if (endOfStatementTokens.contains(tokenType)) {
        val tokenString = if (tokenType == SemiColon) ";" else expectedValue.get
        if (context.expectEndToken.equals(tokenString)) {
          context.ignoreAllMode = false
        }
      }
    } else {
      val token = context.consumeToken().getOrElse{ throw new WrongPathException(this) }
      if (!isValid(token)) {
        //if the current lexical token isn't valid, look for the next one.
        val nextToken = context.consumeToken(movePosition = false)
        if(nextToken.isDefined && !endOfStatementTokens.contains(token.tokenType) && context.allowError) {
          if (isValid(nextToken.get)) {
            // if next token is valid for current node
            // than register a error and assume a deletion of the unexpected token
            context.registerError(token, "unexpected aditional token")
            context.consumeToken() // consume to move the tokenBuffer onward
            return
          }
        }
        throw new WrongPathException(this)
      }
    }
  }

  override def toString = expectedValue.getOrElse(tokenType.toString)

  def isValid(token: Token): Boolean = {
    if (!tokenType.equals(token.tokenType)) {
      return false
    }
    if (expectedValue.isDefined && !expectedValue.get.equalsIgnoreCase(token.name)) {
      return false
    }
    return true
  }
}