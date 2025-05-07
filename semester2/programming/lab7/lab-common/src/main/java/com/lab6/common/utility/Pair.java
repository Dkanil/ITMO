package com.lab6.common.utility;

import java.io.Serial;
import java.io.Serializable;

public class Pair<A, B> implements Serializable {
    @Serial
    private static final long serialVersionUID = 12L;

    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

    public void setFirst(A a) {
        this.a = a;
    }

    public void setSecond(B b) {
        this.b = b;
    }
}
