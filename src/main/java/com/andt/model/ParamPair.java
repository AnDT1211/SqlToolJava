package com.andt.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ParamPair implements Comparable<ParamPair> {
    private String sqlParam;
    private String javaParam;

    public ParamPair(String sqlParam, String javaParam) {
        this.sqlParam = sqlParam;
        this.javaParam = javaParam;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getJavaParam() {
        return javaParam;
    }

    public void setJavaParam(String javaParam) {
        this.javaParam = javaParam;
    }

    @Override
    public String toString() {
        return "ParamPair{" + "sqlParam=" + sqlParam + ", javaParam=" + javaParam + '}';
    }

    @Override
    public int compareTo(ParamPair o) {
        String numStr1 = sqlParam.substring(1, sqlParam.length() - 1);
        String numStr2 = o.getSqlParam().substring(1, o.getSqlParam().length() - 1);
        int num1 = Integer.parseInt(numStr1);
        int num2 = Integer.parseInt(numStr2);
        return num1 - num2;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.sqlParam);
        hash = 83 * hash + Objects.hashCode(this.javaParam);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParamPair other = (ParamPair) obj;
        if (!Objects.equals(this.sqlParam, other.sqlParam)) {
            return false;
        }
        return Objects.equals(this.javaParam, other.javaParam);
    }
    
    public static Map<String, String> convertListToMap(Set<ParamPair> paramPairs) {
        Map<String, String> resultMap = new HashMap<>();

        for (ParamPair pair : paramPairs) {
            resultMap.put(pair.getSqlParam(), pair.getJavaParam());
        }

        return resultMap;
    }
    
}
