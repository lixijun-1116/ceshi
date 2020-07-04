package com.kgc.wxdemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxdemoApplicationTests {


        private static Object object = new Object();
        public static void main(String[] args) throws InterruptedException {
            new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i < 50; i++) {
                            for (int j = 0; j < 10; j++) {
                                System.out.println("线程====== " + (j+1));
                            }
                        }

                }
            }).start();

    }
    @Test
    public void test() throws UnsupportedEncodingException {
        //将字符转换成byte数组
        String  str = "abcdefgabcdefg";
        String str2 = str.replaceAll("cd","");
        System.out.println(str2);
        byte[] sb = str.getBytes("UTF-8");
        for (byte b : sb) {
            System.out.println(b);
        }

        //将byte数组换成字符转
        String str1 = new String(sb,"UTF-8");
        System.out.println(str1);


    }

    @Test
    public void test01(){
        final int a = 5;
        System.out.println("a:"+a);
        final Map<String,String> map =new HashMap<>();
        map.put("jj","55");
        System.out.println("jj:"+map.get("jj"));
        map.put("jj","88");
        System.out.println("jj:"+map.get("jj"));


    }
}
