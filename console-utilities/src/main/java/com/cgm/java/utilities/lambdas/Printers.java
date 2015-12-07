package com.cgm.java.utilities.lambdas;

import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * Consumers that print for various types... maybe there is a way to make them generic?
 */
public class Printers {
    public static final Consumer<?> PRINTING_CONSUMER = (x) -> System.out.println(x);
    public static final LongConsumer LONG = (x) -> System.out.println(x);
}
