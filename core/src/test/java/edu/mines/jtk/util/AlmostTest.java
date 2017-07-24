/****************************************************************************
Copyright 2003, Landmark Graphics and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.util;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Tests {@link edu.mines.jtk.util.Almost}.
 * @author W.S. Harlan
 * @author Chris Engelsma
 * @version 2017.06.06
 */
public class AlmostTest {

  private Almost a;

  @BeforeMethod
  public void setUp() {
    a = new Almost();
  }

  @Test
  public void testLessThanAndLessEquals() {
    assertTrue (a.lt(1.0,2.0));
    assertFalse(a.lt(2.0,2.0));
    assertFalse(a.lt(3.0,2.0));

    assertTrue (a.le(1.0,2.0));
    assertTrue (a.le(2.0,2.0));
    assertFalse(a.le(3.0,1.0));
  }

  @Test
  public void testGreaterThanAndGreaterEquals() {
    assertTrue (a.gt(2.0,1.0));
    assertFalse(a.gt(1.0,1.0));
    assertFalse(a.gt(0.0,1.0));

    assertTrue (a.ge(2.0,1.0));
    assertTrue (a.ge(1.0,1.0));
    assertFalse(a.ge(0.0,1.0));
  }

  @Test
  public void testSafeDivision() {
    double big = 0.01 * Float.MAX_VALUE;
    assertEquals(-0.5, a.divide(-1.0, 2.0, 0.0));
    assertEquals(-0.5, a.divide( 1.0,-2.0, 0.0));
    assertEquals( 1.0, a.divide( 0.0, 0.0, 1.0));
    assertEquals( 1.0, a.divide(-1.0,-1.0, 1.0));
    assertEquals( 1.0, a.divide(-1.0,-1.0, true));
    assertEquals( 1.0, a.divide(1.0E-18,1.0E-18,1.0));
    assertEquals( big, a.divide( 1.0, 0.0, 1.0));

  }

  @Test
  public void testReciprocal() {
    assertEquals(0.5, a.reciprocal(2.0));
  }

  @Test
  public void testBetween() {
    assertTrue(a.between(1., 0., 2.));
    assertTrue(a.between(-1., 0., -2.));
    assertTrue(a.between(-1., -0.5, -2.));
    assertTrue(a.between(1., 1.000000000001, 2.));
    assertTrue(a.between(-1., -1.000000000001, -2.));
  }

  @Test
  public void testOutside() {
    assertEquals(0,a.outside(1., 0., 2.));
    assertEquals(0,a.outside(1., 0.5, 2.));
    assertEquals(0,a.outside(-1., 0., -2.));
    assertEquals(0,a.outside(-1., -0.5, -2.));
    assertEquals(-1,a.outside(-1., -1.1, -2.));
    assertEquals( 1,a.outside(-1., -0.5, -0.9));
    assertEquals(0,a.outside(1., 1.000000000001, 2.));
  }

  @Test
  public void testCompare() {
    assertTrue  (a.cmp(1., 0.) > 0);
    assertTrue  (a.cmp(0., 1.) < 0);
    assertEquals(0,a.cmp(1., 1.));
    assertEquals(0,a.cmp(0., 0.));
    assertEquals(0,a.cmp(1., 1.000000000001));

    Integer i = 1;
    Integer j = 1;
    assertEquals(0,a.compare(i,j));
  }

  @Test
  public void testEquality() {
    assertTrue (a.equal(3.,3.));
    assertTrue (a.equal(0.,0.));
    assertTrue (a.equal(1., 1.+a.getEpsilon()/2.));
    assertFalse(a.equal(1., 1.+a.getEpsilon()*2.1));
    assertTrue (a.equal(1., 1.000000000001));
    assertTrue (a.equal(0., a.getMinValue()/2.));
    assertTrue(a.equals(a));
  }

  @Test
  public void testZero() {
    assertTrue (a.zero(0.));
    assertTrue (a.zero(a.getMinValue()/2.));
    assertFalse(a.zero(a.getMinValue()*2.));
  }

