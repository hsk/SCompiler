package org.scompiler
import org.scompiler.TokenType._

class Token(val tokenType: TokenType, val tokenName: String) {
  
  override def equals (other: Any) : Boolean = other match {
      case t: Token => (tokenType.equals(t.tokenType)) && (tokenName.equals(t.tokenName))
      case _ => false
  }  
  override def toString : String = tokenName + "(" + tokenType.toString() + ")" 
}