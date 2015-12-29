package com.cgm.java.utilities.lambdas;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;

/**
 * Consumers that print for various types... maybe there is a way to make them generic?
 */
public class Printers {
    /**
     * Prints each thing in a stream
     */
    public static final Consumer<?> PRINTING_CONSUMER = (x) -> System.out.println(x);
    /**
     * Prints each {@link java.lang.Long} in a Stream
     */
    public static final LongConsumer LONG_PRINTER = (x) -> System.out.println(x);
    /**
     * Prints each {@link java.lang.Double} in a stream
     */
    public static final DoubleConsumer DOUBLE_PRINTER = (x) -> System.out.println(x);
}
