package com.trium.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configure {

    private static final Configure INSTANCE = new Configure();
    private static final List<String> KEYWORDS = new ArrayList<>();

    private Configure() {
    }

    public static Configure initFromFile(String fileName,String delimiter) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] names = line.split(delimiter);
                for (String name : names) {
                    KEYWORDS.add(name.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    public List<String> getKeywords() {
        return KEYWORDS;
    }
}
