package com.trium.core;

import java.util.Map;

public interface TrieNode {
    Map<Character, ? extends TrieNode> getChildren();
    boolean isEndOfString();
    void setEndOfString(boolean endOfString);
}





