package com.trium.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFAutoComplete implements KeywordTrie {
    private final TrieNode root;
    private final Map<String, Map<String, Double>> tfidfScores;

    public TFIDFAutoComplete() {
        this.root = new TrieNodeItem();
        this.tfidfScores = new HashMap<>();
    }

    public TFIDFAutoComplete(Configure configure) {
        this.root = new TrieNodeItem();
        this.tfidfScores = new HashMap<>();

        if (!configure.getKeywords().isEmpty()) {
            for (String s : configure.getKeywords()) {
                insert(s);
            }
            calculateTFIDFScores(configure.getKeywords());
        }
    }

    @Override
    public void insert(String keyword) {
        TrieNode currentNode = root;

        for (char ch : keyword.toCharArray()) {
            TrieNode node = currentNode.getChildren().get(ch);
            if (node == null) {
                node = new TrieNodeItem();
                ((TrieNodeItem) currentNode).getChildren().put(ch, (TrieNodeItem) node);
            }
            currentNode = node;
        }
        currentNode.setEndOfString(true);
    }

    @Override
    public boolean contains(String keyword) {
        TrieNode currentNode = root;
        for (char ch : keyword.toCharArray()) {
            TrieNode node = currentNode.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            currentNode = node;
        }
        return currentNode.isEndOfString();
    }

    @Override
    public void delete(String keyword) {
        if (contains(keyword)) {
            delete(root, keyword, 0);
        }
    }

    @Override
    public Response find(String prefix) {
        List<String> results = new ArrayList<>();
        Map<String, Double> scoreMap = new HashMap<>();
        TrieNode currentNode = root;

        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            TrieNode node = currentNode.getChildren().get(ch);
            if (node == null) {
                return new Response(results, scoreMap);
            }
            currentNode = node;
        }

        collectAllWordsWithPrefix(currentNode, prefix, results, scoreMap);
        results.sort(Comparator.comparingDouble(keyword -> calculateAverageTFIDF(keyword, tfidfScores)));
        Collections.reverse(results); 

        return new Response(results, scoreMap);
    }

    private void collectAllWordsWithPrefix(TrieNode node, String currentPrefix, List<String> results, Map<String, Double> scoreMap) {
        if (node.isEndOfString()) {
            results.add(currentPrefix);
            scoreMap.put(currentPrefix, calculateAverageTFIDF(currentPrefix, tfidfScores));
        }

        for (char ch : node.getChildren().keySet()) {
            TrieNode childNode = node.getChildren().get(ch);
            collectAllWordsWithPrefix(childNode, currentPrefix + ch, results, scoreMap);
        }
    }
    private void collectAllWordsWithPrefix(TrieNode node, String currentPrefix, List<String> results) {
        if (node.isEndOfString()) {
            results.add(currentPrefix);
        }

        for (char ch : node.getChildren().keySet()) {
            TrieNode childNode = node.getChildren().get(ch);
            collectAllWordsWithPrefix(childNode, currentPrefix + ch, results);
        }
    }

    private void calculateTFIDFScores(List<String> documents) {
        for (String document : documents) {
            tfidfScores.put(document, new HashMap<>());
            String[] terms = document.toLowerCase().split("\\s+");
            for (String term : terms) {
                double tf = calculateTermFrequency(term, terms);
                double idf = calculateInverseDocumentFrequency(term, documents);
                tfidfScores.get(document).put(term, tf * idf);
            }
        }
    }

    private double calculateTermFrequency(String term, String[] documentTerms) {
        long termFrequency = Arrays.stream(documentTerms).filter(t -> t.equals(term)).count();
        return (double) termFrequency / documentTerms.length;
    }

    private double calculateInverseDocumentFrequency(String term, List<String> documents) {
        long documentFrequency = documents.stream()
                .filter(d -> Arrays.asList(d.toLowerCase().split("\\s+")).contains(term.toLowerCase())) // Küçük harfle kontrol et
                .count();
        return Math.log((double) documents.size() / (documentFrequency + 1));
    }

    private double calculateAverageTFIDF(String keyword, Map<String, Map<String, Double>> tfidfScores) {
        return tfidfScores.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(keyword.toLowerCase())) // Küçük harfle kontrol et
                .mapToDouble(entry -> entry.getValue().get(keyword.toLowerCase()))
                .average()
                .orElse(0.0);
    }

    private boolean delete(TrieNode parentNode, String keyword, int index) {
        char ch = keyword.charAt(index);
        TrieNode currentNode = parentNode.getChildren().get(ch);

        if (currentNode.getChildren().size() > 1) {
            delete(currentNode, keyword, index + 1);
            return false;
        }

        if (index == keyword.length() - 1) {
            if (currentNode.getChildren().size() >= 1) {
                currentNode.setEndOfString(false);
                return false;
            } else {
                ((TrieNodeItem) parentNode).getChildren().remove(ch);
                return true;
            }
        }

        if (currentNode.isEndOfString()) {
            delete(currentNode, keyword, index + 1);
            return false;
        }

        boolean canDelete = delete(currentNode, keyword, index + 1);
        if (canDelete) {
            ((TrieNodeItem) parentNode).getChildren().remove(ch);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void insertAll(List<String> keywordList) {
        for (String k : keywordList) {
            insert(k);
        }
        calculateTFIDFScores(keywordList);
    }

	@Override
	public BasicResponse findBasic(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}
}
