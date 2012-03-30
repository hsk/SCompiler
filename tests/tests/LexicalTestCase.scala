package tests
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Spec
import org.scalatest.Assertions
import org.scompiler.lexicalValidator
import org.scompiler.reservedWordVerify
import org.scalatest.matchers.ShouldMatchers
import org.scompiler.Token
import org.scompiler.TokenType

@RunWith(classOf[JUnitRunner])
class LexicalTestCase extends Spec with ShouldMatchers {
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

      val result : Array[Token] = lexicalValidator.getTokens(input.iterator).toArray
            
      result should have length (4)
      result should be (expected)
    }
  }
}