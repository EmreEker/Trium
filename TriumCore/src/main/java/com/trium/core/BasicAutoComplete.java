package com.trium.core;

import java.util.ArrayList;
import java.util.List;

public class BasicAutoComplete implements KeywordTrie {
    private final TrieNode root;

    public BasicAutoComplete() {
        this.root = new TrieNodeItem();	
    }

    public BasicAutoComplete(Configure configure) {
        this.root = new TrieNodeItem();	
        if (!configure.getKeywords().isEmpty()) {
			for (String s : configure.getKeywords())
				insert(s);
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
	public BasicResponse findBasic(String prefix) {
		List<String> results = new ArrayList<>();
		TrieNode currentNode = root;
		for (int i = 0; i < prefix.length(); i++) {
			char ch = prefix.charAt(i);
			TrieNode node = currentNode.getChildren().get(ch);
			if (node == null) {
				return new BasicResponse(results);
			}
			currentNode = node;
		}
		collectAllWordsWithPrefix(currentNode, prefix, results);
		return new BasicResponse(results);

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
		for(String k:keywordList)
			insert(k);
	}

	@Override
	public Response find(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}
}