package tests
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scompiler.LexicalValidator
import org.scompiler.reservedWordVerify
import org.scalatest.matchers.ShouldMatchers
import org.scompiler.Token
import org.scompiler.TokenType

@RunWith(classOf[JUnitRunner])
class LexicalTestCase extends FunSpec with ShouldMatchers {
  val assertWordIsReserved = (word: String) => assert(reservedWordVerify.verify(word), word + " Should be reserved")
  val assertWordIsNotReserved = (word: String) => assert(!reservedWordVerify.verify(word), word + " Should not be reserved")

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
      val input : String = "1234.123 123 -123 1.0E-1"
      val expected : Array[Token] = input.split(" ").map(num => new Token(TokenType.Number, num))
      val lexicalValidator = new LexicalValidator()

      val result : Array[Token] = lexicalValidator.getTokens(input.iterator).toArray
            
      result should have length (4)
      result should be (expected)
    }

    it("should identify correct tokens between wrong tokens") {
      val input : String = "123WWW 213 1.23"
      val expected = Array(
        new Token(TokenType.Undefined, "123"),
        new Token(TokenType.Identifier, "WWW"),
        new Token(TokenType.Number, "213"),
        new Token(TokenType.Number, "1.23")
      )

      val lexicalValidator = new LexicalValidator()

      val result : Array[Token] = lexicalValidator.getTokens(input.iterator).toArray

      result should have length (4)
      result should be (expected)
    }

    it("should identify multiple statements") {
      val input : String = "ID1 := 1234;\nid2:=5.1E10-ID1";

      val expected = Array(
        new Token(TokenType.Identifier, "ID1"),
        new Token(TokenType.Symbol, ":="),
        new Token(TokenType.Number, "1234"),
        new Token(TokenType.Symbol, ";"),

        new Token(TokenType.Identifier, "id2"),
        new Token(TokenType.Symbol, ":="),
        new Token(TokenType.Number, "5.1E10"),
        new Token(TokenType.Symbol, "-"),
        new Token(TokenType.Identifier, "ID1")
      )

      val lexicalValidator = new LexicalValidator()

      val result : Array[Token] = lexicalValidator.getTokens(input.iterator).toArray

      result should be (expected)
    }
  }
}