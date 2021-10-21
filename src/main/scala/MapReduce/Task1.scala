package MapReduce

import java.lang.Iterable
import java.util.StringTokenizer
import HelperUtils.CreateLogger
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import scala.collection.JavaConverters.*


class Task1 
  object Task1 {

    //Mapper for Task1 splits the time into a second bins and making a unique pair of time and token
    class Task1Mapper extends Mapper[Object, Text, Text, IntWritable] {
      val one = new IntWritable(1)
      val log = new Text()

      override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
        val stringArray = value.toString.split(" ")
        val token = stringArray(2)
        val timeStamp = stringArray(0)
        //splitting the logs into seconds bins
        val time = (stringArray(0).split("\\.")(0))
        log.set(token.concat(" ").concat(time))
        context.write(log,one)
      }
    }

    //Reducer for Task1 aggregates the distribution of different messages accross different time intervals
    class Task1Reducer extends Reducer[Text, IntWritable, Text, IntWritable] {
      override def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
        var sum = values.asScala.foldLeft(0)(_ + _.get)
        context.write(key, new IntWritable(sum))
      }
    }

    //Main method for Task1 sets mapper class,reducer class,jars,output key and value class and path to output accordingly
    def main(args: Array[String]): Unit = {
      val configuration = new Configuration
      configuration.set("mapred.textoutputformat.separator", ",")
      val task = Job.getInstance(configuration, "task1")
      task.setJarByClass(this.getClass)
      task.setMapperClass(classOf[Task1Mapper])
      task.setCombinerClass(classOf[Task1Reducer])
      task.setReducerClass(classOf[Task1Reducer])
      task.setOutputKeyClass(classOf[Text])
      task.setOutputValueClass(classOf[IntWritable])
      FileInputFormat.addInputPath(task, new Path(args(0)))
      FileOutputFormat.setOutputPath(task, new Path(args(1)))
      System.exit(if (task.waitForCompletion(true)) 0 else 1)
    }
  }

