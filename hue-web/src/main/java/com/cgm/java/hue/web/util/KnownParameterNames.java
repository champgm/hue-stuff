package com.cgm.java.hue.web.util;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Enum to help keep documentation of attribute keys that can be used in {@link javax.servlet.http.HttpServletRequest}/
 * {@link javax.servlet.http.HttpServletResponse} objects
 */
public enum KnownParameterNames {

    LIGHT_ID("lightid"),
    GROUP_ID("groupid"),
    TOGGLE_GROUP_RESULT("togglegroupresult"),
    SCENE_ID("sceneid"),
    LIGHT("light"),
    LIGHT_STATE("lightstate"),
    SCENE("scene"),
    SCENE_V2("v2"),
    LIGHT_LIST("lightlist"),
    GROUP_LIST("grouplist"),
    SCENE_LIST("scenelist"),
    SENSOR_LIST("sensorlist");

    private final String name;

    KnownParameterNames(final String name) {
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "name may not be null or empty.");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
