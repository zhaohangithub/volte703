package com.guangdong.cn.utils;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.dom4j.Element;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 数据过滤工具类
 */
public class FilterUtils {

    /**
     * ftp服务器文件过滤
     * 获取要下载的文件队列
     * @param ftpClientPool ftp连接池
     * @param ftpQueue  ftp文件名队列
     * @param timestamp 过滤时间
     */
    public static void ftpFilter(GenericObjectPool<FTPClient> ftpClientPool,
                                 LinkedBlockingQueue<String> ftpQueue,
                                 String timestamp) {
        try {
            FTPClient ftp = ftpClientPool.borrowObject();
            List<String> list = FtpUtils.list(GlobalConfUtils.FtpPath, ftp);;
            ftpClientPool.returnObject(ftp);
            for (String file : list){
                if (file.contains("TD-LTE_MRO") || file.contains("TD-LTE_MRE")) {
                    if (file.contains("MRO")){//过滤条件,时间字符串
                        ftpQueue.put(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对解压后的文件名称队列进行过滤,根据给定的cellid集合
     * @param xmlQueue 原队列
     * @param cellIds 条件集合
     * @return 一个新的过滤后的队列,保存了文件名称(以及cellid),用于xml文件内过滤
     */
    public static LinkedBlockingQueue<String> fileFilter(LinkedBlockingQueue<String> xmlQueue,
                                                         List<String> cellIds) {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();//返回
        Iterator<String> iterator = xmlQueue.iterator();//输入数据
        MultiValueMap map = new MultiValueMap();//映射
        MultiValueMap maps = new MultiValueMap();//中间存储
        //将eNBid和file映射
        while (iterator.hasNext()){
            String file = iterator.next();
            String[] split = file.split("_");
            long eNBid = Long.valueOf(split[split.length-2]);
            map.put(eNBid,file);
        }
        //数据存入中间map
        for (String cellId : cellIds){
            long enb = Long.parseLong(cellId) / 256;
            Collection collection = map.getCollection(enb);
            if (collection == null){
                continue;
            }
            for (Object file : collection){
                maps.put(file,cellId);
            }
        }
        //将每个文件名及其对应的cellid存入queue队列
        Set keySet = maps.keySet();
        for (Object key : keySet) {
            StringBuffer sb = new StringBuffer();
            sb.append(key.toString()+",");
            Collection collection = maps.getCollection(key);
            for (Object cellid : collection){
                sb.append(cellid.toString()+",");
            }
            try {
                queue.put(sb.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue;
    }

    /**
     * 根据解析出来的list,去完成具体字段的筛选和计算
     * @param filenameAndCellIds 文件名和cellid
     * xml文件名//作为判断文件的类型(MRO/MRE)
     * cellId//作为过滤条件
     * @param elementList 处理数据的集合
     * @param list 处理数据需要的字段集合
     * @return 返回一个map,一个key对应多个v
     */
    public static MultiValueMap fieldFilter(String filenameAndCellIds,
                                           List<Element> elementList,
                                           List<String> list){
        if (elementList == null || elementList.size() == 0){//如果数据集为空,返回
            return null;
        }
        MultiValueMap map = new MultiValueMap();
        String[] split2 = filenameAndCellIds.split(",");
        for (int a = 0;a < elementList.size();a++) {
            Element element1 = elementList.get(a);
            String[] smrs = element1.element("smr").getText().split(" ");

            //字段名称和索引做映射
            Map<String,Integer> indexMap = new HashMap<>();
            for (String v : list) {
                for (int k = 0; k < smrs.length; k++) {
                    if (smrs[k].equals(v)) {
                        indexMap.put(v,k);
                    }
                }
            }
            //object元素为null时直接返回
            List<Element> objectlist = element1.elements("object");
            if (objectlist.size() == 0 || objectlist == null) {
                return null;
            }
            for (Element element : objectlist) {
                String id = element.attributeValue("id");
                for (int b =1; b <split2.length; b++){
                    if (id.equals(split2[b])){//cellid过滤
                        StringBuffer att = new StringBuffer();
                        StringBuffer value = new StringBuffer();

                        //获取属性值
                        for (String name : list){
                            if (element.attribute(name)==null){
                                continue;
                            }
                            String value1 = element.attribute(name).getValue();
                            if (name.equals("TimeStamp")){
                                value1 = String.valueOf(DateUtils.string2Long(value1));
                            }
                            att.append(value1 + ",");
                        }
                        //获取同区数据
                        List<Element> vlist = element.elements("v");
                        Element element3 = vlist.get(0);//获取第一条数据即可
                        String[] split = element3.getText().split(" ");
                        //同区值拼接
                        for (int i = 0; i < list.size()-7; i++){//后面7个数据是取最大值用的
                            Integer index = indexMap.get(list.get(i));
                            if (index == null){
                                continue;
                            }
                            String v = split[index];
                            value.append(v + ",");
                        }

                        //获取最值
                        if (a == 0){
                            Map<Integer,String> map1 = new HashMap();
                            Map<Integer,String> map2 = new HashMap();
                            Map<Integer,String> map3 = new HashMap();
                            Integer index_sc = indexMap.get("MR.LteScEarfcn");//同频频点
                            Integer index_nc = indexMap.get("MR.LteNcEarfcn");//异频频点
                            Integer index_rsrp = indexMap.get("MR.LteNcRSRP");//同频or异频最大的rsrp
                            Integer index_pci = indexMap.get("MR.LteNcPci");//同频or异频最大的rsrp对应的pci
                            Integer index_rssi = indexMap.get("MR.GsmNcellCarrierRSSI");//最大的GSM的电平值
                            Integer index3_bcch = indexMap.get("MR.GsmNcellBcch");//最大的GSM的电平小区对应的Bcch
                            Integer index3_ncc = indexMap.get("MR.GsmNcellNcc");//最大的GSM的电平小区对应的Ncc
                            Integer index3_bcc = indexMap.get("MR.GsmNcellBcc");//最大的GSM的电平小区对应的Bcc
                            for (Element velement : vlist){
                                String[] split1 = velement.getText().split(" ");

                                if (index_sc == null || index_nc == null){
                                    break;
                                }
                                String sc = split1[index_sc];
                                String nc = split1[index_nc];
                                String rsrp = split1[index_rsrp];
                                String pci = split1[index_pci];
                                String rssi = split1[index_rssi];
                                String bcch = split1[index3_bcch];
                                String ncc = split1[index3_ncc];
                                String bcc = split1[index3_bcc];
                                if (!rsrp.equals("NIL")){
                                    if (!sc.equals("NIL") || !nc.equals("NIL")){
                                        if (sc.equals(nc)){//同频组
                                            map1.put(Integer.parseInt(rsrp), rsrp + "," + pci + ",");
                                        }else {//异频组
                                            map2.put(Integer.parseInt(rsrp), rsrp + "," + pci + "," + nc + ",");
                                        }
                                    }
                                    if (!rssi.equals("NIL"))
                                        map3.put(Integer.parseInt(rssi),rssi+","+bcch+","+ncc+","+bcc+",");
                                }
                            }
                            //同频rsrp最大值,以及对应pci
                            if (map1.size() == 0){
                                value.append("NIL,NIL,");
                            }else {
                                Comparable key1 = Collections.max(map1.keySet());
                                value.append(map1.get(key1));
                            }
                            //异频rsrp最大值,以及对应pci,频点
                            if (map2.size() == 0){
                                value.append("NIL,NIL,NIL,");
                            }else {
                                Comparable key2= Collections.max(map2.keySet());
                                value.append(map2.get(key2));
                            }
                            //最大GSM电平值,以及对应的bcch,ncc,bcc
                            if (map3.size() == 0){
                                value.append("NIL,NIL,NIL,NIL,");
                            }else {
                                Comparable key3 = Collections.max(map3.keySet());
                                value.append(map3.get(key3));
                            }
                        }
                        map.put(att.toString(),value.toString());
                    }
                }
            }
        }
        return map;
    }
}
