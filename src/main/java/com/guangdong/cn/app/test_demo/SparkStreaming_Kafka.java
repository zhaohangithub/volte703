package com.guangdong.cn.app.test_demo;

import kafka.serializer.Decoder;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.Decode;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.*;

public class SparkStreaming_Kafka {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf();

        SparkSession ssc = SparkSession.builder().
                appName("sparkstreaming-kafka").
                config(conf).enableHiveSupport().getOrCreate();


        JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Durations.seconds(5));
        Map<String,String> kafkamap = new HashMap<String,String>();
        kafkamap.put("metadata.broker.list","192.168.181.151");
        kafkamap.put("group.id","kafka_Direct");
        Set<String> topic = new HashSet();
        topic.add("mytopic");

        JavaPairInputDStream<String, String> directStream = KafkaUtils.createDirectStream(streamingContext, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkamap, topic);
        JavaDStream<String> words = directStream.flatMap(new FlatMapFunction<Tuple2<String, String>, String>() {

            @Override
            public Iterator<String> call(Tuple2<String, String> stringStringTuple2) throws Exception {
                return Arrays.asList(stringStringTuple2._2.split(" ")).iterator();
            }
        });
        JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });
        JavaPairDStream<String, Integer> wordcount = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
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
