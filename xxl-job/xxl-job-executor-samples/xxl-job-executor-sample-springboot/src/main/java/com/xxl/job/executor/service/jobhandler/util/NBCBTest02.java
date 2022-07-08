package com.xxl.job.executor.service.jobhandler.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName NBCBTest02
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/8 14:20
 * @Version 1.0
 **/
public class NBCBTest02 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String[] input = in.nextLine().split(" ");
        List<String> list = Arrays.asList(input);
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
                }
            }
        );
        System.out.println(String.join(" ", list));
    }
}
