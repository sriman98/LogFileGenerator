package MapReduce

import Generation.RSGStateMachine.unit
import Generation.RandomStringGenerator
import HelperUtils.LogFileUtils.config
import com.mifmif.common.regex.Generex
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.util.ToolRunner

import scala.io.Source.*

class LogFileGeneratorTest extends AnyFlatSpec with Matchers {

  val minLength = 10
  val maxLength = 50
  val randomSeed = 1
  val lines = fromFile("log/LogFileGenerator.2021-10-20.log").getLines.toString()
  val rsg = RandomStringGenerator((minLength, maxLength), randomSeed)

  behavior of "Configuration Parameters Module"



  //  log level in the sample input file should be same as the one expected
  it should "check file log level" in {
    lines.split(" ")(2) shouldBe INFO
  }

  //check if it is seperated by seperator
  def testConfigurationValues(): Unit ={
    assertEquals(",", config.getString("separator"))
  }


  it should "locate an instance of the pattern in the generated string" in {
    val patternString = "[\\d]+"
    val generex: Generex = new Generex(patternString)
    val genString = generex.random()
    genString should include regex patternString.r
  }

  //randomly generate a string and check if it is less that min length
  it should "generate a random string whose length is lesser than the min length" in {
    val generationStep = init(rsg)
    assert(generationStep._1.length shouldBe < (minLength))
  }

  //randomly generate a string and check if it is greater than the max length
  it should "generate a random string whose length is greater than the max length" in {
    val generationStep = init(rsg)
    assert(generationStep._1.length shouldBe > (maxLength))
  }


}