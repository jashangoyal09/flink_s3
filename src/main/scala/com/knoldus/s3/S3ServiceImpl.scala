package com.knoldus.s3

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.s3.model.{ListObjectsRequest, ObjectListing, S3ObjectSummary}
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.knoldus.models.AwsS3Configurations

import scala.collection.JavaConverters._

class S3ServiceImpl(client: AmazonS3) extends S3Service {

  def getAllObjectfromS3Bucket(
    bucketName: String,
    prefixName: String
  ): Seq[S3ObjectSummary] = {
    val bucket = bucketName
    val prefix = prefixName
    val listObjectsRequest = new ListObjectsRequest()
      .withBucketName(bucketName)
      .withPrefix(prefixName)
      .withDelimiter("")
    val objectListing: ObjectListing = client.listObjects(listObjectsRequest)
    val data = getObjectsSummaries(objectListing)
    data.foreach(println)
    data
  }

  private def getObjectsSummaries(
    list: ObjectListing,
    res: Seq[S3ObjectSummary] = Seq.empty[S3ObjectSummary]
  ): Seq[S3ObjectSummary] =
    if (list.isTruncated) {
      val newList = this.client.listNextBatchOfObjects(list)
      getObjectsSummaries(newList, res ++ newList.getObjectSummaries.asScala)
    } else {
      res ++ list.getObjectSummaries.asScala
    }
}

object S3ServiceImpl {
  def createAmazonClient(config: AwsS3Configurations): AmazonS3 = {
    val region = config.region
    val awsCredentials = new BasicAWSCredentials(config.accessKeyId, config.secretAccessKey)

    AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
      .withRegion(region)
      .build()
  }

}
