package com.alation.autocompletepoc.util;

import com.alation.autocompletepoc.util.Domain.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PairGenerator {
    public static List<Pair> generatePair(int pairAmount, int maxStringLength){
        String str = "abcedfghigklmnopqrstuvwxyz_" ;
        Random rand = new Random() ;
        List<Pair> pairList = new ArrayList<>();

        //generate pair
        for(int i = 0; i < pairAmount; i++){
            int randLength = rand.nextInt(maxStringLength - 1);
            StringBuffer sb = new StringBuffer();
            for(int j = 0; j < randLength; j++){
                int index = rand.nextInt(27);
                char randChar = str.charAt(index);
                if (randChar == '_' && sb.length() > 0 && sb.charAt(sb.length() - 1) == '_') {
                    continue;
                }
                sb.append(randChar);
            }
            if(sb.toString().equals("") || sb.toString().startsWith("_") || sb.toString().endsWith("_")){
                continue;
            }
            pairList.add(new Pair(sb.toString(), (int)(Math.random()*1000)));
        }
        return pairList;
    }
}