package com.stuxo.notify.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by stu on 1/11/14.
 */
class NotificationId {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int createId() {
        return c.incrementAndGet();
    }
    public static int getId(){
        return c.get();
    }
}
