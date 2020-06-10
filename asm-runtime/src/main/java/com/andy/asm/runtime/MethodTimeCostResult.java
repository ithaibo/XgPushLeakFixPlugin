package com.andy.asm.runtime;

public class MethodTimeCostResult {
    public String owner;
    public String name;
    public String desc;
    public long timestampStart;
    public long timestampEnd;

    public MethodTimeCostResult(String owner,
                                String name,
                                String desc,
                                long timestampStart,
                                long timestampEnd) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.timestampStart = timestampStart;
        this.timestampEnd = timestampEnd;
    }

    public MethodTimeCostResult() {}

}