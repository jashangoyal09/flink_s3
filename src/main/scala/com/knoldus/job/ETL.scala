package com.knoldus.job

import com.amazonaws.regions.Regions.US_EAST_1
import com.knoldus.models.AwsS3Configurations
import com.knoldus.s3.S3ServiceImpl
import org.apache.avro.generic.GenericRecord
import org.apache.flink.api.java.hadoop.mapreduce.HadoopInputFormat
import org.apache.flink.api.java.operators.DataSource
import org.apache.flink.api.java.{ExecutionEnvironment, tuple}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.parquet.avro.AvroParquetInputFormat

import scala.collection.JavaConverters._

object ETL extends App {

  val env = ExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(10)

  val awsS3Configurations = AwsS3Configurations(
    "XXXXXXXx",
    "XXXXXXXXXXXXXXXXX",
    US_EAST_1,
    "s3a://",
    "bucketName",
    "prefixName"
  )

  val s3Client = S3ServiceImpl.createAmazonClient(
    awsS3Configurations
  )

  val impl = new S3ServiceImpl(s3Client)

  val s3Objects = impl.getAllObjectfromS3Bucket(awsS3Configurations.bucketName, awsS3Configurations.prefixName)
  val filterParquetFiles = s3Objects.filter(_.getSize > 0).filter(_.getKey.endsWith("parquet.gz"))

  filterParquetFiles.map { s3Object =>

    val url =
      s"${awsS3Configurations.s3BasePathUrl}${awsS3Configurations.bucketName}/${s3Object.getKey}"
    val job = Job.getInstance
    FileInputFormat.addInputPath(
      job,
      new Path(url)
    )

    val hadoopInputFormat =
      new HadoopInputFormat(
        new AvroParquetInputFormat[GenericRecord],
        classOf[Void],
        classOf[GenericRecord],
        job
      )
    val data: DataSource[tuple.Tuple2[Void, GenericRecord]] = env.createInput(hadoopInputFormat)
    val dataList = data.collect().asScala.toList
    dataList.foreach(println)
    dataList.map(_.f1)
  }

  env.execute()
}
