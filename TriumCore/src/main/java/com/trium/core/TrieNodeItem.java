package com.trium.core;

import java.util.HashMap;
import java.util.Map;

public class TrieNodeItem implements TrieNode {
    private final Map<Character, TrieNodeItem> children;
    private boolean endOfString;

    public TrieNodeItem() {
        this.children = new HashMap<>();
        this.endOfString = false;
    }

    @Override
    public Map<Character, TrieNodeItem> getChildren() {
        return children;
    }

    @Override
    public boolean isEndOfString() {
        return endOfString;
    }

    @Override
    public void setEndOfString(boolean endOfString) {
        this.endOfString = endOfString;
    }
}