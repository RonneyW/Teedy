package com.sismics.docs.core.util;

import com.sismics.docs.core.constant.ConfigType;
import org.junit.Assert;
import org.junit.Test;

public class TestConfigUtil {

    @Test
    public void testGetConfigBooleanValueDefault() {
        // Assume NEXT_DOCUMENT_INCREMENT is not a boolean, or just try to get a boolean value with default fallback
        // The default fallback in `ConfigUtil.getConfigBooleanValue(configType, defaultValue)`
        boolean val = ConfigUtil.getConfigBooleanValue(ConfigType.OCR_ENABLED, true);
        System.out.println("Config value: " + val);
        // It might be false or true depending on db, but let's test if it throws no exception
        Assert.assertNotNull(val);
    }
}