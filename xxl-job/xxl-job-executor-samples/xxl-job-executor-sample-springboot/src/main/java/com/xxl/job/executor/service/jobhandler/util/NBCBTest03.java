package com.xxl.job.executor.service.jobhandler.util;

import java.util.*;
import java.util.stream.Stream;

/**
 * @ClassName NBCBTest03
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/8 14:57
 * @Version 1.0
 **/
public class NBCBTest03 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int nums = Integer.parseInt(in.nextLine());
        Set set = new HashSet();
        for (int i = 0; i < nums; i++) {
            set.add(Integer.parseInt(in.nextLine()));
        }
        Stream sorted = set.stream().sorted();
        sorted.forEach(System.out::println);
    }
}
