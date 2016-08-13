package com.intbridge.projectduck.utils;

import android.content.Context;

import com.intbridge.projectduck.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import xmlwise.Plist;
import xmlwise.XmlParseException;

/**
 * Created by Derek on 12/26/2014.
 * This is the filter class, it keeps the data and filters data for search
 */
public class SearchSuggestions {

    private Map<String, Object> LocationMap = null;
    private ArrayList<String> totalStringList = new ArrayList<>();
    private ArrayList<String> filteredStringList = new ArrayList<>();
    private ArrayList<LocationSuggestion> totalLocationSuggestion = new ArrayList<>();

    // This function loads the plists data to map(java), it has been hard coded to 'ucsb' currently, but will be easy to load other plist by using the param 'school'
    private Map<String, Object> loadPListByXmlwise(Context here, String school){
        Map<String, Object> schoolMap = null;
        try{
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            inputStream = here.getResources().openRawResource(R.raw.universityoforegon);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            schoolMap = Plist.fromXml(sb.toString());
        }catch (XmlParseException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            return schoolMap;
        }
    }

    // The constructor, which setup the totalStringList for search and load data to this SearchSuggestion object
    public SearchSuggestions(Context here, String school){
        LocationMap = loadPListByXmlwise(here,school);
        if(LocationMap == null){
            //do nothing if the object is empty, which meant something wrong with loadPListByXmlwise
        }else {
            // get a list of string for all location
            for(String key : LocationMap.keySet()) {
                totalStringList.add(key);
                totalLocationSuggestion.add(new LocationSuggestion(key));
            }
            Collections.sort(totalStringList);
            Collections.sort(totalLocationSuggestion);
        }
    }

    // Match the query text with the string in the list, return true if query text is the first letters of the keys
    private boolean matchKey(String key, String queryText){
        if(key == null){
            return false;
        }
        if(queryText.length() > key.length()){
            return false;
        }
        for (int i = 0; i <queryText.length(); i++){
            if(key.charAt(i)!=queryText.charAt(i)){
                return  false;
            }
        }
        return true;
    }

    // Split the key and do match for each
    private boolean checkKey(String key, String queryText){
        String key1 = null;
        String key2 = null;
        String key3 = null;
        if (key.contains("|")) {
            //TODO: key may contains more that 3 parts separate by |, this model can not handle that situation. Make the code more expandable. Also, clean and refactor the code
            //Split key to more key by '|'
            String[] parts = key.split("\\|");
            key1 = parts[0];
            key2 = parts[1];
            // If there is more than two keys
            if(parts.length==3){
                key3 = parts[2];
                key3 = key3.substring(1);
                // chop the key if necessary
                if (key3.charAt(key3.length()-1)==" ".charAt(0)) {
                    key3 = key1.substring(0, key3.length() - 1);
                }
            }
            // chop the key if necessary
            if (key1.charAt(key1.length()-1)==" ".charAt(0)) {
                key1 = key1.substring(0, key1.length() - 1);
            }
            // chop the key if necessary
            key2 = key2.substring(1);
            if (key2.charAt(key2.length()-1)==" ".charAt(0)) {
                key2 = key2.substring(0, key2.length() - 1);
            }
            if(matchKey(key1,queryText)||matchKey(key2,queryText)||matchKey(key3,queryText)){
                return true;
            }else {
                return false;
            }
        } else {
            return matchKey(key,queryText);
        }
    }

    // Get the filtered list by using the checkKey function
    public ArrayList<String> generateFilteredStringList(String newText){
        if(LocationMap == null){
            return null;
        }else {
            for(String key : totalStringList) {
                if (checkKey(key.toLowerCase(), newText.toLowerCase())) {
                    filteredStringList.add(key);
                }
            }
            Collections.sort(filteredStringList);
            return filteredStringList;
        }
    }

    public ArrayList<LocationSuggestion> generateFilteredLocationSuggestionList(String newText){

        ArrayList<LocationSuggestion> filteredLocationSuggestionList = new ArrayList<>();
        for(String key : totalStringList) {
            if (checkKey(key.toLowerCase(), newText.toLowerCase())) {
                filteredLocationSuggestionList.add(new LocationSuggestion(key));
            }
        }
        Collections.sort(filteredLocationSuggestionList);
        return filteredLocationSuggestionList;

    }

    public void resetFilteredStringList(){
        filteredStringList = new ArrayList<String>();
    }

    public ArrayList<LocationSuggestion> getTotalLocationSuggestion(){
        //Collections.sort(totalLocationSuggestion);
        return totalLocationSuggestion;
    }

    public ArrayList<String> getTotalStringList(){
        return totalStringList;
    }

    // Get la and lo by key, return a array for la and lo
    @SuppressWarnings("unchecked")
    public ArrayList<Double> getLaLo(String key){
        if(LocationMap == null){
            return null;
        }else {
            ArrayList<Double> lalo = new ArrayList<Double>();
            Map<String, Object> vMap = (Map<String, Object>) LocationMap.get(key);
            lalo.add((Double) vMap.get("la"));
            lalo.add((Double) vMap.get("lo"));
            return lalo;
        }
    }


}
