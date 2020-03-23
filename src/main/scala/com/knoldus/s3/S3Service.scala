package com.knoldus.s3

import com.amazonaws.services.s3.model.S3ObjectSummary

trait S3Service {
  def getAllObjectfromS3Bucket(
    bucketName: String,
    prefixName: String
  ): Seq[S3ObjectSummary]
}
