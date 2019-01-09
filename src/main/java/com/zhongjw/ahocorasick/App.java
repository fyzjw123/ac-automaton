package com.zhongjw.ahocorasick;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongjw
 * @date 2018/10/2
 */
public class App {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(7);
        list.add("a");
        list.add("i");
        list.add("in");
        list.add("tin");
        list.add("sting");
        list.add("bting");
        list.add("ating");

        Trie trie = Trie.newBuilder().addPatternStrings(list).build();
    }
}
