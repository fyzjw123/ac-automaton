package com.zhongjw.ahocorasick;

import java.util.*;

/**
 * Aho–Corasick algorithm implementation
 * <p>
 * Automaton's status representation. Status has three functions.
 * <ul>
 * <li>success: move to another status through the trie, add this character to status's string representation</li>
 * <li>failure: move to another status through the suffix link</li>
 * <li>output: hit some pattern strings and output this pattern</li>
 * </ul>
 *
 * @author zhongjw
 * @date 2018/10/2
 */
public class Status {

    /**
     * new status when failed.
     */
    private Status failure;

    /**
     * depth of this status
     */
    private int depth;

    /**
     * goto map when success.
     */
    private Map<Character, Status> successGotoMap;

    /**
     * output link
     */
    private Set<String> outputSet;

    public Status() {
        this(0);
    }

    public Status(int depth) {
        this.depth = depth;
        this.successGotoMap = new HashMap<>(32);
        this.outputSet = new HashSet<>(32);
    }

    public int getDepth() {
        return this.depth;
    }

    public Status nextStatus(Character character) {
        Status nextStatus = this.successGotoMap.get(character);

        //return to rootStatus
        if (this.depth == 0 && nextStatus == null) {
            nextStatus = this;
        }

        return nextStatus;
    }

    public Status addStatus(String patternString) {
        Status nextStatus = this;
        for (Character character : patternString.toCharArray()) {
            nextStatus = nextStatus.addStatus(character);
        }
        return nextStatus;
    }

    public Status addStatus(Character character) {
        Status nextStatus = this.successGotoMap.get(character);
        if (nextStatus == null) {
            nextStatus = new Status(this.depth + 1);
            this.successGotoMap.put(character, nextStatus);
        }

        return nextStatus;
    }

    public void addOutput(String patternString) {
        this.outputSet.add(patternString);
    }

    public void addOutputs(Collection<String> patternStrings) {
        for (String pattern : patternStrings) {
            addOutput(pattern);
        }
    }

    public Collection<String> output() {
        return this.outputSet == null ? Collections.<String>emptySet() : this.outputSet;
    }

    public void setFailureStatus(Status failureStatus) {
        this.failure = failureStatus;
    }

    public Status failed() {
        return this.failure;
    }

    public Collection<Character> getSuccessCharacter() {
        return this.successGotoMap.keySet();
    }

    public Collection<Status> getSuccessLinkStatuses() {
        return this.successGotoMap.values();
    }
}
