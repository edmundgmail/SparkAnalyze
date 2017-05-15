import java.io.InputStream
import java.net.{HttpURLConnection, URL}

import com.cotdp.hadoop.ZipFileInputFormat
import com.ddp.StockQuoteJob
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{BytesWritable, LongWritable, Text}
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.quartz.{Job, JobBuilder, SimpleScheduleBuilder, TriggerBuilder}
import org.quartz.impl.StdSchedulerFactory
import org.quartz.JobDetail
import org.quartz.SchedulerFactory

object SparkAnalyze extends App{

  def getStream = {

    val schedFactory = new StdSchedulerFactory

    val sched = schedFactory.getScheduler

    sched.start

    // define the job and tie it to our HelloJob class
    val job = JobBuilder.newJob(classOf[StockQuoteJob]).withIdentity("myJob", "group1").build

    // Trigger the job to run now, and then every 40 seconds
    val trigger = TriggerBuilder.newTrigger.withIdentity("myTrigger", "group1").startNow.withSchedule(SimpleScheduleBuilder.simpleSchedule.withIntervalInSeconds(30).repeatForever).build

    // Tell quartz to schedule the job using our trigger
    sched.scheduleJob(job, trigger)
  }

  def get(url: String,
          connectTimeout: Int = 5000,
          readTimeout: Int = 5000,
          requestMethod: String = "GET") : InputStream =
  {

    val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    connection.getInputStream
    //val content = scala.io.Source.fromInputStream(inputStream).mkString
    //if (inputStream != null) inputStream.close
    //content
  }


  //getStream
  val sparkConf = new SparkConf().setAppName("SqlNetworkWordCount").setMaster("local[*]")
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  ssc.fileStream()
  ssc.start();
}