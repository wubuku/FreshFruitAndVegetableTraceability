package org.dddml.ffvtraceability.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TelecomNumberUtilTest {

    @Test
    public void testParseWithNull() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse(null);
        assertNull(result.getCountryCode());
        assertNull(result.getAreaCode());
        assertNull(result.getContactNumber());
    }

    @Test
    public void testParseWithEmptyString() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse("");
        assertNull(result.getCountryCode());
        assertNull(result.getAreaCode());
        assertNull(result.getContactNumber());
    }

    @Test
    public void testParseNorthAmericanNumberWithSpaces() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse("+1 212 5550123");
        assertEquals("1", result.getCountryCode());
        assertEquals("212", result.getAreaCode());
        assertEquals("5550123", result.getContactNumber());
    }

    @Test
    public void testParseNorthAmericanNumberWithoutSpaces() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse("+12125550123");
        assertEquals("1", result.getCountryCode());
        assertEquals("212", result.getAreaCode());
        assertEquals("5550123", result.getContactNumber());
    }

    @Test
    public void testParseChineseNumberWithSpaces() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse("+86 10 12345678");
        assertEquals("86", result.getCountryCode());
        assertEquals("10", result.getAreaCode());
        assertEquals("12345678", result.getContactNumber());
    }

    @Test
    public void testParseChineseNumberWithoutSpaces() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse("+861012345678");
        assertEquals("86", result.getCountryCode());
    }

    @Test
    public void testParseNumberWithoutCountryCode() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse("010 12345678");
        assertNull(result.getCountryCode());
        assertEquals("010", result.getAreaCode());
        assertEquals("12345678", result.getContactNumber());
    }

    @Test
    public void testParseNumberWithoutSpaces() {
        TelecomNumberUtil.TelecomNumberDto result = TelecomNumberUtil.parse("01012345678");
        assertNull(result.getCountryCode());
        assertEquals("010", result.getAreaCode());
        assertEquals("12345678", result.getContactNumber());
    }

    @Test
    public void testFormatFullNumber() {
        TelecomNumberUtil.TelecomNumberDto number = new TelecomNumberUtil.TelecomNumberDto("86", "10", "12345678");
        assertEquals("+86 10 12345678", number.format());
    }

    @Test
    public void testFormatWithoutCountryCode() {
        TelecomNumberUtil.TelecomNumberDto number = new TelecomNumberUtil.TelecomNumberDto(null, "010", "12345678");
        assertEquals("010 12345678", number.format());
    }

    @Test
    public void testFormatNorthAmericanNumber() {
        TelecomNumberUtil.TelecomNumberDto number = new TelecomNumberUtil.TelecomNumberDto("1", "212", "5550123");
        assertEquals("+1 212 5550123", number.format());
    }

    @Test
    public void testFormatPartialNumber() {
        TelecomNumberUtil.TelecomNumberDto number = new TelecomNumberUtil.TelecomNumberDto(null, "12345678", null);
        assertEquals("12345678", number.format());
    }

    @Test
    public void testFormatEmptyNumber() {
        TelecomNumberUtil.TelecomNumberDto number = new TelecomNumberUtil.TelecomNumberDto(null, null, null);
        assertEquals("", number.format());
    }

    @Test
    public void testParseAndFormat() {
        String original = "+86 10 12345678";
        TelecomNumberUtil.TelecomNumberDto dto = TelecomNumberUtil.parse(original);
        String formatted = dto.format();
        assertEquals(original, formatted);
    }
} 