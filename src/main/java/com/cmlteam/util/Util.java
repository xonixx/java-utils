package com.cmlteam.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Util {

  private Util() {}

  /**
   * @param name input filename
   * @return safe filename: contains only letters, numbers, underscores(_), dots(.)
   */
  public static String safeFilename(String name) {
    String[] parts = name.split("\\.");

    for (int i = 0; i < parts.length; i++) {
      // replaces any character that isn't a number, letter or underscore
      parts[i] = parts[i].replaceAll("\\W+", "");
    }

    return String.join(".", parts);
  }

  /**
   * @param boolCandidate input
   * @return boolean representation of input (or false if input doesn't represent valid bool)
   */
  public static boolean safeToBoolean(Object boolCandidate) {
    return Boolean.TRUE.equals(boolCandidate) || "true".equals(boolCandidate);
  }

  /**
   * @param intCandidate input
   * @return int representation of input (or 0 if input doesn't represent valid int)
   */
  public static int safeToInt(Object intCandidate) {
    return safeToInt(intCandidate, 0);
  }

  /**
   * @param intCandidate input
   * @param defaultVal default int value to use
   * @return int representation of input (or defaultVal if input doesn't represent valid int)
   */
  public static int safeToInt(Object intCandidate, int defaultVal) {
    if (intCandidate instanceof Number) {
      return ((Number) intCandidate).intValue();
    }

    if (intCandidate instanceof String) {
      try {
        return Integer.parseInt((String) intCandidate);
      } catch (NumberFormatException e) {
        return defaultVal;
      }
    }

    return defaultVal;
  }

  /**
   * @param longCandidate input
   * @return long representation of input (or 0 if input doesn't represent valid long)
   */
  public static long safeToLong(Object longCandidate) {
    return safeToLong(longCandidate, 0);
  }

  /**
   * @param longCandidate input
   * @param defaultVal default long value to use
   * @return long representation of input (or defaultVal if input doesn't represent valid long)
   */
  public static long safeToLong(Object longCandidate, long defaultVal) {
    if (longCandidate instanceof Number) {
      return ((Number) longCandidate).longValue();
    }

    if (longCandidate instanceof String) {
      try {
        return Long.parseLong((String) longCandidate);
      } catch (NumberFormatException e) {
        return defaultVal;
      }
    }

    return defaultVal;
  }

  /**
   * @param str input string
   * @param maxLen input bound of string
   * @return ellipsis string (3 dots at the end, if string out of bounds)
   */
  public static String trim(String str, int maxLen) {
    if (str == null) {
      return null;
    }

    if (str.length() <= maxLen) {
      return str;
    }

    return str.substring(0, maxLen) + "...";
  }

  /**
   * @param s input string
   * @return hexadecimal string of length 40 which represents the SHA1 hash of input
   */
  public static String sha1(String s) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
      messageDigest.update(s.getBytes(StandardCharsets.UTF_8));
      /*
       * Why pad up to 40 characters? Because SHA-1 has an output
       * size of 160 bits. Each hexadecimal character is 4-bits.
       * 160 / 4 = 40
       */
      return leftPad(new BigInteger(1, messageDigest.digest()).toString(16), 40, '0');
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }

  private static String leftPad(String string, int upToLen, char charToPad) {
    if (string.length() >= upToLen) {
      return string;
    }

    StringBuilder sb = new StringBuilder(string);

    for (int charsToGo = upToLen - sb.length(); charsToGo > 0; charsToGo--) {
      sb.insert(0, charToPad);
    }

    return sb.toString();
  }

  /**
   * Human-readable duration representation. Ex.: "1 h 35 m"
   *
   * @param startMillis the start point of duration interval
   * @return Human-readable duration representation
   */
  public static String renderDurationFromStart(long startMillis) {
    return renderDelta(System.currentTimeMillis() - startMillis);
  }

  /**
   * Human-readable duration representation. Ex.: "1 h 35 m"
   *
   * @param deltaMillis the duration in millis
   * @return Human-readable duration representation
   */
  public static String renderDuration(long deltaMillis) {
    float deltaSec = deltaMillis / 1000f;
    int deltaMin = 0;
    int deltaHr = 0;
    int deltaDay = 0;

    if (deltaSec >= 60) {
      deltaMin = (int) (deltaSec / 60);
      deltaSec = deltaSec - (deltaMin * 60);
    }

    if (deltaMin >= 60) {
      deltaHr = deltaMin / 60;
      deltaMin = deltaMin - (deltaHr * 60);
    }

    if (deltaHr >= 24) {
      deltaDay = deltaHr / 24;
      deltaHr = deltaHr - (deltaDay * 24);
    }

    String dayS = deltaDay > 0 ? deltaDay + " d" : null;
    String hrS = deltaHr > 0 ? deltaHr + " h" : null;
    String minS = deltaMin > 0 ? deltaMin + " m" : null;
    String secS = deltaSec > 0 ? deltaSec + " s" : null;
    String secSI = deltaSec > 0 ? ((int) deltaSec) + " s" : null;

    return dayS != null
        ? dayS + " " + (hrS == null ? "0 h" : hrS)
        : deltaHr > 0
            ? hrS + " " + (minS == null ? "0 m" : minS)
            : deltaMin > 0
                ? (minS + " " + (secSI == null ? "0 s" : secSI))
                : deltaSec > 0 ? secS : "0 s";
  }

  /** @deprecated Use {@link #renderDuration(long)} */
  @Deprecated
  public static String renderDelta(long deltaMillis) {
    return renderDuration(deltaMillis);
  }

  /**
   * Renders human-readable file size. Ex.: "15 KB". Based on
   * http://stackoverflow.com/a/3758880/104522
   *
   * @param bytes file size in bytes
   * @return human-readable file size
   */
  public static String renderFileSize(long bytes) {
    return humanReadableByteCount(bytes, false).replace("i", "");
  }

  /**
   * Renders human-readable file size. Ex.: "15 KB". Based on
   * http://stackoverflow.com/a/3758880/104522
   *
   * @param bytes file size in bytes
   * @param si true to use units of 1000, otherwise 1024
   * @return human-readable file size
   */
  public static String humanReadableByteCount(long bytes, boolean si) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) {
      return bytes + " B";
    }
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
  }

  @SuppressWarnings("restriction")
  public static Unsafe getUnsafe() {
    try {
      Field f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      return (Unsafe) f.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public interface StringReplacerCallback {
    String replace(Matcher match);
  }

  /**
   * @param input input string
   * @param regex that should be replaced
   * @param callback replace logic
   * @return `input` with all occurrences of `regex` being replaced according to replace logic of `callback`
   */
  public static String replaceRegexWithCallback(
      String input, Pattern regex, StringReplacerCallback callback) {

    StringBuilder res = new StringBuilder();

    Matcher regexMatcher = regex.matcher(input);

    int prevStart = 0;

    while (regexMatcher.find()) {
      res.append(input, prevStart, regexMatcher.start());
      res.append(callback.replace(regexMatcher));
      prevStart = regexMatcher.end();
    }

    res.append(input.substring(prevStart));
    return res.toString();
  }
}
