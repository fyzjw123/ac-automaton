package com.zhongjw.ahocorasick;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongjw
 * @date 2018/10/9
 */
public class TrieTest {
    @Test
    public void testStatus() {
        List<String> list = new ArrayList<>(7);
        list.add("a");
        list.add("i");
        list.add("in");
        list.add("tin");
        list.add("sting");
        list.add("bting");
        list.add("ating");

        Trie trie = Trie.newBuilder().addPatternStrings(list).build();

        Status rootState = trie.getRootStatus();
        Status nextStatus = rootState.nextStatus('a');
        Assert.assertEquals(true, nextStatus.output().size() == 1);
        Assert.assertEquals(1, nextStatus.getDepth());

        Status i = rootState.nextStatus('i');
        Assert.assertEquals(true, i.output().size() == 1);
        Assert.assertEquals(1, i.getDepth());

        nextStatus = i.nextStatus('n');
        Assert.assertEquals(true, nextStatus.output().size() == 1);
        Assert.assertEquals(1, i.getDepth());

        nextStatus = rootState.nextStatus('t').nextStatus('i').nextStatus('n');
        Status temp = nextStatus;
        Assert.assertEquals(true, nextStatus.output().size() == 2);
        Assert.assertEquals(3, nextStatus.getDepth());

        nextStatus = rootState.nextStatus('t').nextStatus('i');
        Assert.assertEquals(true, nextStatus.output().size() == 1);
        Assert.assertEquals(2, nextStatus.getDepth());

        nextStatus = rootState.nextStatus('s').nextStatus('t').nextStatus('i').nextStatus('n');
        Assert.assertEquals(true, nextStatus.output().size() == 2);
        Assert.assertEquals(4, nextStatus.getDepth());
        Assert.assertEquals(temp, nextStatus.failed());

        nextStatus = rootState.nextStatus('s').nextStatus('t').nextStatus('i').nextStatus('n').nextStatus('g');
        Assert.assertEquals(true, nextStatus.output().size() == 1);
        Assert.assertEquals(5, nextStatus.getDepth());

        nextStatus = rootState.nextStatus('b').nextStatus('t').nextStatus('i').nextStatus('n').nextStatus('g');
        Assert.assertEquals(true, nextStatus.output().size() == 1);
        Assert.assertEquals(5, nextStatus.getDepth());

        nextStatus = rootState.nextStatus('a').nextStatus('t').nextStatus('i').nextStatus('n').nextStatus('g');
        Assert.assertEquals(true, nextStatus.output().size() == 1);
        Assert.assertEquals(5, nextStatus.getDepth());

        nextStatus = rootState.nextStatus('a').nextStatus('t').nextStatus('i').nextStatus('n');
        Assert.assertEquals(true, nextStatus.output().size() == 2);
        Assert.assertEquals(4, nextStatus.getDepth());
    }

    @Test
    public void testMatch() {
        List<String> list = new ArrayList<>(7);
        list.add("a");
        list.add("i");
        list.add("in");
        list.add("tin");
        list.add("sting");
        list.add("bting");
        list.add("ating");

        Trie trie = Trie.newBuilder().addPatternStrings(list).build();

        Assert.assertEquals(true, trie.match("btin"));
        Assert.assertEquals(true, trie.match("bti"));
        Assert.assertEquals(false, trie.match("bt"));

        list.clear();
        list.add("lmsdf");
        list.add("badfsdf");
        list.add("fs");
        list.add("a好");

        trie = Trie.newBuilder().addPatternStrings(list).build();

        Assert.assertEquals(true, trie.match("wsfggasfs"));
        Assert.assertEquals(false, trie.match("wsfggasf"));
        Assert.assertEquals(true, trie.match("wsfggasfa好に"));
        Assert.assertEquals(false, trie.match("wsfggasf好に"));
        Assert.assertEquals(true, trie.match("wslmsdfggasf好に"));

        Status status = trie.getRootStatus().nextStatus('a').nextStatus('好');
        Assert.assertEquals(2, status.getDepth());
        Assert.assertEquals(true, status.output().size() == 1);
    }
}
