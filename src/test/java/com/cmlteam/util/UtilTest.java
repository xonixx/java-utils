package com.cmlteam.util;

import junit.framework.Assert;
import junit.framework.TestCase;

public class UtilTest extends TestCase {
  public void testSafeToBoolean() {
    Assert.assertTrue(Util.safeToBoolean("true"));
  }
}
