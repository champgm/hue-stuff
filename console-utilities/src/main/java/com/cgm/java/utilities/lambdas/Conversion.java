package com.cgm.java.utilities.lambdas;

import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

/**
 * Lambdas for converting one type to another
 */
public class Conversion {
    /**
     * This is a Lambda which is actually readable
     * It converts {@link java.lang.String} to {@link java.lang.Long}
     */
    public static final ToLongFunction<String> STRING_TO_LONG_PRETTY = (x) -> Long.parseLong(x);

    /**
     * This is a "method reference" implementation of a Lambda
     * It converts {@link java.lang.String} to {@link java.lang.Long}
     */
    public static final ToLongFunction<String> STRING_TO_LONG = Long::parseLong;

    /**
     * converts {@link java.lang.String} to {@link java.lang.Double}
     */
    public static final ToDoubleFunction<String> STRING_TO_DOUBLE = Double::parseDouble;
}
