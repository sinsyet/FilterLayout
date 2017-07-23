package com.example.test;



import com.example.filterlayout.ConditionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by sin on 2017/7/15.
 */

public class Util {


    private static Random random = new Random();

    private static String[] citys = new String[]{
            "北京",
            "上海",
            "深圳",
            "广州",
            "重庆",
            "香港",
            "其他"
    };

    private static String[] sexs = new String[]{
            "女性",
            "男性"
    };


    public static Map<String,List<String>> getBilibiliSearch(){
        HashMap<String, List<String>> map = new HashMap<>();
        String order = "默认排序";
        ArrayList<String> orders = new ArrayList<>();
        orders.add("默认排序");
        orders.add("播放多");
        orders.add("新发布");
        orders.add("弹幕多");

        map.put(order,orders);

        String time = "全部时长";
        ArrayList<String> times = new ArrayList<>();
        times.add("全部时长");
        times.add("1~10分钟");
        times.add("10~30分钟");
        times.add("30~60分钟");
        times.add("60分钟+");
        map.put(time,times);

        String zone = "全部分区";
        ArrayList<String> zones = new ArrayList<>();
        zones.add("全部分区");
        zones.add("番剧");
        zones.add("动画");
        zones.add("国创");
        zones.add("音乐");
        zones.add("舞蹈");
        zones.add("游戏");
        zones.add("科技");
        zones.add("生活");
        zones.add("鬼畜");
        zones.add("时尚");
        zones.add("广告");
        zones.add("娱乐");
        zones.add("电影");
        zones.add("电视剧");

        map.put(zone,zones);

        return map;
    }

    public static LinkedHashMap<List<String>,Integer> getMap(){
        LinkedHashMap<List<String>,Integer> map = new LinkedHashMap<>();
        ArrayList<String> orders = new ArrayList<>();
        orders.add("默认排序");
        orders.add("播放多");
        orders.add("新发布");
        orders.add("弹幕多");

        map.put(orders,4);

        ArrayList<String> times = new ArrayList<>();
        times.add("全部时长");
        times.add("1~10分钟");
        times.add("10~30分钟");
        times.add("30~60分钟");
        times.add("60分钟+");
        map.put(times,5);

        ArrayList<String> zones = new ArrayList<>();
        zones.add("全部分区");
        zones.add("番剧");
        zones.add("动画");
        zones.add("国创");
        zones.add("音乐");
        zones.add("舞蹈");
        zones.add("游戏");
        zones.add("科技");
        zones.add("生活");
        zones.add("鬼畜");
        zones.add("时尚");
        zones.add("广告");
        zones.add("娱乐");
        zones.add("电影");
        zones.add("电视剧");

        map.put(zones,5);

        return map;
    }

    public static Map<String,List<ConditionMenu>> getBilibiliSearchMenu(){
        HashMap<String, List<ConditionMenu>> map = new HashMap<>();
        String order = "默认排序";
        ArrayList<ConditionMenu> orders = new ArrayList<>();
        orders.add(new ConditionMenu("默认排序",false));
        orders.add(new ConditionMenu("播放多",false));
        orders.add(new ConditionMenu("新发布",false));
        orders.add(new ConditionMenu("弹幕多",false));

        map.put(order,orders);

        String time = "全部时长";
        ArrayList<ConditionMenu> times = new ArrayList<>();
        times.add(new ConditionMenu("全部时长",false));
        times.add(new ConditionMenu("1~10分钟",false));
        times.add(new ConditionMenu("10~30分钟",false));
        times.add(new ConditionMenu("30~60分钟",false));
        times.add(new ConditionMenu("60分钟+",false));
        map.put(time,times);

        String zone = "全部分区";
        ArrayList<ConditionMenu> zones = new ArrayList<>();
        zones.add(new ConditionMenu("全部分区",false));
        zones.add(new ConditionMenu("番剧",false));
        zones.add(new ConditionMenu("动画",false));
        zones.add(new ConditionMenu("国创",false));
        zones.add(new ConditionMenu("音乐",false));
        zones.add(new ConditionMenu("舞蹈",false));
        zones.add(new ConditionMenu("游戏",false));
        zones.add(new ConditionMenu("科技",false));
        zones.add(new ConditionMenu("生活",false));
        zones.add(new ConditionMenu("鬼畜",false));
        zones.add(new ConditionMenu("时尚",false));
        zones.add(new ConditionMenu("广告",false));
        zones.add(new ConditionMenu("娱乐",false));
        zones.add(new ConditionMenu("电影",false));
        zones.add(new ConditionMenu("电视剧",false));

        map.put(zone,zones);

        return map;
    }
}
