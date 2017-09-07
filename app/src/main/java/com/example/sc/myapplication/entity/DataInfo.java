package com.example.sc.myapplication.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/6/25.
 */
public class DataInfo {

    public String error;
    public List<Info> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Info> getResults() {
        return results;
    }

    public void setResults(List<Info> results) {
        this.results = results;
    }

    public class Info{
        public String _id;
        public String url;
    }
}
