package tests

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scompiler.lexer.{TokenType, Token, LexicalTokenizer}
import org.scalatest.matchers.ShouldMatchers
import org.scompiler.lexer.LexicalConstants._

@RunWith(classOf[JUnitRunner])
class LexicalTestCase extends FunSpec with ShouldMatchers {
  private def verifyReservedWord(word: String) : Boolean = reservedWords contains word.toUpperCase

  val assertWordIsReserved = (word: String) => assert(verifyReservedWord(word), word + " Should be reserved")
  val assertWordIsNotReserved = (word: String) => assert(!verifyReservedWord(word), word + " Should not be reserved")

  describe("A lexical component of a compiler") {

    it("shoud identify reserved words") {

      assertWordIsReserved("if")
      assertWordIsReserved("iF")
      assertWordIsReserved("If")
      assertWordIsReserved("IF")
      assertWordIsReserved("and")
      assertWordIsReserved("+")
      assertWordIsReserved("nOt")
      assertWordIsReserved("OR")
      assertWordIsReserved("case")

      assertWordIsNotReserved("elseif")
      assertWordIsNotReserved("when")
      assertWordIsNotReserved("%$!@#")
    }

    it("should identify numbers") {
      val input: String = "1234.123 123 0 1.0E-1"
      val expected: Array[Token] = Array(
        new Token(TokenType.RealNumber, "1234.123"),
        new Token(TokenType.NaturalNumber, "123"),
        new Token(TokenType.NaturalNumber, "0"),
        new Token(TokenType.ScientificNotationNumber, "1.0E-1")
      )
      val lexicalTokenizer = new LexicalTokenizer(input.iterator)

      val result: Array[Token] = lexicalTokenizer.toArray

      result should have length (4)
      result should be(expected)
    }

    it("should identify correct tokens between wrong tokens") {
      val input: String = "123WWW 213 1.23"
      val expected = Array(
        new Token(TokenType.Undefined, "123WWW"),
        new Token(TokenType.NaturalNumber, "213"),
        new Token(TokenType.RealNumber, "1.23")
      )

      val lexicalTokenizer = new LexicalTokenizer(input.iterator)

      val result: Array[Token] = lexicalTokenizer.toArray

      result should have length (3)
      result should be(expected)
    }

    it("should identify multiple statements") {
      val input: String = "ID1 := 1234;\nid2:=5.1E10-ID1;";

      val expected = Array(
        new Token(TokenType.Identifier, "ID1"),
        new Token(TokenType.AttributionOperator, ":="),
        new Token(TokenType.NaturalNumber, "1234"),
        new Token(TokenType.SemiColon, ";"),

        new Token(TokenType.Identifier, "id2"),
        new Token(TokenType.AttributionOperator, ":="),
        new Token(TokenType.ScientificNotationNumber, "5.1E10"),
        new Token(TokenType.MinusOperator, "-"),
        new Token(TokenType.Identifier, "ID1"),
        new Token(TokenType.SemiColon, ";")
      )

      val lexicalTokenizer = new LexicalTokenizer(input.iterator)

      val result: Array[Token] = lexicalTokenizer.toArray

      result should be(expected)
    }

    it("should identify string") {

      val input: String = "myVar := 'test' ; \n myVar2 :=' +teste2+ '+myVar;";

      val expected = Array(
        new Token(TokenType.Identifier, "myVar"),
        new Token(TokenType.AttributionOperator, ":="),
        new Token(TokenType.String, "'test'"),
        new Token(TokenType.SemiColon, ";"),

        new Token(TokenType.Identifier, "myVar2"),
        new Token(TokenType.AttributionOperator, ":="),
        new Token(TokenType.String, "' +teste2+ '"),
        new Token(TokenType.AddOperator, "+"),
        new Token(TokenType.Identifier, "myVar"),
        new Token(TokenType.SemiColon, ";")
      )

      val lexicalTokenizer = new LexicalTokenizer(input.iterator)

      val result: Array[Token] = lexicalTokenizer.toArray

      result should be(expected)
    }

    it("should identify escaped text") {

      val input: String = "myEscapedText := 'espaced '' text';";

      val expected = Array(
        new Token(TokenType.Identifier, "myEscapedText"),
        new Token(TokenType.AttributionOperator, ":="),
        new Token(TokenType.String, "'espaced '' text'"),
        new Token(TokenType.SemiColon, ";")
      )

      val lexicalTokenizer = new LexicalTokenizer(input.iterator)

      val result: Array[Token] = lexicalTokenizer.toArray

      result should be(expected)
    }

    it("should identify symbols") {
      val input: String = "value := 1+calcFunc(123.3, 'stringValue');";

      val expected = Array(
        new Token(TokenType.Identifier, "value"),
        new Token(TokenType.AttributionOperator, ":="),
        new Token(TokenType.NaturalNumber, "1"),
        new Token(TokenType.AddOperator, "+"),
        new Token(TokenType.Identifier, "calcFunc"),
        new Token(TokenType.ParenthesisOpen, "("),
        new Token(TokenType.RealNumber, "123.3"),
        new Token(TokenType.Comma, ","),
        new Token(TokenType.String, "'stringValue'"),
        new Token(TokenType.ParenthesisClose, ")"),
        new Token(TokenType.SemiColon, ";")
      )

      val lexicalTokenizer = new LexicalTokenizer(input.iterator)

      val result: Array[Token] = lexicalTokenizer.toArray

      result should be(expected)
    }
  }
}