package com.uady.awsproject.common.util;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {

    private static final AtomicLong SEQUENCE = new AtomicLong(0L);

    private IdGenerator() {
    }

    public static Long nextId() {
        return SEQUENCE.incrementAndGet();
    }
}

