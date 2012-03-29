package tests
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Assertions
import org.scalatest.FunSuite
import org.scompiler.reservedWordVerify

@RunWith(classOf[JUnitRunner])
class LexicalTestCase extends FunSuite {
  test("addition") {
    val assertIsReserved = (word: String) => assert(reservedWordVerify.verify(word), word + " Should be reserved")
    val assertIsNotReserved = (word: String) => assert(!reservedWordVerify.verify(word), word + " Should not be reserved")
    
    assertIsReserved("if")
    assertIsReserved("iF")
    assertIsReserved("If")
    assertIsReserved("IF")
    assertIsReserved("and")
    assertIsReserved("+")
    assertIsReserved("nOt")
    assertIsReserved("OR")
    assertIsReserved("case")
        
    assertIsNotReserved("elseif")
    assertIsNotReserved("when")
    assertIsNotReserved("%$!@#")
    assertIsNotReserved("%$!@#")
  }

  test("subtraction") {
    val diff = 4 - 1
    assert(diff === 3)
  }
}