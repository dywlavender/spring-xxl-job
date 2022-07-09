package com.xxl.job.executor.service.jobhandler.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @ClassName NBCNTest
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/8 13:52
 * @Version 1.0
 **/
public class NBCNTest {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int nums = Integer.parseInt(in.nextLine());
        List<String> dict = new ArrayList<>();
        List<String> querys = new ArrayList<>();
        Map<String,Integer> result = new HashMap<>();
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < nums; i++) {
            dict.add(in.nextLine());
        }
        int queryNum = Integer.parseInt(in.nextLine());
        for (int i = 0; i < queryNum; i++) {
            String temp = in.nextLine();
            querys.add(temp);
            result.put(temp,0);
        }
        System.out.println("123");
        Collections.sort(dict);
        Collections.sort(querys);
        int j = 0;
        for (int i = 0; i < queryNum; i++) {
            String query = querys.get(i);
            int count = 0;
            while (j < nums && isAnagram(query,dict.get(j))){
                    j++;
                    count++;
            }
            result.put(query,count);
        }
        for (Map.Entry entry: result.entrySet()){
            System.out.println(entry.getValue());
        }
    }
    public static boolean isAnagram(String str1,String str2){
        if (str1==null || str2==null || str1.length() != str2.length() || str1.equals(str2)){
            return false;
        }
        char[] chars1 = str1.toLowerCase().toCharArray();
        char[] chars2 = str2.toLowerCase().toCharArray();
        int sum1 = 0;
        int sum2 = 0;
        for (char c : chars1) {
            sum1 += (int) c;
        }
        for (char c : chars2) {
            sum2 += (int) c;
        }
        return sum1 == sum2;
    }
}
