package org.scompiler

import LexicalConstants._

object reservedWordVerify {
  def verify(word: String): Boolean = reservedWords contains word.toUpperCase
}
