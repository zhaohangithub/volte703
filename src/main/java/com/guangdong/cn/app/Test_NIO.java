package com.guangdong.cn.app;

import com.guangdong.cn.utils.GlobalConfUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class Test_NIO {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        saxReadXml("D:\\dataDir\\downLoadDir\\TD-LTE_MRO_HUAWEI_010221240111_30553_20170702110000.xml");
        long l1 = System.currentTimeMillis();
        System.out.println(l1 - l);
        saxReadXml1("D:\\dataDir\\downLoadDir\\TD-LTE_MRO_HUAWEI_010221240111_30553_20170702110000.xml");
        long l2 = System.currentTimeMillis();
        System.out.println(l2-l1);

    }
    public static List<Element> saxReadXml(String file){
        List<Element> elementList = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        saxReader.addHandler(GlobalConfUtils.SAX_Nodes, new ElementHandler() {
            @Override
            public void onEnd(ElementPath elementPath) {
                Element object = elementPath.getCurrent();
                elementList.add(object);
                object.detach();
            }

            @Override
            public void onStart(ElementPath elementPath) {

            }
        });
        try {
            FileInputStream fis = new FileInputStream(new File(file));
            FileChannel fc = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 10);

            fc.read(buffer);

            buffer.flip();

            saxReader.read(fis);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return elementList;
    }

    public static List<Element> saxReadXml1(String file){
        List<Element> elementList = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        saxReader.addHandler(GlobalConfUtils.SAX_Nodes, new ElementHandler() {
            @Override
            public void onEnd(ElementPath elementPath) {
                Element object = elementPath.getCurrent();
                elementList.add(object);
                object.detach();
            }

            @Override
            public void onStart(ElementPath elementPath) {

            }
        });
        try {
            FileInputStream fis = new FileInputStream(new File(file));
            saxReader.read(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elementList;
    }
}
