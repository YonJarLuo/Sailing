package com.jiayuan.commen.response;

import java.util.List;

/**
 * Created by LuoYJ on 2018/3/7.
 */
public class DataResponse extends Response {
    private List<String> resultList;

    public List<String> getResultList() {
        return resultList;
    }

    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }
}
