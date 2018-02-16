package com.willeke.utility;

/**
 * Title:        Browser Control
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Scott Willeke
 * @author Scott Willeke
 * @version 1.0
 *
 * Credit:
 * Steven Spencer, JavaWorld magazine (<a href="http://www.javaworld.com/javaworld/javatips/jw-javatip66.html">Java Tip 66</a>)
 */

import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A simple, static class to display a URL in the system browser.
 *
 * Under Unix, the system browser is hard-coded to be 'netscape'. Netscape must be in your PATH for this to work. This has been tested with the following platforms: AIX, HP-UX and Solaris.
 *
 * Under Windows, this will bring up the default browser under windows, usually either Netscape or Microsoft IE. The default browser is determined by the OS. This has been tested under Windows 95/98/NT.
 *
 * Examples: BrowserControl.displayURL("http://www.javaworld.com") BrowserControl.displayURL("file://c:\\docs\\index.html") BrowserContorl.displayURL("file:///user/joe/index.html");
 *
 * Note - you must include the url type -- either "http://" or "file://".
 */

public class BrowserControl
{
    /**
     * Display a file in the system browser. If you want to display a file, you must include the absolute path name.
     *
     * @param url
     *            the file's url (the url must start with either "http://" or "file://").
     */
    public static void displayURL(String url)
    {
	    if (Desktop.isDesktopSupported())
	    {
		Desktop desktop = Desktop.getDesktop();
		try
		{
		    desktop.browse(new URI(url));
		}
		catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		catch (URISyntaxException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	    else
	    {
		Runtime runtime = Runtime.getRuntime();
		try
		{
		    runtime.exec("xdg-open " + url);
		}
		catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
    }
}