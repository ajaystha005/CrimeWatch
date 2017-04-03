package com.mvp.crimewatch.helper;

import com.mvp.crimewatch.model.Crime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ajayshrestha on 3/19/17.
 */

/**
 * CrimeProcessor helps to process the Crime data and generate the Crime Frequency, Crime Ranking and Crime Dictionary
 */
public class CrimeProcessor {

    public static void processCrimeData(List<Crime> crimeList,
                                        Map<String, Integer> lookupFrequency,
                                        Map<String, ArrayList<Crime>> lookupKeyWords) {

        for (Crime crime : crimeList) {
            calculateCrimeFrequency(crime, lookupFrequency);
            constructCrimeDictionary(crime, lookupKeyWords);
        }
    }

    /**
     * Calculate the Crime Frequency
     *
     * @param crime
     * @param lookupFrequency
     */
    private static void calculateCrimeFrequency(Crime crime, Map<String, Integer> lookupFrequency) {
        if (lookupFrequency.containsKey(crime.getCpdDistrict())) {
            lookupFrequency.put(crime.getCpdDistrict(), lookupFrequency.get(crime.getCpdDistrict()) + 1);
        } else {
            lookupFrequency.put(crime.getCpdDistrict(), 1);
        }
    }

    /**
     * Construct the Crime Dictionary
     *
     * @param crime
     * @param lookupKeyWords
     */
    private static void constructCrimeDictionary(Crime crime, Map<String, ArrayList<Crime>> lookupKeyWords) {
        String[] words = crime.getCrimeDescription().split("[^\\w']+");
        for (String word : words) {
            word = word.toLowerCase();
            if (lookupKeyWords.containsKey(word)) {
                ArrayList<Crime> crimeList = lookupKeyWords.get(word);
                crimeList.add(crime);
                lookupKeyWords.put(word, crimeList);
            } else {
                ArrayList<Crime> crimeList = new ArrayList<>();
                crimeList.add(crime);
                lookupKeyWords.put(word, crimeList);
            }

        }
    }

    /**
     * Sort the Map in descending order
     *
     * @param unsortMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByCrimeFrequency(Map<K, V> unsortMap) {

        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;

    }

    /**
     * Calculate the crime Rank based on Frequency
     *
     * @param crimeFrequency
     * @return
     */
    public static HashMap<String, Integer> getCrimeByRank(Map<String, Integer> crimeFrequency) {
        int rank = 0;
        int previousRank = 0;
        HashMap<String, Integer> crimeRank = new HashMap<>();
        for (Map.Entry entry : crimeFrequency.entrySet()) {
            if ((Integer) entry.getValue() != previousRank) {
                crimeRank.put((String) entry.getKey(), ++rank);
            } else {
                crimeRank.put((String) entry.getKey(), rank);
            }
            previousRank = (Integer) entry.getValue();
        }

        return crimeRank;
    }


}
