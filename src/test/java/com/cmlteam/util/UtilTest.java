package com.cmlteam.util;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

  private static final char DECIMAL_SEPARATOR =
      DecimalFormatSymbols.getInstance().getDecimalSeparator();

  @Test
  public void testSafeToBoolean() {
    assertTrue(Util.safeToBoolean(true));
    assertTrue(Util.safeToBoolean("true"));
    assertFalse(Util.safeToBoolean(false));
    assertFalse(Util.safeToBoolean(null));
    assertFalse(Util.safeToBoolean("TRUE"));
    assertFalse(Util.safeToBoolean(new Object()));
  }

  @Test
  public void testSafeFilename() {
    // https://stackoverflow.com/questions/1976007/what-characters-are-forbidden-in-windows-and-linux-directory-names
    assertEquals("_file.txt", Util.safeFilename("_file.txt"));
    assertEquals(".file.txt", Util.safeFilename(".file.txt"));
    String forbiddenSymbols = "/\\?%*:|\"<>";
    assertEquals(
        "file.txt",
        Util.safeFilename(
            forbiddenSymbols + "fil" + forbiddenSymbols + "e.txt" + forbiddenSymbols));
  }

  @Test
  public void testSafeToInt() {
    assertEquals(0, Util.safeToInt(0));
    assertEquals(3, Util.safeToInt(null, 3));
    assertEquals(0, Util.safeToInt("0"));
    assertEquals(Integer.MAX_VALUE, Util.safeToInt(Integer.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE, Util.safeToInt(Integer.MIN_VALUE));
    assertEquals(-2, Util.safeToInt("-2"));
    assertEquals(0, Util.safeToInt("2a"));
    assertEquals(3, Util.safeToInt("2a", 3));
  }

  @Test
  public void testSafeToLong() {
    assertEquals(0, Util.safeToLong(0));
    assertEquals(3, Util.safeToLong(null, 3));
    assertEquals(0, Util.safeToLong("0"));
    assertEquals(Long.MAX_VALUE, Util.safeToLong(Long.MAX_VALUE));
    assertEquals(Long.MIN_VALUE, Util.safeToLong(Long.MIN_VALUE));
    assertEquals(-2, Util.safeToLong("-2"));
    assertEquals(0, Util.safeToLong("2a"));
    assertEquals(3, Util.safeToLong("2a", 3));
  }

  @Test
  public void testTrim() {
    String string10 = "qwertyuiop";
    assertNull(Util.trim(null, 10));
    assertEquals("q...", Util.trim(string10, 1));
    assertEquals(string10, Util.trim(string10, 10));
  }

  @Test
  public void testSha1() {
    assertEquals("b0399d2029f64d445bd131ffaa399a42d2f8e7dc", Util.sha1("qwertyuiop"));
    assertEquals("00078f66cd4321af437c7d9486bacb3b3b187328", Util.sha1("6117"));
    assertEquals("00000cb4a5d760de88fecb38e2f71b7bec52e834", Util.sha1("946399"));
  }

  @Test
  public void testRenderDelta() {
    long millisSec = 1000;
    long millisMin = millisSec * 60;
    long millisHr = millisMin * 60;
    long millisDay = millisHr * 24;
    assertEquals(
        "2 d 3 h", Util.renderDelta(millisDay * 2 + millisHr * 3 + millisMin * 4 + millisSec * 5));
    assertEquals("3 h 4 m", Util.renderDelta(millisHr * 3 + millisMin * 4 + millisSec * 5));
    assertEquals("4 m 5 s", Util.renderDelta(millisMin * 4 + millisSec * 5));
    assertEquals("2 d null", Util.renderDelta(millisDay * 2 + millisSec * 5));
  }

  @Test
  public void testRenderDurationFromStart() {
    long millisSec = 1000;
    assertNotNull(Util.renderDurationFromStart(millisSec));
  }

  @Test
  public void testHumanReadableByteCount() {
    assertEquals("11 B", Util.humanReadableByteCount(11, true));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 kB", Util.humanReadableByteCount(1000, true));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 MB", Util.humanReadableByteCount(1000000, true));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 GB", Util.humanReadableByteCount(1000000000, true));
    assertEquals(
        "1" + DECIMAL_SEPARATOR + "0 TB", Util.humanReadableByteCount(1000000000000L, true));

    assertEquals("11 B", Util.humanReadableByteCount(11, false));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 KiB", Util.humanReadableByteCount(1024, false));
    assertEquals(
        "1" + DECIMAL_SEPARATOR + "0 MiB", Util.humanReadableByteCount(1024 * 1024, false));
    assertEquals(
        "1" + DECIMAL_SEPARATOR + "0 GiB", Util.humanReadableByteCount(1024 * 1024 * 1024, false));
    assertEquals(
        "1" + DECIMAL_SEPARATOR + "0 TiB",
        Util.humanReadableByteCount(1024 * 1024 * 1024 * 1024L, false));
  }

  @Test
  public void testRenderFileSize() {
    assertEquals("11 B", Util.renderFileSize(11));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 KB", Util.renderFileSize(1024));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 MB", Util.renderFileSize(1024 * 1024));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 GB", Util.renderFileSize(1024 * 1024 * 1024));
    assertEquals("1" + DECIMAL_SEPARATOR + "0 TB", Util.renderFileSize(1024 * 1024 * 1024 * 1024L));
  }

  @Test
  public void testGetUnsafe() {
    assertNotNull(Util.getUnsafe());
  }

  @Test
  public void testReplaceRegexWithCallback() {
    assertEquals(
        "qwe123",
        Util.replaceRegexWithCallback(
            "\\q&we-%123", Pattern.compile("\\W+", Pattern.MULTILINE), match -> ""));
  }
}
