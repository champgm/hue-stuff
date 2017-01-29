package com.cgm.java.hue.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgm.java.hue.models.State;

public class StateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateUtil.class);

    public static boolean areNonNullAttributesEqual(final State expected, final State result) {
        if (expected.getOn() != null && (!expected.getOn().equals(result.getOn()))) {
            LOGGER.info("Bad Thing: getOn()");
            return false;
        }
        if (expected.getBri() != null && (!expected.getBri().equals(result.getBri()))) {
            LOGGER.info("Bad Thing: getBri()");
            return false;
        }
        if (expected.getAlert() != null && (!expected.getAlert().equals(result.getAlert()))) {
            LOGGER.info("Bad Thing: getAlert()");
            return false;
        }
        if (expected.getReachable() != null && (!expected.getReachable().equals(result.getReachable()))) {
            LOGGER.info("Bad Thing: getReachable()");
            return false;
        }
        if (expected.getColormode() != null && (!expected.getColormode().equals(result.getColormode()))) {
            LOGGER.info("Bad Thing: getColormode()");
            return false;
        }
        if (expected.getHue() != null && (!expected.getHue().equals(result.getHue()))) {
            LOGGER.info("Bad Thing: getHue()");
            return false;
        }
        if (expected.getSat() != null && (!expected.getSat().equals(result.getSat()))) {
            LOGGER.info("Bad Thing: getSat()");
            return false;
        }
        if (expected.getEffect() != null && (!expected.getEffect().equals(result.getEffect()))) {
            LOGGER.info("Bad Thing: getEffect()");
            return false;
        }
        if (expected.getXy() != null && (!expected.getXy().equals(result.getXy()))) {
            LOGGER.info("Bad Thing: getXy()");
            return false;
        }
        return true;
    }
}
