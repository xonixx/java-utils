package com.cmlteam.util;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {
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
    String forbiddenSymbols = "/\\?%*:|\"<>";
    assertEquals(Util.safeFilename("_file.txt"), "_file.txt");
    assertEquals(
        Util.safeFilename(forbiddenSymbols + "fil" + forbiddenSymbols + "e.txt" + forbiddenSymbols),
        "file.txt");
    assertEquals(Util.safeFilename(".file.txt"), ".file.txt");
  }

  @Test
  public void testSafeToInt() {
    assertEquals(Util.safeToInt(0), 0);
    assertEquals(Util.safeToInt(null, 3), 3);
    assertEquals(Util.safeToInt("0"), 0);
    assertEquals(Util.safeToInt(Integer.MAX_VALUE), Integer.MAX_VALUE);
    assertEquals(Util.safeToInt(Integer.MIN_VALUE), Integer.MIN_VALUE);
    assertEquals(Util.safeToInt("-2"), -2);
    assertEquals(Util.safeToInt("2a"), 0);
    assertEquals(Util.safeToInt("2a", 3), 3);
  }

  @Test
  public void testSafeToLong() {
    assertEquals(Util.safeToLong(0), 0);
    assertEquals(Util.safeToLong(null, 3), 3);
    assertEquals(Util.safeToLong("0"), 0);
    assertEquals(Util.safeToLong(Long.MAX_VALUE), Long.MAX_VALUE);
    assertEquals(Util.safeToLong(Long.MIN_VALUE), Long.MIN_VALUE);
    assertEquals(Util.safeToLong("-2"), -2);
    assertEquals(Util.safeToLong("2a"), 0);
    assertEquals(Util.safeToLong("2a", 3), 3);
  }

  @Test
  public void testTrim() {
    String string10 = "qwertyuiop";
    assertNull(Util.trim(null, 10));
    assertEquals(Util.trim(string10, 1), "q...");
    assertEquals(Util.trim(string10, 10), string10);
  }

  @Test
  public void testSha1() {
    assertEquals(Util.sha1("qwertyuiop"), "b0399d2029f64d445bd131ffaa399a42d2f8e7dc");
    assertEquals(Util.sha1("6117"), "00078f66cd4321af437c7d9486bacb3b3b187328");
    assertEquals(Util.sha1("946399"), "00000cb4a5d760de88fecb38e2f71b7bec52e834");
  }

  @Test
  public void testRenderDelta() {
    long millisSec = 1000;
    long millisMin = millisSec * 60;
    long millisHr = millisMin * 60;
    long millisDay = millisHr * 24;
    assertEquals(
        Util.renderDelta(millisDay * 2 + millisHr * 3 + millisMin * 4 + millisSec * 5), "2 d 3 h");
    assertEquals(Util.renderDelta(millisHr * 3 + millisMin * 4 + millisSec * 5), "3 h 4 m");
    assertEquals(Util.renderDelta(millisMin * 4 + millisSec * 5), "4 m 5 s");
    assertEquals(Util.renderDelta(millisDay * 2 + millisSec * 5), "2 d null");
  }

  @Test
  public void testRenderDurationFromStart() {
    long millisSec = 1000;
    assertNotNull(Util.renderDurationFromStart(millisSec));
  }

  @Test
  public void testHumanReadableByteCount() {
    char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
    assertEquals(Util.humanReadableByteCount(11, true), "11 B");
    assertEquals(Util.humanReadableByteCount(1000, true), "1" + decimalSeparator + "0 kB");
    assertEquals(Util.humanReadableByteCount(1000000, true), "1" + decimalSeparator + "0 MB");
    assertEquals(Util.humanReadableByteCount(1000000000, true), "1" + decimalSeparator + "0 GB");
    assertEquals(
        Util.humanReadableByteCount(1000000000000L, true), "1" + decimalSeparator + "0 TB");

    assertEquals(Util.humanReadableByteCount(11, false), "11 B");
    assertEquals(Util.humanReadableByteCount(1024, false), "1" + decimalSeparator + "0 KiB");
    assertEquals(Util.humanReadableByteCount(1024 * 1024, false), "1" + decimalSeparator + "0 MiB");
    assertEquals(
        Util.humanReadableByteCount(1024 * 1024 * 1024, false), "1" + decimalSeparator + "0 GiB");
    assertEquals(
        Util.humanReadableByteCount(1024 * 1024 * 1024 * 1024L, false),
        "1" + decimalSeparator + "0 TiB");
  }

  @Test
  public void testRenderFileSize() {
    char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
    assertEquals(Util.renderFileSize(11), "11 B");
    assertEquals(Util.renderFileSize(1024), "1" + decimalSeparator + "0 KB");
    assertEquals(Util.renderFileSize(1024 * 1024), "1" + decimalSeparator + "0 MB");
    assertEquals(Util.renderFileSize(1024 * 1024 * 1024), "1" + decimalSeparator + "0 GB");
    assertEquals(Util.renderFileSize(1024 * 1024 * 1024 * 1024L), "1" + decimalSeparator + "0 TB");
  }

  @Test
  public void testGetUnsafe() {
    assertNotNull(Util.getUnsafe());
  }

  @Test
  public void testReplaceRegexWithCallback() {
    Util.StringReplacerCallback stringReplacerCallback = match -> "";
    assertEquals(
        Util.replaceRegexWithCallback(
            "\\q&we-%123", Pattern.compile("\\W+", Pattern.MULTILINE), stringReplacerCallback),
        "qwe123");
  }
}
