package com.ddp

import java.io.InputStream

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import java.net.{HttpURLConnection, URL}
/**
  * Created by eguo on 5/14/17.
  */
class StockQuoteJob extends Job{

  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
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

  @throws[JobExecutionException]
  def execute(context: JobExecutionContext): Unit = {
    try {
      val inputStream = get("http://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=1min&apikey=7R71")


      //println(content)
    } catch {
      case ioe: java.io.IOException =>  // handle this
      case ste: java.net.SocketTimeoutException => // handle this
    }
  }
}
