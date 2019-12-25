package com.ygaps.travelapp.Model;

public class SearchUserByKeyword_Data {
    private String searchKey;
    private int pageIndex;
    private String pageSize;

    public SearchUserByKeyword_Data(String searchKey, int pageIndex, String pageSize) {
        this.searchKey = searchKey;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
}
