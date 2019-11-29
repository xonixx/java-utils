package com.cmlteam.util;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.regex.Pattern;

public class UtilTest extends TestCase {
  public void testSafeToBoolean() {
    Assert.assertTrue(Util.safeToBoolean(true));
    Assert.assertTrue(Util.safeToBoolean("true"));
    Assert.assertFalse(Util.safeToBoolean(false));
    Assert.assertFalse(Util.safeToBoolean(null));
    Assert.assertFalse(Util.safeToBoolean("TRUE"));
    Assert.assertFalse(Util.safeToBoolean(new Object()));
  }

  public void testSafeFilename() {
    // https://stackoverflow.com/questions/1976007/what-characters-are-forbidden-in-windows-and-linux-directory-names
    String forbiddenSymbols = "/\\?%*:|\"<>";
    Assert.assertEquals(Util.safeFilename("_file.txt"), "_file.txt");
    Assert.assertEquals(Util.safeFilename(forbiddenSymbols + "fil" + forbiddenSymbols + "e.txt" + forbiddenSymbols), "file.txt");
    Assert.assertEquals(Util.safeFilename(".file.txt"), ".file.txt");
  }

  public void testSafeToInt() {
    Assert.assertEquals(Util.safeToInt(0), 0);
    Assert.assertEquals(Util.safeToInt(null, 3), 3);
    Assert.assertEquals(Util.safeToInt("0"), 0);
    Assert.assertEquals(Util.safeToInt(Integer.MAX_VALUE), Integer.MAX_VALUE);
    Assert.assertEquals(Util.safeToInt(Integer.MIN_VALUE), Integer.MIN_VALUE);
    Assert.assertEquals(Util.safeToInt("-2"), -2);
    Assert.assertEquals(Util.safeToInt("2a"), 0);
    Assert.assertEquals(Util.safeToInt("2a", 3), 3);
  }

  public void testSafeToLong() {
    Assert.assertEquals(Util.safeToLong(0), 0);
    Assert.assertEquals(Util.safeToLong(null, 3), 3);
    Assert.assertEquals(Util.safeToLong("0"), 0);
    Assert.assertEquals(Util.safeToLong(Long.MAX_VALUE), Long.MAX_VALUE);
    Assert.assertEquals(Util.safeToLong(Long.MIN_VALUE), Long.MIN_VALUE);
    Assert.assertEquals(Util.safeToLong("-2"), -2);
    Assert.assertEquals(Util.safeToLong("2a"), 0);
    Assert.assertEquals(Util.safeToLong("2a", 3), 3);
  }

  public void testTrim() {
    String string10 = "qwertyuiop";
    Assert.assertEquals(Util.trim(string10, 1), "q...");
    Assert.assertEquals(Util.trim(string10, 10), string10);
  }

  public void testSha1() {
    String string = "qwertyuiop";
    Assert.assertEquals(Util.sha1(string).getBytes().length, 40);
  }

  public void testRenderDelta() {
    long millisSec = 1000;
    long millisMin = millisSec * 60;
    long millisHr = millisMin * 60;
    long millisDay = millisHr * 24;
    Assert.assertEquals(Util.renderDelta(millisDay * 2 + millisHr * 3 + millisMin * 4 + millisSec * 5), "2 d 3 h");
    Assert.assertEquals(Util.renderDelta(millisHr * 3 + millisMin * 4 + millisSec * 5), "3 h 4 m");
    Assert.assertEquals(Util.renderDelta(millisMin * 4 + millisSec * 5), "4 m 5 s");
    Assert.assertEquals(Util.renderDelta(millisDay * 2 + millisSec * 5), "2 d null");
  }

  public void testRenderDurationFromStart() {
    long millisSec = 1000;
    Assert.assertNotNull(Util.renderDurationFromStart(millisSec));
  }

  public void testHumanReadableByteCount() {
    Assert.assertEquals(Util.humanReadableByteCount(11, true), "11 B");
    Assert.assertEquals(Util.humanReadableByteCount(1000, true), "1,0 kB");
    Assert.assertEquals(Util.humanReadableByteCount(1000000, true), "1,0 MB");
    Assert.assertEquals(Util.humanReadableByteCount(1000000000, true), "1,0 GB");
    Assert.assertEquals(Util.humanReadableByteCount(1000000000000L, true), "1,0 TB");

    Assert.assertEquals(Util.humanReadableByteCount(11, false), "11 B");
    Assert.assertEquals(Util.humanReadableByteCount(1024, false), "1,0 KiB");
    Assert.assertEquals(Util.humanReadableByteCount(1024 * 1024, false), "1,0 MiB");
    Assert.assertEquals(Util.humanReadableByteCount(1024 * 1024 * 1024, false), "1,0 GiB");
    Assert.assertEquals(Util.humanReadableByteCount(1024 * 1024 * 1024 * 1024L, false), "1,0 TiB");
  }

  public void testRenderFileSize() {
    Assert.assertEquals(Util.renderFileSize(11), "11 B");
    Assert.assertEquals(Util.renderFileSize(1024), "1,0 KB");
    Assert.assertEquals(Util.renderFileSize(1024 * 1024), "1,0 MB");
    Assert.assertEquals(Util.renderFileSize(1024 * 1024 * 1024), "1,0 GB");
    Assert.assertEquals(Util.renderFileSize(1024 * 1024 * 1024 * 1024L), "1,0 TB");
  }

  public void testGetUnsafe() {
    Assert.assertNotNull(Util.getUnsafe());
  }

  public void testReplaceRegexWithCallback() {
    Util.StringReplacerCallback stringReplacerCallback = match -> "";
    Assert.assertEquals(
        Util.replaceRegexWithCallback("\\q&we-%123", Pattern.compile("\\W+", Pattern.MULTILINE), stringReplacerCallback),
        "qwe123"
    );
  }
}
