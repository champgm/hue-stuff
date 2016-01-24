package com.cgm.java.hue.web.util;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Enum to help keep documentation of attribute keys that can be used in {@link javax.servlet.http.HttpServletRequest}/
 * {@link javax.servlet.http.HttpServletResponse} objects
 */
public enum KnownParameterNames {

    LIGHT_ID("lightid"),
    LIGHT_LIST("lightlist"),
    SCENE_LIST("scenelist");

    private final String name;

    KnownParameterNames(final String name) {
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "name may not be null or empty.");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
