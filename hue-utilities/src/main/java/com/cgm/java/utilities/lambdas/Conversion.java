package com.cgm.java.utilities.lambdas;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

import com.cgm.java.hue.models.Light;

/**
 * Lambdas for converting one type to another
 */
public class Conversion {
    /**
     * This is a Lambda which is actually readable
     * It converts {@link java.lang.String} to {@link java.lang.Long}
     */
    public static final ToLongFunction<String> STRING_TO_LONG_PRETTY = Long::parseLong;

    /**
     * This is a "method reference" implementation of a Lambda
     * It converts {@link java.lang.String} to {@link java.lang.Long}
     */
    public static final ToLongFunction<String> STRING_TO_LONG = Long::parseLong;

    /**
     * converts {@link java.lang.String} to {@link java.lang.Double}
     */
    public static final ToDoubleFunction<String> STRING_TO_DOUBLE = Double::parseDouble;

    /**
     * converts {@link java.lang.String} to {@link java.lang.CharSequence}
     */
    public static final Function<String, CharSequence> STRING_TO_SEQUENCE = (string -> string);

    /**
     * Maps a {@link com.cgm.java.hue.models.Light} to its name
     */
    public static final Function<Light, String> LIGHT_TO_NAME = (light -> light.getName().toString());

    /**
     * Maps a {@link com.cgm.java.hue.models.Light} to its ID
     */
    public static final Function<Light, String> LIGHT_TO_ID = (light -> light.getId().toString());
}
