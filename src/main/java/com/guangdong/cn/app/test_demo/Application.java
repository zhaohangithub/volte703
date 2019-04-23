package com.guangdong.cn.app.test_demo;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import kafka.serializer.Decoder;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.datanucleus.store.rdbms.table.SecondaryTable;
import org.netlib.util.Second;
import scala.Tuple2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf();
        conf.set("", "")
                .set("", "")
                .set("", "");

        SparkContext sparkContext = new SparkContext(conf);
        SparkSession sparkSession = new SparkSession(sparkContext);

        SQLContext sqlContext = new SQLContext(sparkSession);
        RDD<String> stringRDD = sparkContext.textFile("", 1);
        Dataset<Row> json = sqlContext.read().format("json").load("");
        Dataset<Row> json1 = sqlContext.read().json("");
        RDD<Row> rdd = json1.rdd();
        json1.registerTempTable("json1");
        SparkSession sparksession = SparkSession.builder().appName("sparksession").config(conf).enableHiveSupport().getOrCreate();
        sparksession.read();
        SQLContext sqlContext1 = sparksession.sqlContext();
        JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Durations.seconds(5));
        Dataset<Row> load = sparksession.read().load();
        JavaRDD<Row> rowJavaRDD = load.toJavaRDD();

        load.registerTempTable("test");
        sparkSession.sql("");
        Dataset<Row> result = sqlContext.sql("");

        Map<String,String> kafkaParmas = new HashMap<>();
        Set<String> topics = new HashSet<>();

        JavaPairInputDStream<String, String> directStream = KafkaUtils.createDirectStream(streamingContext, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParmas, topics);
        JavaDStream<String> stringJavaDStream = directStream.flatMap(new FlatMapFunction<Tuple2<String, String>, String>() {
            @Override
            public Iterator<String> call(Tuple2<String, String> stringStringTuple2) throws Exception {
                return Arrays.asList(stringStringTuple2._2.split(" ")).iterator();
            }
        });
        JavaPairDStream<String, Integer> stringIntegerJavaPairDStream = stringJavaDStream.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);

            }
        });
        JavaPairDStream<String, Integer> stringIntegerJavaPairDStream1 = stringIntegerJavaPairDStream.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        streamingContext.start();
        streamingContext.awaitTermination();
        streamingContext.stop();


    }
}
