package com.andy.plugin.biz;

public interface Compact {
    interface Matcher<T> {
        boolean match(T... input);
    }

    interface Vistor<V> {
        V visit(V visitor);
    }
}
