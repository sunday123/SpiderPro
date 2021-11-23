package com.weibo.fans;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * https://m.weibo.cn/api/container/getIndex?containerid=231051_-_fans_-_*************
 * *************
 */

public class GatherTest {
    /**
     * 关注列表
     */
    private static Set<Fan> follow_list = new HashSet<>();
    /**
     * 粉丝列表
     */
    private static Set<Fan> followers_list = new HashSet<>();

    public static void main(String[] args) throws IOException {
        Long uid = 1l;//*************l爬取的ID
        Fan fan = getFan(uid);
        System.out.println(fan);
        System.out.println("关注列表的详细数据:");
        int fans_count = fan.getFollow_count();
        int max_page = fans_count / 20 + 1;
        max_page = max_page > 250 ? 250 : max_page;//由于当page大于250时就已经无法得到内容了
        for (int i = 1; i <= max_page; i++) {
            followers(fan.getId(), i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
        String dateStr = df.format(new Date());
        File file = new File("out/" + fan.getId() + "_关注列表__" + dateStr + ".xlsx");
        if (file.exists()) {
            file.deleteOnExit();
        }
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName().split("__")[0]);
        ExportXls out = new ExportXls();
        out.getExportXls(follow_list, file);


        System.out.println("-----------------------------------------------");
        System.out.println("粉丝的详细数据:");



        fans_count = getFollowersCount(fan.getFollowers_count());
        max_page = fans_count / 20 + 1;
        max_page = max_page > 250 ? 250 : max_page;
        for (int i = 1; i <= max_page; i++) {
            followers_counts(fan.getId(), i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        int fCount = 0;//女
        for (Fan f : follow_list) {
            if ("f".equals(f.getGender())) {
                fCount++;
            }
        }
        System.out.println("关注男+" + (follow_list.size() - fCount) + "+女" + fCount);


        file = new File("out/" + fan.getId() + "_粉丝列表__" + dateStr + ".xlsx");
        if (file.exists()) {
            file.deleteOnExit();
        }
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName().split("__")[0]);
        out.getExportXls(followers_list, file);

        fCount = 0;//女
        for (Fan f : followers_list) {
            if ("f".equals(f.getGender())) {
                fCount++;
            }
        }
        System.out.println("粉丝男+" + (followers_list.size() - fCount) + "+女" + fCount);


        System.out.println("fin");


    }

    private static int getFollowersCount(String countCNStr) {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.CHINA, NumberFormat.Style.SHORT);
        Number count = 0;
        try {
            count = fmt.parse(countCNStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return count.intValue();
    }

    public static Fan getFan(Long uid) throws IOException {
        URL url = new URL("https://m.weibo.cn/profile/info?uid=" + uid);
        URLConnection conn = url.openConnection();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String ua = ua_list()[(int) (ua_list().length * Math.random())];
//		System.out.println("ua:"+ua);
        conn.setRequestProperty("User-agent", ua);//为连接设置ua
        conn.setConnectTimeout(500);
        conn.setReadTimeout(5000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String child = null;
        StringBuilder sb = new StringBuilder();
        while ((child = reader.readLine()) != null) {
            sb.append(child);
        }
        String line = sb.toString();
        reader.close();

//			System.out.println(line);
        Boolean b = JSONUtil.isJsonObj(line);
        if (b) {
            JSONObject obj = JSONUtil.parseObj(line);
            JSONObject data = (JSONObject) obj.get("data");
            JSONObject user = (JSONObject) data.get("user");
            Fan fan = JSONUtil.toBean(user, Fan.class);
            return fan;
        } else {
            return null;
        }


    }

    /**
     * 关注
     */
    public static void followers(Long uid, int index) throws IOException {
        // TODO Auto-generated method stub
        URL url = new URL("https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_" + uid + "&since_id=" + index);
        URLConnection conn = url.openConnection();
        String ua = ua_list()[(int) (ua_list().length * Math.random())];
//			System.out.println("ua:"+ua);
        conn.setRequestProperty("User-agent", ua);//为连接设置ua
        conn.setConnectTimeout(500);
        conn.setReadTimeout(5000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String child = null;
        StringBuilder sb = new StringBuilder();
        while ((child = reader.readLine()) != null) {
            sb.append(child);
        }
        String line = sb.toString();
        reader.close();

        JSONObject obj = JSONUtil.parseObj(line);
        JSONObject data = (JSONObject) obj.get("data");
//				System.out.println(data);
        JSONArray cards = (JSONArray) data.getJSONArray("cards");
//				System.out.println(" cards.size():"+ cards.size());
        for (Object cardO : cards) {
            JSONObject card = (JSONObject) cardO;
            JSONArray card_group = card.getJSONArray("card_group");
//                	System.out.println(card_group.size());
            for (Object card_group0 : card_group) {
                JSONObject card_group0Json = (JSONObject) card_group0;
//                		 System.out.println(card_group0Json.keySet().toString());
                if (card_group0Json.containsKey("user")) {
                    JSONObject user = (JSONObject) card_group0Json.get("user");
                    Long id = user.getLong("id");
                    if (id != null) {
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Fan fan = getFan(id);
                        if (fan != null) {
                            follow_list.add(fan);
                            System.out.println(fan);
                        }
                    }
                }
            }
        }


    }


    /**
     * 粉丝
     */
    public static void followers_counts(Long uid, int index) throws IOException {
        // TODO Auto-generated method stub
        URL url = new URL("https://m.weibo.cn/api/container/getIndex?containerid=231051_-_fans_-_" + uid + "&since_id=" + index);
        URLConnection conn = url.openConnection();
        String ua = ua_list()[(int) (ua_list().length * Math.random())];
//			System.out.println("ua:"+ua);
        conn.setRequestProperty("User-agent", ua);//为连接设置ua
        conn.setConnectTimeout(500);
        conn.setReadTimeout(5000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String child = null;
        StringBuilder sb = new StringBuilder();
        while ((child = reader.readLine()) != null) {
            sb.append(child);
        }
        String line = sb.toString();
        reader.close();
        JSONObject obj = JSONUtil.parseObj(line);
        JSONObject data = (JSONObject) obj.get("data");
//				System.out.println(data);
        JSONArray cards = (JSONArray) data.getJSONArray("cards");
//				System.out.println(" cards.size():"+ cards.size());
        for (Object cardO : cards) {
            JSONObject card = (JSONObject) cardO;
            JSONArray card_group = card.getJSONArray("card_group");
//                	System.out.println(card_group.size());
            for (Object card_group0 : card_group) {
                JSONObject card_group0Json = (JSONObject) card_group0;
//                		 System.out.println(card_group0Json.keySet().toString());
                if (card_group0Json.containsKey("user")) {
                    JSONObject user = (JSONObject) card_group0Json.get("user");
                    Long id = user.getLong("id");
                    if (id != null) {
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Fan fan = getFan(id);
                        if (fan != null) {
                            followers_list.add(fan);
                            System.out.println(fan);
                        }
                    }
                }
            }
        }


    }


    private final static String[] ua_list() {
        // TODO Auto-generated method stub
        return new String[]{
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
                "Opera/8.0 (Windows NT 5.1; U; en)",
                "Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0",
                "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; SE 2.X MetaSr 1.0)",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.3.4000 Chrome/30.0.1599.101 Safari/537.36",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36",
                "Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.27 Safari/525.13",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Mozilla/5.0 (Macintosh; U; IntelMac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1Safari/534.50",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
                "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
                "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24"};
    }
}
