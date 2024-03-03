package com.andt.controller;

import com.andt.model.ParamPair;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@SessionScoped
public class User implements Serializable {

    private String data = "";
    private String outputJava = "";

    private Set<ParamPair> keyValueParams;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data.trim();
    }

    public String getOutputJava() {
        return outputJava;
    }

    public void setOutputJava(String outputJava) {
        this.outputJava = outputJava;
    }

    public Set<ParamPair> getKeyValueParams() {
        return keyValueParams;
    }

    public void setKeyValueParams(Set<ParamPair> keyValueParams) {
        this.keyValueParams = keyValueParams;
    }

    public void updating() {
        outputJava = "";
        String[] lines = data.split("\n");
        Map<String, String> pairMap = ParamPair.convertListToMap(keyValueParams);
        for (String line : lines) {
            outputJava += replaceSubstrings(line, pairMap) + "\n";
        }
        outputJava = formatSQL(outputJava);
    }

    private static String formatSQL(String input) {
        StringBuilder sb = new StringBuilder();
        String[] lines = input.split("\n");

        for (String line : lines) {

            String[] sql = line.split("--");
            if (sql.length == 2) {
                int idx = getLastNonSpaceIndex(sql[0]);
                String tmp;
                if (idx != -1) {
                    tmp = "sb.append(\" " + sql[0].substring(0, getLastNonSpaceIndex(sql[0]) + 1) + " \");";
                } else {
                    tmp = "sb.append(\" " + sql[0] + " \");";
                }
                sb.append(padRight(tmp, 80));
                sb.append("// ").append(sql[1].trim()).append("\n");

            } else {
                sb.append("sb.append(\" ").append(sql[0].trim()).append(" \");\n");
            }
        }

        return sb.toString();
    }

    private static int getLastNonSpaceIndex(String str) {
        // Start iterating from the end of the string
        for (int i = str.length() - 1; i >= 0; i--) {
            // Check if the character is not a space
            if (!Character.isWhitespace(str.charAt(i))) {
                return i; // Return the index of the last non-space character
            }
        }
        // If the string is empty or contains only spaces, return -1
        return -1;
    }

    private static String padRight(String input, int length) {
        if (input.length() >= length) {
            return input;
        }

        while (input.length() < length) {
            input += " ";
        }

        return input;
    }

    public static String replaceSubstrings(String input, Map<String, String> replacementMap) {
        for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            input = input.replace(key, "?" + value);
        }

        return input;
    }

    public void checkParam() {
        keyValueParams = new TreeSet<>();
        String[] linesInput = data.split("\n");
        for (String line : linesInput) {
            String res = findPlaceholder(line);
            if (res.isEmpty()) {
                continue;
            }
            keyValueParams.add(new ParamPair(res, res));
        }
    }

    private static String findPlaceholder(String inputString) {
        // Define the regular expression pattern
        Pattern pattern = Pattern.compile("\\{(\\d+)}");

        // Create a matcher object
        Matcher matcher = pattern.matcher(inputString);

        // Check if the pattern is found
        if (matcher.find()) {
            // Return the matched substring
            return matcher.group();
        } else {
            // Return null if no match is found
            return "";
        }
    }

}
