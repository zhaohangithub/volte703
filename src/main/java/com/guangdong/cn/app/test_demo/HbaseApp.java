package com.guangdong.cn.app.test_demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HbaseApp {
    public static void main(String[] args) throws IOException{
        Configuration conf =HBaseConfiguration.create();
        conf.set("hbase.rootdir","hdfs://node1:9000/hbase");
        HTable hTable = new HTable(conf,"user");
        //get
       /* Get get = new Get(String.valueOf(2).getBytes());
        get.addColumn("family".getBytes(),"column".getBytes());
        Result result = hTable.get(get);*/


        //scan
        Scan scan = new Scan();
        scan.addColumn("family".getBytes(),"colume".getBytes());
        SingleColumnValueFilter filter = new SingleColumnValueFilter("family".getBytes(),"column".getBytes(), CompareFilter.CompareOp.EQUAL, Bytes.toBytes("value"));
        scan.setFilter(filter);
        ResultScanner results = hTable.getScanner(scan);
        for (Result result : results
             ) {
            for (Cell cell : result.rawCells()){
                CellUtil.cloneValue(cell);
            }
        }


    }


}
