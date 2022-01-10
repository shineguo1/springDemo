package gxj.study.leetcode.unknown;

import java.util.HashMap;
import java.util.Map;

public class Trie {
}

class MapSum{

    class TrieNode {
        private Map<Character, TrieNode> links;
        private boolean isEnd;
        private int value;

        TrieNode() {
            this.links = new HashMap<>();
            this.isEnd = false;
            this.value = 0;
        }

        TrieNode(int value) {
            this.links = new HashMap<>();
            this.isEnd = false;
            this.value = value;
        }

        public boolean contains(char c) {
            return links.containsKey(c);
        }

        public TrieNode get(char c) {
            return links.get(c);
        }

        public TrieNode put(char c, TrieNode node) {
            return links.put(c, node);
        }
    }

    private TrieNode root;
    private Map<String,Integer> cache;

    /**
     * Initialize your data structure here.
     */
    public MapSum() {
        root = new TrieNode();
        cache = new HashMap<>();
    }

    /**
     * Inserts a word into the trie.
     */
    public void insert(String key, int val) {
        TrieNode parent = this.root;
        Integer oldValue = cache.get(key);
        cache.put(key,val);
        if(oldValue != null) val = val - oldValue;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (parent.contains(c)) {
                parent = parent.get(c);
            } else {
                TrieNode next = new TrieNode();
                parent.put(c, next);
                parent = next;
            }
            parent.value += val;
        }
    }

    /**
     * Returns if the word is in the trie.
     */
    public int sum(String prefix) {
        TrieNode parent = this.root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            parent = parent.get(c);
            if (parent == null) return 0;
        }
        return parent.value;
    }

    /**
     * Returns if there is any word in the trie that starts with the given prefix.
     */
    public boolean startsWith(String prefix) {

        TrieNode parent = this.root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            parent = parent.get(c);
            if (parent == null) return false;
        }
        return true;
    }
}