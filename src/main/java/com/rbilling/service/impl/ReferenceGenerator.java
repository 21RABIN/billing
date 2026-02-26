package com.rbilling.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class ReferenceGenerator {

    // Sequence counter (can be reset daily or kept global)
    private static final AtomicLong counter = new AtomicLong(1);

    public static String generateReferenceNo() {
        // Current date in YYYYMMDD format
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // Increment sequence
        long seq = counter.getAndIncrement();

        // Build reference number
        return "PAY-" + datePart + "-" + String.format("%04d", seq);
    }
}
