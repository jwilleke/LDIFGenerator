package com.willeke.utility;

/**
 * Title:        Collection of conversion methods
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Willeke.com
 * @author       Jim Willeke
 * @version 1.0
 */

import java.util.*;

public class RandConvUtility
{
    private static Random rn = new Random();
    private static final int SIZE = 95;

    private static final int ASCII[] = { 0x0020, 0x0021, 0x0022, 0x0023, 0x0024, 0x0025, 0x0026, 0x0027, 0x0028, 0x0029, 0x002a, 0x002b, 0x002c, 0x002d, 0x002e, 0x002f, 0x0030,
	    0x0031, 0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037, 0x0038, 0x0039, 0x003a, 0x003b, 0x003c, 0x003d, 0x003e, 0x003f, 0x0040, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045,
	    0x0046, 0x0047, 0x0048, 0x0049, 0x004a, 0x004b, 0x004c, 0x004d, 0x004e, 0x004f, 0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005a,
	    0x005b, 0x005c, 0x005d, 0x005e, 0x005f, 0x0060, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067, 0x0068, 0x0069, 0x006a, 0x006b, 0x006c, 0x006d, 0x006e, 0x006f,
	    0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077, 0x0078, 0x0079, 0x007a, 0x007b, 0x007c, 0x007d, 0x007e };

    private static final int EBCDIC[] = { 0x0040, 0x005a, 0x007f, 0x007b, 0x005b, 0x006c, 0x0050, 0x007d, 0x004d, 0x005d, 0x005c, 0x004e, 0x006b, 0x0060, 0x004b, 0x0061, 0x00f0,
	    0x00f1, 0x00f2, 0x00f3, 0x00f4, 0x00f5, 0x00f6, 0x00f7, 0x00f8, 0x00f9, 0x007a, 0x005e, 0x004c, 0x007e, 0x006e, 0x006f, 0x007c, 0x00c1, 0x00c2, 0x00c3, 0x00c4, 0x00c5,
	    0x00c6, 0x00c7, 0x00c8, 0x00c9, 0x00d1, 0x00d2, 0x00d3, 0x00d4, 0x00d5, 0x00d6, 0x00d7, 0x00d8, 0x00d9, 0x00e2, 0x00e3, 0x00e4, 0x00e5, 0x00e6, 0x00e7, 0x00e8, 0x00e9,
	    0x00ad, 0x00e0, 0x00bd, 0x005f, 0x006d, 0x0079, 0x0081, 0x0082, 0x0083, 0x0084, 0x0085, 0x0086, 0x0087, 0x0088, 0x0089, 0x0091, 0x0092, 0x0093, 0x0094, 0x0095, 0x0096,
	    0x0097, 0x0098, 0x0099, 0x00a2, 0x00a3, 0x00a4, 0x00a5, 0x00a6, 0x00a7, 0x00a8, 0x00a9, 0x00c0, 0x006a, 0x00d0, 0x00a1 };

    public RandConvUtility()
    {
    }

    public static int getIntInRange(int min, int max)
    {
	// above, e.g. as method parameters or otherwise

	// nextInt is normally exclusive of the top value,
	// so add 1 to make it inclusive
	int value = max - min + 1;
	if (value <= 0)
	{
	    value = 1;
	}
	int randomNum = rn.nextInt(value) + min;
	return randomNum;
    }

    /**
     * <p>
     * Provides a quick way to get a random integer between lo and hi
     * </p>
     * 
     * @param lo
     *            lower bound
     * @param hi
     *            upper bound
     * @return int
     * @throws out
     *             -of-bounds when errors occur ??.
     */
    public static int randInt(int lo, int hi)
    {

	int n = hi - lo;
	if (n <= 0)
	{
	    n = 1;
	}
	int i = rn.nextInt() % n;
	if (i < 0)
	{
	    i = -i;
	}
	return lo + i;
    }

    /*
     * Numbers 48 through 57 are the numerals zero through nine. Numbers 65 through 90 are the capital letters A through Z.
     */

    /**
     * <p>
     * Provides method to get a random string of Numbers and UPPERcase letters of requested length. Like a License Plate Number
     * </p>
     * 
     * @param length
     *            of returned String
     * @return str Random Numbers and UPPERCASE letters
     * @throws <code>Outofbounds</code> - when errors occur.
     */
    public static String randASCII(int length)
    { // Return Numbers and UPPERCASE letters
	String str = "";
	int asc;
	for (int i = 0; i < length; i++)
	{
	    asc = randInt(48, 90);
	    if ((asc < 58) || (asc > 64))
	    { // can use it! Try Again
		str = str + (char) asc;
		// System.out.print(asc+" is Good!\n");
	    }
	    else
	    {
		length++;
	    }
	}
	return str;
    }

    // Retunrs a Random String of Bytes
    public static String randString(int lo, int hi)
    {
	int n = randInt(lo, hi);
	byte b[] = new byte[n];
	for (int i = 0; i < n; i++)
	{
	    b[i] = (byte) randInt('a', 'z');
	}
	return new String(b);
    }

    public final static int translateByte(int i, String charSet)
    {
	String thisCharSet = charSet;
	if (thisCharSet.equals("EBCDIC"))
	{
	    for (int j = 0; j < SIZE; j++)
	    {
		if (i == ASCII[j])
		{
		    return EBCDIC[j];
		}
	    }
	}
	return i;
    }
}