
name := "flink_s3_avro_parquet"

version := "0.1"

scalaVersion := "2.12.10"

val flinkVersion = "1.10.0"
val sdkVersion = "1.7.4"
val clientCore = "3.1.1"
val parquetAvro = "1.8.1"
val hadoopAws = "2.7.2"
val httpVersion = "4.2.5"

libraryDependencies ++= Seq(
"org.apache.parquet" % "parquet-avro" % parquetAvro,
"org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
"org.apache.flink" % "flink-s3-fs-hadoop" % "1.10.0",
"org.apache.flink" %% "flink-hadoop-compatibility" % flinkVersion,
"org.apache.hadoop" % "hadoop-mapreduce-client-core" % clientCore,
"org.apache.flink" % "flink-avro" % flinkVersion,
"com.amazonaws" % "aws-java-sdk" % sdkVersion
)