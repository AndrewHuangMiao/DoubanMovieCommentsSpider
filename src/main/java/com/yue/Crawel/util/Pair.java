package com.yue.Crawel.util;

/**
 * Created by andrew on 16/3/1.
 */

import java.io.Serializable;

public class Pair<T1, T2> implements Serializable {
    public T1 first = null;
    public T2 second = null;

    public Pair(T1 t1, T2 t2) {
        first = t1;
        second = t2;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    private static boolean equals(Object x, Object y) {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        return other instanceof Pair
                && equals(first, ((Pair<T1, T2>) other).first)
                && equals(second, ((Pair<T1, T2>) other).second);
    }

    @Override
    public int hashCode() {
        if (first == null)
            return (second == null) ? 0 : second.hashCode() + 1;
        else if (second == null)
            return first.hashCode() + 2;
        else
            return first.hashCode() * 17 + second.hashCode();
    }

    @Override
    public String toString() {
        return "{" + getFirst() + "," + getSecond() + "}";
    }
}

