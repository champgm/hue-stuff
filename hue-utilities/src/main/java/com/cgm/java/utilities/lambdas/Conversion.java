package com.cgm.java.utilities.lambdas;

import java.util.function.Function;

import com.cgm.java.hue.models.Light;

/**
 * Lambdas for converting one type to another
 */
public class Conversion {
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
