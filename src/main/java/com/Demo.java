package com;

public class Demo {
    String a;
    Context b;
    long c;
    public Demo(String var1, Context var2, long var3) {
        this.a = var1;
        this.b = var2;
        this.c = var3;

        Runnable cmd = new Runnable() {
            @Override
            public void run() {
                //do nothing
            }
        };
    }
}
