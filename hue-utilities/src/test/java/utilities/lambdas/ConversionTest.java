package utilities.lambdas;

import junit.framework.Assert;

import org.junit.Test;

import com.cgm.java.hue.models.Light;
import com.cgm.java.utilities.lambdas.Conversion;

public class ConversionTest {
    @Test
    public void testLightToName() {
        final String expectedLightName = "lightName";
        final Light light = Light.newBuilder().setId("lightId").setName(expectedLightName).build();
        final String actualLightName = Conversion.LIGHT_TO_NAME.apply(light);

        Assert.assertEquals(expectedLightName, actualLightName);
    }

    @Test
    public void testLightToId() {
        final String expectedLightId = "lightId";
        final Light light = Light.newBuilder().setId(expectedLightId).setName("lightName").build();
        final String actualLightId = Conversion.LIGHT_TO_ID.apply(light);

        Assert.assertEquals(expectedLightId, actualLightId);
    }

    @Test
    public void testStringToSequence() {
        final String string = "string contents";
        final CharSequence sequence = Conversion.STRING_TO_SEQUENCE.apply(string);
        Assert.assertTrue(sequence != null);
        Assert.assertTrue(sequence instanceof CharSequence);
        Assert.assertEquals(string, sequence.toString());
    }
}
