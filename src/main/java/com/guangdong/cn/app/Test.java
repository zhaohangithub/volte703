package com.guangdong.cn.app;

import com.guangdong.cn.utils.FileUtils;
import com.guangdong.cn.utils.GlobalConfUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.jruby.RubyProcess;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {
    public static void main(String[] args) {
        String file = "D:\\dataDir\\downLoadDir\\TD-LTE_MRO_HUAWEI_010221240111_30553_20170702110000.xml.gz";
        String file1 = "D:\\dataDir\\downLoadDir\\TD-LTE_MRO_HUAWEI_010221240111_30553_20170702110000.xml";
        long l = System.currentTimeMillis();
        FileUtils.unGZ(file);
        long l1 = System.currentTimeMillis();
        System.out.println(l1-l);
        // FileUtils.delete(file);
        List<Element> elementList1 = FileUtils.readXml(file1);
        System.out.println(elementList1.size());
        long l2 = System.currentTimeMillis();
        System.out.println(l2-l1);
        saxReadXml(file1);
        //System.out.println(elementList.size());
        long l3 = System.currentTimeMillis();
        System.out.println(l3-l2);
    }

    public static void saxReadXml(String file){

        SAXReader saxReader = new SAXReader();
        saxReader.addHandler("/bulkPmMrDataFile/eNB/measurement/object", new ElementHandler() {
            @Override
            public void onEnd(ElementPath elementPath) {
                Element object = elementPath.getCurrent();
                object.detach();
            }

            @Override
            public void onStart(ElementPath elementPath) {

            }
        });
        try {
            saxReader.read(new BufferedInputStream(new FileInputStream(new File(file))));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
