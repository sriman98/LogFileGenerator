package MapReduce


import java.lang.Iterable
import java.util.StringTokenizer
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import scala.collection.JavaConverters.*

class Task4
object Task4{

  //Mapper for Task4 sets the log value to the string size with the input message mapped by message type
  class Task4Mapper extends Mapper[Object, Text, Text, IntWritable] {
    val log = new Text()
    override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
      val stringArray = value.toString.split(" ")
      val token = stringArray(2)

      val matchString = stringArray.slice(5,stringArray.size).mkString("")
      log.set(token)
      val length = matchString.length
      context.write(log, new IntWritable(length))
    }
  }

  //Reducer for Task4 aggregates the log lengths by combining by message type getting the max of each type
  class Task4Reducer extends Reducer[Text,IntWritable,Text,IntWritable] {
    override def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
      var maximumValue = values.asScala.foldLeft(0)(_ max _.get)
      context.write(key, new IntWritable(maximumValue))
    }
  }

  //Main method for Task4 sets mapper class,reducer class,jars,output key and value class and path to output accordingly
  def main(args: Array[String]): Unit = {
    val configuration = new Configuration
    configuration.set("mapred.textoutputformat.separator", ",")
    val task = Job.getInstance(configuration,"task4")
    task.setJarByClass(this.getClass)
    task.setMapperClass(classOf[Task4Mapper])
    task.setCombinerClass(classOf[Task4Reducer])
    task.setReducerClass(classOf[Task4Reducer])
    task.setOutputKeyClass(classOf[Text])
    task.setOutputValueClass(classOf[IntWritable])
    FileInputFormat.addInputPath(task, new Path(args(0)))
    FileOutputFormat.setOutputPath(task, new Path(args(1)))
    System.exit(if (task.waitForCompletion(true)) 0 else 1)
  }


}
