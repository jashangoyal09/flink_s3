package com.knoldus.models

import com.amazonaws.regions.Regions

final case class AwsS3Configurations(
  accessKeyId: String,
  secretAccessKey: String,
  region: Regions,
  s3BasePathUrl: String,
  bucketName: String,
  prefixName:String
)
