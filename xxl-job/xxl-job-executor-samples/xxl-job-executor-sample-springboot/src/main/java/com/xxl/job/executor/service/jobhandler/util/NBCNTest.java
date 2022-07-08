package com.xxl.job.executor.service.jobhandler.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < nums; i++) {
            dict.add(in.nextLine());
        }
        int queryNum = Integer.parseInt(in.nextLine());
        for (int i = 0; i < queryNum; i++) {
            int count = 0;
            String query = in.nextLine();
            for (String world:dict) {
               if (isAnagram(query,world)){
                count++;
               }
            }
            ans.add(count);
        }
        ans.forEach(System.out::println);
    }
    public static boolean isAnagram(String str1,String str2){
        if (str1==null || str2==null || str1.length() != str2.length() || str1.equals(str2)){
            return false;
        }
        char[] chars1 = str1.toLowerCase().toCharArray();
        char[] chars2 = str2.toLowerCase().toCharArray();
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i < chars1.length; i++) {
            sum1 += (int) chars1[i];
        }
        for (int i = 0; i < chars2.length; i++) {
            sum2 += (int) chars1[i];
        }
        if (sum1==sum2){
            return true;
        }
        return false;
    }
}
