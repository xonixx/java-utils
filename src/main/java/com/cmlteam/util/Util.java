package com.cmlteam.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Util {

    private Util() {
    }

    /**
     * @param name input filename
     * @return safe filename: contains only letters, numbers, uderscores(_), dots(.)
     */
    public static String safeFilename(String name) {
        String[] parts = name.split("\\.");

        for (int i = 0; i < parts.length; i++) {
            // replaces any character that isn't a number, letter or underscore
            parts[i] = parts[i].replaceAll("\\W+", "");
        }

        return StringUtils.join(parts, '.');
    }

    /**
     * TODO
     */
    public static String trim(String str, int maxLen) {
        if (str == null)
            return null;

        if (str.length() <= maxLen)
            return str;

        return str.substring(0, maxLen) + "...";
    }

    /**
     * @param s input string
     * @return hexadecimal string of length 40 which represents the SHA1 hash of input
     */
    public static String sha1(String s) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(s.getBytes("UTF-8"));
            /*
             * Why pad up to 40 characters? Because SHA-1 has an output
             * size of 160 bits. Each hexadecimal character is 4-bits.
             * 160 / 4 = 40
             */
            return StringUtils.leftPad(new BigInteger(1, messageDigest.digest()).toString(16), 40, '0');
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String renderDurationFromStart(long startMillis) {
        return renderDelta(System.currentTimeMillis() - startMillis);
    }

    public static String renderDelta(long deltaMillis) {
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
                ? dayS + " " + hrS
                : deltaHr > 0
                ? hrS + " " + minS
                : deltaMin > 0
                ? minS + " " + secSI
                : deltaSec > 0
                ? secS
                : "0 s";
    }
}
