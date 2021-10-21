package MapReduce

import java.lang.Iterable
import java.util.StringTokenizer
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}

import scala.collection.JavaConverters.*
import HelperUtils.{CreateLogger, ObtainConfigReference}


class Task3
object Task3{

  //Mapper for Task3 sets the log count to 1 with the input message mapped by message type
  class Task3Mapper extends Mapper[Object, Text, Text, IntWritable]{
    val one = new IntWritable(1)
    val log = new Text()
    override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
      val stringArray = value.toString.split(" ")
      val token = stringArray(2)
      val matchString = stringArray(5)
      log.set(token)
      context.write(log, one)
      }
  }

  //Reducer for Task3 aggregates the log count by combining by message type
  class Task3Reducer extends Reducer[Text,IntWritable,Text,IntWritable] {
    override def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
      var total = values.asScala.foldLeft(0)(_ + _.get)
      context.write(key, new IntWritable(total))
    }
  }

//Main method for Task3 sets mapper class,reducer class,jars,output key and value class and path to output accordingly
  def main(args: Array[String]): Unit = {
    val configuration = new Configuration
    configuration.set("mapred.textoutputformat.separator", ",")
    val task = Job.getInstance(configuration,"task3")
    task.setJarByClass(this.getClass)
    task.setMapperClass(classOf[Task3Mapper])
    task.setCombinerClass(classOf[Task3Reducer])
    task.setReducerClass(classOf[Task3Reducer])
    task.setOutputKeyClass(classOf[Text])
    task.setOutputValueClass(classOf[IntWritable])
    FileInputFormat.addInputPath(task, new Path(args(0)))
    FileOutputFormat.setOutputPath(task, new Path(args(1)))
    System.exit(if (task.waitForCompletion(true)) 0 else 1)
  }


}
