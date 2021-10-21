package MapReduce

import java.lang.Iterable
import java.util.StringTokenizer
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text, WritableComparable, WritableComparator}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import HelperUtils.{CreateLogger, ObtainConfigReference}
import scala.collection.JavaConverters.*

class Task2
object Task2 {


    // Mapper to map each second time frame with ERROR logs
    class Task2Mapper1 extends Mapper[Object, Text, Text, IntWritable]{

      val one = new IntWritable(1)
      val log = new Text()

      override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
        val stringArray = value.toString.split(" ")
        val token = stringArray(2)
        val timeStamp = stringArray(0)
        //splitting the logs into seconds bins
        val time = (stringArray(0).split("\\.")(0))
        if(token.contains("ERROR")) {
          log.set(time)
          context.write(log, one)
        }
      }
    }

   //Aggregating all the equal second bins to sum of ERROR logs
    class Task2Reducer1 extends Reducer[Text, IntWritable, Text, IntWritable] {
     override def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
       val sum = values.asScala.foldLeft(0)(_ + _.get)
       context.write(key, new IntWritable(sum))
     }
   }

     //Mapper to reverse the map from the result of Reducer1 so to make it easy to sort
    class Task2Mapper2 extends Mapper[Object, Text, Text, Text] {

       val timeStamp = new Text()
       val sum = new Text()
       override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, Text]#Context): Unit = {
        timeStamp.set(value.toString.split(",")(0))
        sum.set(value.toString.split(",")(1))
         context.write(sum,timeStamp)
       }
     }

  // writing my own comparator to help in descending order sorting
  class MyComparator extends WritableComparator(classOf[Text], true) {
    override def compare(i: WritableComparable[_], j: WritableComparable[_]): Int = {
      val first = i.toString
      val second = j.toString
      //descending approach
      second.compareTo(first)
    }
  }
     //Reducer to sort the timestamps in descending order of ERROR logs
    class Task2Reducer2 extends Reducer[Text, Text, Text, Text] {
       override def reduce(key: Text, values: Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
         values.asScala.foreach((value) => {
           context.write(value, key)
         })
       }
     }


    //Main method to write pipeline for task2 where first we make sure the first map reduced is completed and then given
    // as input to second Mapreduced class
    def main(args: Array[String]): Unit = {
      val configuration1 = new Configuration
      configuration1.set("mapred.textoutputformat.separator", ",")
      val task1 = Job.getInstance(configuration1, "task2pt1")
      task1.setJarByClass(this.getClass)
      task1.setMapperClass(classOf[Task2Mapper1])
      task1.setCombinerClass(classOf[Task2Reducer1])
      task1.setReducerClass(classOf[Task2Reducer1])
      task1.setOutputKeyClass(classOf[Text])
      task1.setOutputValueClass(classOf[IntWritable])
      FileInputFormat.addInputPath(task1, new Path(args(0)))
      FileOutputFormat.setOutputPath(task1, new Path(args(1)))


      task1.waitForCompletion(true)


      val configuration2 = new Configuration
      configuration2.set("mapred.textoutputformat.separator", ",")
      val task2 = Job.getInstance(configuration2, "task2pt2")
      task2.setJarByClass(this.getClass)
      task2.setMapperClass(classOf[Task2Mapper2])
      task2.setSortComparatorClass(classOf[MyComparator])
      task2.setReducerClass(classOf[Task2Reducer2])
      task2.setOutputKeyClass(classOf[Text])
      task2.setOutputValueClass(classOf[Text])
      FileInputFormat.addInputPath(task2, new Path(args(1)))
      FileOutputFormat.setOutputPath(task2, new Path(args(2)))

      task2.waitForCompletion(true)
      System.exit(if (task2.waitForCompletion(true)) 0 else 1)


    }

  }