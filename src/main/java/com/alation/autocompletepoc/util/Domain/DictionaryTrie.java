package com.alation.autocompletepoc.util.Domain;

import com.alation.autocompletepoc.util.PairGenerator;
import com.alation.autocompletepoc.util.PropertyUtil;
import com.alation.autocompletepoc.util.SerializeUtil;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class DictionaryTrie {

    private TrieNode root;

    private Map<String, List<String>> cache;

    private String INPUT_FILE_NAME = PropertyUtil.getProperty("input_file_path_name");

    private String TRIE_FILE_NAME = PropertyUtil.getProperty("trie_file_path_name");

    private String pairAmount = PropertyUtil.getProperty("pairAmount");

    private String maxStringLength = PropertyUtil.getProperty("maxStringLength");

    //Exposed API
    public List<String> search(String prefix){
        if(prefix.equals("")){
            return new ArrayList<>();
        }
        if(cache.containsKey(prefix)){
            //System.out.println("using cache");
            return cache.get(prefix);
        }

        TrieNode curr = root;
        //follow the path to prefix first
        for(char c: prefix.toCharArray()){
            if(!curr.children.containsKey(c)){
                return new ArrayList<>();
            } else {
                curr = curr.children.get(c);
            }
        }
        //System.out.println("to find topk");
        List<String> topTenList = findTopK(curr, 10);
        cache.put(prefix, topTenList);
        return topTenList;
    }

    private List<String> findTopK(TrieNode curr, int k){
        Queue<Pair> resultpq = new PriorityQueue<>(k, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return o1.getScore() - o2.getScore();
            }
        });

        Queue<TrieNode> queueForTraverse = new LinkedList<>();
        queueForTraverse.add(curr);
        while (!queueForTraverse.isEmpty()){
            TrieNode node = queueForTraverse.poll();
            if(node.ResultPairs.size() > 0){
                //update resultpq
                for(Pair pair : node.ResultPairs){
                    if(resultpq.size() < k){
                        resultpq.add(pair);
                    } else {
                        if(resultpq.peek().getScore() < pair.getScore()){
                            resultpq.poll();
                            resultpq.add(pair);
                        }
                    }
                }
            }
            for(TrieNode child : node.children.values()){
                if(child != null){
                    queueForTraverse.offer(child);
                }
            }
        }

        List<String> res = new ArrayList<>();
        //System.out.println(resultpq.size());
        while(resultpq.size() != 0){
            res.add(resultpq.poll().getName());
        }
        return res;
    }

    private void initTrie(){
        // trie file not exist, randomly generate pairs
        File file = new File(TRIE_FILE_NAME);
        if (!file.exists()) {
            File inputFile = new File(INPUT_FILE_NAME);
            if (!inputFile.exists()) {
                List<Pair> pairs = PairGenerator.generatePair(Integer.valueOf(pairAmount), Integer.valueOf(maxStringLength));
                SerializeUtil.writeObjectToFile(INPUT_FILE_NAME, pairs);
            }
            List<Pair> pairs = SerializeUtil.getObjectFromFile(inputFile, List.class);
            root = new TrieNode();
            for(Pair pair : pairs){
                //System.out.println(pair.getName());
                //System.out.println(pair.getScore());
                insertPair(pair);
            }
            SerializeUtil.writeObjectToFile(TRIE_FILE_NAME, root);
        } else {
            root = SerializeUtil.getObjectFromFile(file, TrieNode.class);
        }
    }


    private void insertPair(Pair pair){
        String pairName = pair.getName();
        //abc abc_def
        String[] names = pairName.split("_");
        //abc def => def abc
        reverseStringArray(names);

        String partialName = "";
        for(String name: names){
            TrieNode curr = root;
            if(partialName.equals("")){
                partialName += name;
                //System.out.println(partialName);
            } else {
                partialName = name + "_" + partialName;
                //System.out.println(partialName);
            }

            for(char c : partialName.toCharArray()){
                if(!curr.children.containsKey(c)){
                    curr.children.put(c, new TrieNode());
                }
                curr = curr.children.get(c);
            }

            // update the same string with higher score
            for(Pair existPair:curr.ResultPairs){
                if(existPair.getName().equals(pair.getName())){
                    if(pair.getScore() > existPair.getScore()){
                        curr.ResultPairs.remove(existPair);
                        curr.ResultPairs.add(pair);
                    }
                    return;
                }
            }
            curr.ResultPairs.add(pair);
        }
    }

    private void reverseStringArray(String[] arr) {
        for(int start=0,end=arr.length-1;start<end;start++,end--) {
            String temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }
    }


    public DictionaryTrie(){
        initTrie();
        this.cache = new HashMap<>();
    }
}