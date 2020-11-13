package gxj.study.leetCode;

import com.microsoft.schemas.office.visio.x2012.main.SectionType;

import java.util.*;

class WordDictionary {
    class Node {
        Character val;
        Map<Character, Node> nexts;

        Node() {
            this.nexts = new HashMap<>();
        }

        Node(Character val) {
            this.val = val;
            this.nexts = new HashMap<>();
        }
    }

    Node head;

    /**
     * Initialize your data structure here.
     */
    public WordDictionary() {
        head = new Node();
        head.nexts.put('.', new Node('.'));
    }

    /**
     * Adds a word into the data structure.
     */
    public void addWord(String word) {
        List<Node> parents = new ArrayList<>();
        Node dot = head;
        parents.add(head);
        for (char c : word.toCharArray()) {
            Node current;
            if (dot.nexts.containsKey(c)) {
                current = dot.nexts.get(c);
            } else {
                current = new Node(c);
                parents.forEach(o -> o.nexts.put(c, current));
            }
            dot.nexts.putIfAbsent('.', new Node('.'));
            dot = dot.nexts.get('.');
            current.nexts.putIfAbsent('.', dot);
            parents.clear();
            parents.add(dot);
            parents.add(current);
        }
    }

    /**
     * Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter.
     */
    public boolean search(String word) {
        Node parent = head;
        for (char c : word.toCharArray()) {
            Node node = parent.nexts.get(c);
            if (node == null) return false;
            parent = node;
        }
        return true;
    }

    public static void main(String[] args) {
        WordDictionary dict = new WordDictionary();
        dict.addWord("a");
        dict.addWord("a");
        System.out.println(dict.search("."));
        System.out.println(dict.search("a"));
        System.out.println(dict.search("aa"));
        System.out.println(dict.search("a"));
        System.out.println(dict.search(".a"));
        System.out.println(dict.search("a."));
    }
}