package com.guangdong.cn.utils;

import com.guangdong.cn.pojo.MmeBean;
import com.guangdong.cn.utils.DateUtils;
import com.guangdong.cn.utils.GlobalConfUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 数据过滤工具类
 * 1.id值过滤
 * 2.复合条件过滤
 * 3.指定字段最大值过滤
 */
public class FilterUtils {
    /**
     * 根据id过滤数据(for循环嵌套,效率低)
     * @param elementList
     */
    public static List<Element> idFilter(List<Element> elementList,List<String> idList){

        List<Element> idElements = new ArrayList<>();
        for (Element element : elementList){
            String mmeUeS1apId = element.attribute(GlobalConfUtils.MmeUeS1apId).getValue();
            for (String id :idList) {
                if (mmeUeS1apId.equals(id)) {
                    idElements.add(element);
                }
            }
        }
        return idElements;

    }

    /**
     * 根据id过滤数据(高效)
     * 此方法将list集合中的数据放入map集合中,此集合可以存放重复的value,
     * 即value是collection集合,key为过滤的id
     * @param elementList  mro/mre数据集
     */
    public static List<Element> idFilter2(List<Element> elementList,List<String> idList){
        //可以存放重复值的map<MmeUeS1apId,List<Element>>
        MultiValueMap elementMap = new MultiValueMap();
        //返回的list
        List<Element> idElements = new ArrayList<>();

        for (Element element : elementList){
            String mmeUeS1apId = element.attribute(GlobalConfUtils.MmeUeS1apId).getValue();
            elementMap.put(mmeUeS1apId,element);
        }

        for (String MmeUeS1apId : idList ){
            Collection collection = elementMap.getCollection(MmeUeS1apId);
            if (collection != null)
            idElements.addAll(collection);
        }

        //返回list,继续操作
        return idElements;

    }

    /**
     * 在id过滤的基础上进行深度过滤(usid,cellid,时间窗口)
     * 三个条件必须同时满足
     * 此方法处理的数据集应该在上述方法处理过后,数据量极小
     * 此方法执行后得到的结果集应该是满足三条件的object
     * 该方法执行后要进行v元素的过滤,即指定字段最大值过滤
     * @param elementList mro/mre数据集
     * @param beanList  mme数据集
     */
    public static List<Element> filter(List<Element> elementList, List<MmeBean> beanList) {
        List<Element> idElements = new ArrayList<>();
        //mre 数据解析
        for (Element element : elementList){
            String mmeUeS1apId = element.attribute(GlobalConfUtils.MmeUeS1apId).getValue();
            String cellId = element.attribute(GlobalConfUtils.CellId).getValue();
            String timeStamp = element.attribute(GlobalConfUtils.TimeStamp).getValue();
            //时间转时间戳
            long mreTimestamp = DateUtils.date2timestamp(timeStamp);

            //mme数据解析
            for (MmeBean bean : beanList){
                //关联3条件
                String cell_id = bean.getCell_Id();
                String mme_ue_s1AP_id = bean.getMME_UE_S1AP_ID();
                String end_time = bean.getEnd_Time();
                //转时间戳
                long mmeTimestamp = DateUtils.date2timestamp(end_time);
                long minTime = mmeTimestamp-20000;
                long maxTime = mmeTimestamp+20000;
                if (mme_ue_s1AP_id.equals(mmeUeS1apId) && cell_id.equals(cellId) && mreTimestamp <= maxTime && mreTimestamp>= minTime){
                    idElements.add(element);
                }
            }
        }
        return idElements;

    }

    /**
     * 指定字段最大值过滤,此过滤是object过滤的基础上,进行v值过滤
     * 获取pci字段最大值
     * 该方法是根据某个确定的索引去比较,获取该索引位置的最大值.并返回该条信息
     * 该方法要在所有过滤条件的最后执行,因为会解析xml文件,形成自己的格式存储
     * 最后的结果,存入set集合,该集合线程安全,并且对全线程共享
     * @param elementList mro数据集
     * @return
     */
    public static void maxValueFilter(List<Element> elementList, ConcurrentSkipListSet set){
        //节点-----------"v"
        String valueElement = GlobalConfUtils.valueElement;
        //index==8-----------该索引处存储的值即为pci字段
        int index1 = GlobalConfUtils.Index;
        //list集合控制台输出
        //List<String> maxList = new ArrayList<>();
        //mre数据解析
        for (Element element : elementList){
            //属性值
            List<Attribute> attributes = element.attributes();
            //节点值------"v"
            List<Element> vlist = element.elements(valueElement);

            //1.每条数据存入String字符串中展示
            StringBuffer allvalue = new StringBuffer();

            for (Attribute att : attributes) {
                allvalue.append(att.getValue() + " ");
            }
            //根据条件筛选最大的v
            int max = 0;
            int index = -1;
            if (vlist.size() >= 2){
                for (int i =0 ; i<vlist.size();i++){
                    String pci = Arrays.asList(vlist.get(i).getText().split(" ")).get(index1);
                    if (!pci.equals("NIL")){
                        int pciInt = Integer.parseInt(pci);
                        if (pciInt > max){
                            max = pciInt;
                            index = i;
                        }
                    }else {
                        index = 0;
                    }
                }
            }else {
                index = 0;
            }
            //追加v的值
            allvalue.append(vlist.get(index).getText());
            //存入set集合
            set.add(allvalue.toString());
            //maxList.add(allvalue.toString());

        }
       /* //实际存入set集合
        set.addAll(maxList);
        //只在console展示,
        return maxList;*/

    }

    /**
     * element结果展示
     * @param elementList  mro数据集
     */
    public static List<String> element2String(List<Element> elementList) {
        //element值 ------------- "v"
        String valueElement = GlobalConfUtils.valueElement;
        List<String> list = new ArrayList<>();
        for (Element element : elementList) {

            List<Attribute> attributes = element.attributes();

            List<Element> vlist = element.elements(valueElement);
            //console输出
            for (Element v : vlist) {
                StringBuffer value = new StringBuffer();
                for (Attribute att : attributes) {
                    value.append(att.getValue() + " ");
                }
                String valus = v.getText();
                value.append(valus);
                list.add(value.toString());

            }
        }
        return list;

    }

}
