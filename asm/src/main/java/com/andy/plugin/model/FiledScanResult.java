package com.andy.plugin.model;

import java.util.List;

public class FiledScanResult {
    /**such as com.example.test.Demo#Name<br/>
     * class is com.example.test.Demo,<br/>
     * and filed name is Name*/
    String filedReference;

    /***/
    List<String> usageList;
}