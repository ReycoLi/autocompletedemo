package com.alation.autocompletepoc.util.Domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TrieNode implements Serializable {
    public Map<Character, TrieNode> children;
    public List<Pair> ResultPairs;
    public TrieNode(){
        this.children = new HashMap<>();
        this.ResultPairs = new ArrayList<>();
    }
}
