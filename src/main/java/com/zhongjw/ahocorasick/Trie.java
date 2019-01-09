package com.zhongjw.ahocorasick;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Aho–Corasick algorithm implementation
 *
 * @author zhongjw
 * @date 2018/10/3
 */
public class Trie {
    private final Status rootStatus;

    private Trie() {
        this.rootStatus = new Status();
    }

    /**
     * BFS
     *
     * @return this trie
     */
    private Trie buildTrie() {
        final Queue<Status> bfsQueue = new LinkedBlockingDeque<>();

        for (Status status : getRootStatus().getSuccessLinkStatuses()) {
            status.setFailureStatus(getRootStatus());
            bfsQueue.add(status);
        }

        while (!bfsQueue.isEmpty()) {
            Status currentStatus = bfsQueue.remove();

            for (Character character : currentStatus.getSuccessCharacter()) {
                Status nextStatus = currentStatus.nextStatus(character);
                bfsQueue.add(nextStatus);

                Status failedStatus = currentStatus.failed();
                while (failedStatus.nextStatus(character) == null) {
                    failedStatus = failedStatus.failed();
                }

                Status nextFailedStatus = failedStatus.nextStatus(character);
                nextStatus.setFailureStatus(nextFailedStatus);
                nextStatus.addOutputs(nextFailedStatus.output());
            }
        }

        return this;
    }

    /**
     * match text string
     *
     * @param text text string
     * @return true if text match pattern.
     */
    public boolean match(String text) {
        Status nextStatus = getRootStatus();

        for (Character character : text.toCharArray()) {
            nextStatus = getStatus(nextStatus, character);

            Collection<String> outputs = nextStatus.output();
            if (outputs != null && !outputs.isEmpty()) {
                //deal with outputs
                //todo
                return true;
            }
        }

        return false;
    }

    private Status getStatus(Status currentStatus, Character character) {
        Status nextStatus = currentStatus.nextStatus(character);

        //failed
        while (nextStatus == null) {
            currentStatus = currentStatus.failed();
            nextStatus = currentStatus.nextStatus(character);
        }

        return nextStatus;
    }

    public static TrieBuilder newBuilder() {
        return new TrieBuilder();
    }

    public Status getRootStatus() {
        return this.rootStatus;
    }

    public static class TrieBuilder {
        private Trie trie;

        private TrieBuilder() {
            this.trie = new Trie();
        }

        private TrieBuilder addPatternString(String patternString) {
            this.trie.getRootStatus().addStatus(patternString).addOutput(patternString);
            return this;
        }

        public TrieBuilder addPatternStrings(Collection<String> patterStringCollection) {
            for (String patternString : patterStringCollection) {
                addPatternString(patternString);
            }
            return this;
        }

        public Trie build() {
            return this.trie.buildTrie();
        }
    }
}