  @Test
  public void testEpsilon() {
    assertNotEquals(1.,1.+a.getEpsilon());
    assertNotEquals(1.,1.-a.getEpsilon());
  }

  @Test
  public void testGetMinValue() {
    assertNotEquals(0.,a.getMinValue());
    assertTrue     (a.getMinValue()/2.>0.);
  }

  @Test
  public void testHashCode () {
    Almost a = new Almost(0.001,0.000001);
    assertEquals(0,a.hashCodeOf(0.00000001,100));
    assertEquals(1,a.hashCodeOf(0.99999999,100));
    assertEquals(1,a.hashCodeOf(1.00000001,100));
    assertEquals(123456789L,a.hashCodeOf(123456789L,100));

    assertEquals   (a.hashCodeOf( 3.1415,4),  a.hashCodeOf( 3.1415926,4));
    assertNotEquals(a.hashCodeOf( 3.1415,5),  a.hashCodeOf( 3.1415926,5));

    assertEquals   (a.hashCodeOf(-3.1415,4),  a.hashCodeOf(-3.1415926,4));
    assertNotEquals(a.hashCodeOf(-3.1415,5),  a.hashCodeOf(-3.1415926,5));

    assertEquals   (a.hashCodeOf( 314.15,4),  a.hashCodeOf(314.15926,4));
    assertNotEquals(a.hashCodeOf( 314.15,5),  a.hashCodeOf(314.15926,5));

    assertEquals   (a.hashCodeOf(-314.15,4),  a.hashCodeOf(-314.15926,4));
    assertNotEquals(a.hashCodeOf(-314.15,5),  a.hashCodeOf(-314.15926,5));

    assertEquals   (a.hashCodeOf(0.0031415,4),a.hashCodeOf(0.0031415926,4));
    assertNotEquals(a.hashCodeOf(0.0031415,5),a.hashCodeOf(0.0031415926,5));

    // specify precision differently
    a = new Almost(0.0001);
    assertTrue(a.equal(0.0031415,0.0031415926));
    assertEquals(a.hashCodeOf(0.0031415),a.hashCodeOf(0.0031415926));

    a = new Almost(0.00001);
    assertFalse(a.equal(0.0031415,0.0031415926));
    assertNotEquals(a.hashCodeOf(0.0031415),a.hashCodeOf(0.0031415926));

    a = new Almost(4);
    assertTrue(a.equal(0.0031415,0.0031415926));
    assertEquals(a.hashCodeOf(0.0031415),a.hashCodeOf(0.0031415926));

    a = new Almost(5);
    assertFalse(a.equal(0.0031415,0.0031415926));
    assertNotEquals(a.hashCodeOf(0.0031415),a.hashCodeOf(0.0031415926));

  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testEpsilonTooLargeThrowsException() {
    new Almost(0.10001);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNegativeSignificantDigitsThrowsException() {
    new Almost(-5);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNaNsThrowsException() {
    Almost.FLOAT.equal(0,Float.NaN);
    Almost.FLOAT.equal(0,Double.NaN);
  }

  @Test
  public void testAlmostObjectMethod() {
    Almost af1= new Almost(10*MathPlus.FLT_EPSILON, 100*Float.MIN_VALUE);
    Almost af2 = new Almost(10*MathPlus.FLT_EPSILON);
    Almost af3 = new Almost();
    Almost ad = new Almost(10*MathPlus.DBL_EPSILON, 100*Double.MIN_VALUE);
    assertTrue(af1.equals(af2));
    assertTrue(af1.equals(af3));
    assertTrue(af2.equals(af3));
    assertEquals(af1.hashCode(),af2.hashCode());
    assertEquals(af1.hashCode(),af3.hashCode());
    assertEquals(af2.hashCode(),af3.hashCode());
    assertTrue(af1.toString().equals(af2.toString()));
    assertTrue(af1.toString().equals(af3.toString()));
    assertTrue(af2.toString().equals(af3.toString()));
    for (Almost af: new Almost[] {af1, af2, af3}) {
      assertFalse(af.equals(ad));
      assertNotEquals(af.hashCode(),ad.hashCode());
      assertFalse(af.toString().equals(ad.toString()));
    }
  }
}

