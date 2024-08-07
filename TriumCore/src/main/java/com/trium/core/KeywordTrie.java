package com.trium.core;

import java.util.List;

public interface KeywordTrie {
    void insert(String keyword);
    boolean contains(String keyword);
    void delete(String keyword);
    BasicResponse findBasic(String prefix);
    Response find(String prefix);
    void insertAll(List<String> keywordList);
}