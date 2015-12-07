package com.cgm.java.utilities.lambdas;

import java.util.function.ToLongFunction;

/**
 * Lambdas for converting one type to another
 */
public class Conversion {
    /**
     * This is a Lambda which is actually readable
     */
    public static final ToLongFunction<String> STRING_TO_LONG_PRETTY = (x) -> Long.parseLong(x);
    /**
     * This is a "method reference" implementation of a Lambda
     */
    public static final ToLongFunction<String> STRING_TO_LONG = Long::parseLong;
}
