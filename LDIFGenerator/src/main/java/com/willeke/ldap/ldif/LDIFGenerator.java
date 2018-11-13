package com.willeke.ldap.ldif;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.willeke.utility.BrowserControl;
import com.willeke.utility.Timer;

public class LDIFGenerator extends JFrame
{
    /**
     * 
     */
    private static final String VERSION = "2014-09-01-05:15:59";
    private static final long serialVersionUID = -3968847416242346254L;
    private static final String NL = System.getProperties().getProperty("line.separator");
    private final static String EDIRECTORY = "eDirectory";
    private final static String GENERIC = "Generic";
    private final static String MAD = "Active Directory";
    private static final String[] directoryTypes = { GENERIC, MAD, EDIRECTORY };
    private static final String[] changeTypes = { "None", "add", "delete", "modify", "moddn", "modrdn", };
    Timer timer = new Timer();
    private static int NUMBER_TO_GENERATE = 11;
    boolean isVerbose = false;
    boolean addChangeType = true;
    boolean isQuiet = false;
    private String baseDN;
    private ArrayList<String> orgUnits = new ArrayList<String>();
    private ArrayList<String> areaCodes = new ArrayList<String>();
    private ArrayList<String> employeeTypes = new ArrayList<String>();
    private ArrayList<String> familyNames = new ArrayList<String>();
    private ArrayList<String> givenNames = new ArrayList<String>();
    private ArrayList<String> localities = new ArrayList<String>();
    private ArrayList<String> mailHosts = new ArrayList<String>();
    private ArrayList<String> positions = new ArrayList<String>();
    private ArrayList<String> titleRanks = new ArrayList<String>();
    private ArrayList<String> dnAL = new ArrayList<String>();
    private ArrayList<String> mgrAL = new ArrayList<String>();
    private ArrayList<String> secAL = new ArrayList<String>();
    private ArrayList<String> userObjectClasses = new ArrayList<String>();
    private RandomAccessFile input;
    private RandomAccessFile output;
    Random rand = new Random();
    // This is the RFC inherited classes for inetOrgPerson
    private String[] objectClass = null;
    JPanel contentPane;
    JPanel jPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel jPanel3 = new JPanel();
    JButton jBRun = new JButton();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    JTextField jTFBaseDN = new JTextField();
    JComboBox<String> jCBType = new JComboBox<String>(directoryTypes);
    JRadioButton jRBOUs = new JRadioButton();
    JRadioButton jRBPeople = new JRadioButton();
    JTextField jTFInPut = new JTextField();
    JTextField jTFOutPut = new JTextField();
    JRadioButton jRBUseUID = new JRadioButton();
    JTextField jTFNumToGen = new JTextField();
    JLabel jLGen = new JLabel();
    JLabel jLDone = new JLabel();
    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea jTA = new JTextArea();
    JComboBox<String> jCBChangeType = new JComboBox<String>(changeTypes);
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JButton jBExit = new JButton();
    JButton jBHelp = new JButton();
    BrowserControl browser = new BrowserControl();
    BorderLayout borderLayout1 = new BorderLayout();
    JProgressBar PBar = new JProgressBar();

    /**
     * <p>
     * Generates LDIF files for importing into LDAP
     * </p>
     */
    public LDIFGenerator()
    {
	enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	try
	{
	    jbInit();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * <p>
     * Generates LDIF files for importing into LDAP
     * </p>
     * 
     * @param ldapBase
     *            - Appended to all entries generated
     */

    LDIFGenerator(String ldapBase)
    {
	// Allow passing in the Base for Generation
	jTFBaseDN.setText(ldapBase);
	enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	try
	{
	    jbInit();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /*
     * <p> Component initialization </p>
     * 
     * @throws Exception - when errors occur reading.
     */
    private void jbInit() throws Exception
    {
	String fileSep = File.separator;
	// setIconImage(Toolkit.getDefaultToolkit().createImage(FrameLDIFGen.class.getResource("[Your Icon]")));
	GridBagConstraints gridBagConstraints = new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 2, 3), 207, 0);
	gridBagConstraints.ipadx = 100;
	contentPane = (JPanel) this.getContentPane();
	contentPane.setLayout(borderLayout1);
	this.setPreferredSize(new Dimension(900, 500));
	// this.setResizable(false);
	// this.setSize(new Dimension(900, 500));
	this.setTitle("LDIF File Generator");
	jBRun.setText("Run");
	jBRun.addActionListener(new FrameLDIFGen_jBRun_actionAdapter(this));
	jPanel3.setLayout(gridBagLayout3);
	jTFBaseDN.setToolTipText("BaseDN where to Place Entries");
	if (jTFBaseDN.getText().length() < 2)
	{
	    jTFBaseDN.setText("dc=willeke, dc=com");
	}
	jRBOUs.setText("Generate OUs");
	jRBOUs.setFont(new java.awt.Font("Dialog", 1, 12));
	jRBOUs.setToolTipText("If selected, OU entries will be generated");
	jRBPeople.setText("Generate People");
	jRBPeople.setFont(new java.awt.Font("Dialog", 1, 12));
	jRBPeople.setToolTipText("If Selected, People Entries will be generated");
	jTFInPut.setToolTipText("The Directory that holds the Input Data for " + "Random Entries (Please add trailing \\");
	jTFInPut.setText(System.getProperties().getProperty("user.dir") + (fileSep + "data" + fileSep));
	jTFOutPut.setToolTipText("Output Path and File Name");
	jTFOutPut.setText(System.getProperties().getProperty("user.home") + fileSep + "output.ldif");
	jRBUseUID.setText("dn Uses uid vs cn");
	jRBUseUID.setFont(new java.awt.Font("Dialog", 1, 12));
	jRBUseUID.setToolTipText("If selected, Distinguished Name will use uid= vs cn=");
	jTFNumToGen.setToolTipText("Enter Number of Entires to Generate. Must be more than 10.");
	jTFNumToGen.setText("100");
	jTFNumToGen.setHorizontalAlignment(SwingConstants.TRAILING);
	jCBType.setMinimumSize(new Dimension(95, 21));
	jCBType.setToolTipText("Some server have Unique Requirements. Select Vendor of LDAP server.");
	jCBType.addActionListener(new FrameLDIFGen_jCBType_actionAdapter(this));
	jScrollPane1.setMinimumSize(new Dimension(24, 50));
	jScrollPane1.setPreferredSize(new Dimension(21, 21));
	jCBChangeType.setToolTipText("Select ChangeType For Records");
	jCBChangeType.addActionListener(new FrameLDIFGen_jCBChangeType_actionAdapter(this));
	jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
	jLabel1.setText("Base Added to Generated Records");
	jLabel2.setFont(new java.awt.Font("Dialog", 1, 12));
	jLabel2.setText("Directory Where Input Data is Stored");
	jLabel3.setFont(new java.awt.Font("Dialog", 1, 12));
	jLabel3.setText("File where records will be written.");
	jLabel4.setFont(new java.awt.Font("Dialog", 1, 12));
	jLabel4.setText("Number of Records");
	jPanel3.setBorder(BorderFactory.createRaisedBevelBorder());
	jBExit.setText("Exit");
	jBExit.addActionListener(new FrameLDIFGen_jBExit_actionAdapter(this));
	jBHelp.setToolTipText("Launches Browser to Online Help File");
	jBHelp.setText("Help");
	jBHelp.addActionListener(new FrameLDIFGen_jBHelp_actionAdapter(this));
	contentPane.setMinimumSize(new Dimension(0, 0));
	contentPane.setPreferredSize(new Dimension(640, 480));
	PBar.setFont(new java.awt.Font("Dialog", 0, 10));
	PBar.setPreferredSize(new Dimension(500, 20));
	PBar.setStringPainted(true);
	jTA.setLineWrap(true);
	jPanel2.add(PBar, null);
	jPanel2.add(jBRun, null);
	jPanel2.add(jBExit, null);
	jPanel2.add(jBHelp, null);
	contentPane.add(jPanel3, BorderLayout.CENTER);
	jPanel3.add(jRBPeople, new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 3, 2), 0, 0));
	jPanel3.add(jLGen, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel3.add(jLDone, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	jPanel3.add(jLabel1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(4, 3, 3, 2), 0, 0));
	jPanel3.add(jLabel2, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 3, 2), 0, 0));
	jPanel3.add(jRBUseUID, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 3, 2), 128, 0));
	jPanel3.add(jRBOUs, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 3, 2), 147, 0));
	jPanel3.add(jLabel4, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 3, 2), 34, 0));
	jPanel3.add(jCBChangeType, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 2, 3), 21, 0));
	jPanel3.add(jCBType, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 2, 3), 21, 0));
	jPanel3.add(jTFBaseDN, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 2, 3), 186, 0));
	jPanel3.add(jTFNumToGen, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 2, 3), 68, 0));
	jPanel3.add(jTFInPut, gridBagConstraints);
	jPanel3.add(jTFOutPut, new GridBagConstraints(2, 7, 1, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 2, 3), 206, 4));
	jPanel3.add(jLabel3, new GridBagConstraints(0, 7, 2, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 3, 3, 1), 0, 4));
	jPanel3.add(jScrollPane1, new GridBagConstraints(1, 9, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 3, 2, 3), 571, 103));
	contentPane.add(jPanel2, BorderLayout.SOUTH);
	jScrollPane1.getViewport().add(jTA, null);
	jRBOUs.setSelected(true);
	jRBPeople.setSelected(true);
	this.pack();
    }

    /** Overridden so we can exit when window is closed */
    /**
     * <p>
     * Overridden so we can exit when window is closed
     * </p>
     * 
     * @param e
     *            Window Event
     */

    protected void processWindowEvent(WindowEvent e)
    {
	super.processWindowEvent(e);
	// if (e.getID() == WindowEvent.WINDOW_CLOSING)
	// {
	// JOptionPane.showMessageDialog(this,"We Are here");
	// }
    }

    /**
     * <p>
     * Fill arrays to speed execution
     * </p>
     * 
     * @throws <code>IOException</code> - when errors occur reading file.
     */
    private void fillArrays()
    {
	fillThisArray(areaCodes, jTFInPut.getText() + "area-codes.txt");
	fillThisArray(employeeTypes, jTFInPut.getText() + "employee-types.txt");
	fillThisArray(familyNames, jTFInPut.getText() + "family-names.txt");
	fillThisArray(givenNames, jTFInPut.getText() + "given-names.txt");
	fillThisArray(localities, jTFInPut.getText() + "localities.txt");
	fillThisArray(mailHosts, jTFInPut.getText() + "mail-hosts.txt");
	fillThisArray(orgUnits, jTFInPut.getText() + "organizational-units.txt");
	fillThisArray(positions, jTFInPut.getText() + "positions.txt");
	fillThisArray(titleRanks, jTFInPut.getText() + "title-ranks.txt");
	fillThisArray(userObjectClasses, jTFInPut.getText() + "objectclasses.txt");
	// jTA.append("Arrays are Filled from: "+jTFInPut.getText());
    }

    /**
     * <p>
     * Fills a specific array
     * </p>
     * 
     * @param al
     *            - An Arraylist to fill
     * @param fileName
     *            - Name of file to fill into the Arraylist
     * @throws <code>IOException</code> - when errors occur reading.
     * @throws <code>EOFException</code> - when errors occur reading.
     */
    private void fillThisArray(ArrayList<String> al, String fileName)
    {
	al.clear();
	try
	{
	    input = new RandomAccessFile(fileName, "r");
	    String str = "fwefwef";
	    try
	    {
		while (!(str == null))
		{
		    str = input.readLine();
		    if (str == null)
		    {
			break;
		    }
		    // System.out.println(str);
		    al.add(str);
		}
		// jTA.append(getRand(al)+NL);
		// return al;
		try
		{
		    Thread.sleep(10);
		}
		catch (InterruptedException ex)
		{
		    System.out.println(ex);
		}
	    }
	    catch (EOFException EOFex)
	    {
		System.out.println(EOFex);
		input.close();
	    }
	}
	catch (IOException ioe)
	{
	    JOptionPane.showMessageDialog(this, "This file does not exist: " + NL + fileName + NL + ioe);
	}
    }

    /**
     * <p>
     * Gets a random entry from the arrayList provided
     * </p>
     * 
     * @param al
     *            the ArrayList to look for entry
     * @throws <code>IOException</code> - when errors occur reading.
     * @return String of Entry from ArrayList
     */

    private String getRand(ArrayList<String> al)
    {
	try
	{
	    // System.out.println("Size ="+al.size());
	    int i = com.willeke.utility.RandConvUtility.randInt(1, al.size() - 1);
	    return (String) al.get(i);

	}
	catch (IndexOutOfBoundsException ex)
	{
	    jTA.setText("");
	    jTA.append("Sorry, an Error has occurred. Please Close and Restart.");
	    ex.printStackTrace();
	    return null;
	}
    }

    /**
     * <p>
     * Main Thread of Program
     * </p>
     * 
     * @throws <code>IOException</code> - when errors occur Writing File.
     */

    void dbGen()
    {
	timer.reset();
	String fn;
	String gn;
	String ou;
	String ac;
	String loc;
	String uid;
	String dn;
	String str;
	baseDN = jTFBaseDN.getText();
	// Need BaseDN, People? OU?, NUM, File for Out, Dir for In, Server Type
	// Would like an Counter to tell how far along we are. If we are generating
	// 1,000,000 it takes so long. Would like to know at each 100 or 1000 entries
	PBar.setMaximum(NUMBER_TO_GENERATE);
	PBar.setMinimum(0);
	PBar.setValue(0);

	try
	{
	    output = new RandomAccessFile(jTFOutPut.getText(), "rw");
	    output.writeBytes("version: 1" + NL);
	    if (jRBOUs.isSelected())
	    { // Generate OrganizationalUnits ?
		for (int i = 0; i < orgUnits.size(); i++)
		{
		    output.writeBytes(NL + "dn: ou=" + orgUnits.get(i) + "," + baseDN);
		    if (!(jCBChangeType.getSelectedItem() == "None"))
		    {
			output.writeBytes(NL + "changetype: " + jCBChangeType.getSelectedItem());
		    }
		    output.writeBytes(NL + "ou: " + orgUnits.get(i));
		    output.writeBytes(NL + "objectClass: top" + NL + "objectClass: organizationalUnit");
		    if (jCBType.getSelectedItem() == EDIRECTORY)
		    {
			output.writeBytes(NL + "objectClass: ndsLoginProperties" + NL + "objectClass: ndsContainerLoginProperties");
		    }
		    output.writeBytes(NL);
		}
	    }

	    if (jRBPeople.isSelected())
	    {
		objectClass = userObjectClasses.toArray(new String[userObjectClasses.size()]);
		for (int x = 1; x <= NUMBER_TO_GENERATE; x++)
		{
		    // The Manager and Sec entries do not work well with less than 10
		    if ((x % 10 == 0) || (x == NUMBER_TO_GENERATE))
		    { // Update ProgressBar every 10 entries
			PBar.setValue(x);
			PBar.update(PBar.getGraphics());
		    }
		    // Get the random items for this Entry that we use in other things
		    fn = getRand(familyNames);
		    gn = getRand(givenNames);
		    ou = getRand(orgUnits);
		    ac = getRand(areaCodes);
		    loc = getRand(localities);
		    // Generate UID based on Name
		    if (fn.length() >= 7)
		    {
			uid = fn.substring(0, 7) + gn.substring(0, 1);
		    }
		    else
		    {
			uid = fn.substring(0, fn.length()) + gn.substring(0, 1);
			// Create DN
		    }
		    // When not generating OUs, put them in the base.
		    if (jRBOUs.isSelected())
		    {
			dn = "cn=" + gn + " " + fn + ",ou=" + ou + "," + baseDN;
		    }
		    else
		    {
			dn = "cn=" + gn + " " + fn + "," + baseDN;
		    }
		    // Check to see dn already exists
		    if (dnAL.contains(dn))
		    { // Duplicate entry decrement Counter and continue so we try again
			x = x - 1;
			System.out.println("Dup: " + dn);
			continue;
		    }
		    else
		    {
			dnAL.add(dn);
			output.writeBytes(NL + "dn: " + dn);
			if (!(jCBChangeType.getSelectedItem() == "None"))
			{
			    output.writeBytes(NL + "changetype: " + jCBChangeType.getSelectedItem());
			}
			// ObjectClass Entries
			for (int i = 0; i < objectClass.length; i++)
			{
			    output.writeBytes(NL + "objectClass: " + objectClass[i]);
			}
			output.writeBytes(NL + "cn: " + gn + " " + fn);
			output.writeBytes(NL + "sn: " + fn);
			if (jCBType.getSelectedItem() == MAD)
			{
			    output.writeBytes(NL + "sAMAccountName: " + uid);
			}
			output.writeBytes(NL + "description: This is " + gn + " " + fn + "'s description");
			output.writeBytes(NL + "facsimileTelephoneNumber: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999) + "-"
				+ com.willeke.utility.RandConvUtility.randInt(1000, 9999));
			output.writeBytes(NL + "l: " + loc);
			output.writeBytes(NL + "ou: " + ou);
			output.writeBytes(NL + "postalAddress: " + ou + "$" + loc);
			output.writeBytes(NL + "telephoneNumber: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999) + "-"
				+ com.willeke.utility.RandConvUtility.randInt(1000, 9999));
			output.writeBytes(NL + "title: " + getRand(titleRanks) + " " + ou + " " + getRand(positions));
			// Password Same as family Name
			// output.writeBytes(NL + "userPassword: " + fn);
			// Password Same as family Name
			output.writeBytes(NL + "userPassword: " + "Password1");

			output.writeBytes(NL + "uid: " + uid);
			output.writeBytes(NL + "givenName: " + gn);
			output.writeBytes(NL + "mail: " + uid + "@" + getRand(mailHosts) + ".com");
			output.writeBytes(NL + "carLicense: " + com.willeke.utility.RandConvUtility.randASCII(6));
			output.writeBytes(NL + "departmentNumber: " + com.willeke.utility.RandConvUtility.randInt(1000, 9999));
			output.writeBytes(NL + "employeeType: " + getRand(employeeTypes));
			output.writeBytes(NL + "homePhone: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999) + "-"
				+ com.willeke.utility.RandConvUtility.randInt(1000, 9999));
			output.writeBytes(NL + "initials: " + gn.substring(0, 1) + ". " + fn.substring(0, 1) + ".");
			output.writeBytes(NL + "mobile: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999) + "-"
				+ com.willeke.utility.RandConvUtility.randInt(1000, 9999));
			output.writeBytes(NL + "pager: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999) + "-"
				+ com.willeke.utility.RandConvUtility.randInt(1000, 9999));
			output.writeBytes(NL + "roomNumber: " + com.willeke.utility.RandConvUtility.randInt(8000, 9999));
			// Next we will guarantee we have at least One Manager
			if (x == 1)
			{
			    mgrAL.add(dn);
			}
			// Next we will guarantee we have at least One secretary
			if (x == 2)
			{
			    secAL.add(dn);
			}
			if (x < (NUMBER_TO_GENERATE / orgUnits.size()))
			{ // Based on how many entries, we generate multiple managers and sec
			    if (dnAL.size() > 1)
			    { // Sec
				str = getRand(dnAL);
				if ((!(dn == str)) && (!(mgrAL.contains(str))))
				{ // if str is not a MGR and this is not the same DN Make a Sec!
				    output.writeBytes(NL + "secretary: " + str);
				    if (!(secAL.contains(str)))
				    { // add to sec array
					secAL.add(str);
				    }
				}
				str = getRand(dnAL);
				if ((!(dn == str)) && (!(secAL.contains(str))))
				{
				    output.writeBytes(NL + "manager: " + str);
				    if (!(mgrAL.contains(str)))
				    {
					mgrAL.add(str);
				    }
				}
				else
				{ // Every one but first should have manager so
				    output.writeBytes(NL + "manager: " + mgrAL.get(0));
				}
			    }
			}
			else
			{ // We shave enough managers & Sec
			    if ((!(mgrAL.contains(dn))) && (!(secAL.contains(dn))))
			    {
				output.writeBytes(NL + "manager: " + getRand(mgrAL));
			    }
			    if ((!(secAL.contains(dn))))
			    {
				output.writeBytes(NL + "secretary: " + getRand(secAL));
			    }
			}
			output.writeBytes(" " + NL);
		    } // Duplicate??
		} // for x
	    } // if jRBPeople
	    output.close();
	}
	catch (IOException ioe)
	{
	    System.out.println("File not Accessable: " + jTFOutPut.getText() + NL + ioe);
	}
	str = "Generating " + NUMBER_TO_GENERATE + " entries.";
	if (jRBPeople.isSelected())
	{
	    str = str + NL + "With People entries";
	}
	else
	{
	    str = str + NL + "Without People entries";
	}
	if (jRBOUs.isSelected())
	{
	    str = str + "With OUs entries";
	}
	else
	{
	    str = str + "Without OUs entries";
	}
	if (jRBUseUID.isSelected())
	{
	    str = str + NL + "Using UID in Distinguished Name";
	}
	else
	{
	    str = str + NL + "Using CN in Distinguished Name";
	}
	str = str + "In the " + jTFBaseDN.getText() + " context";
	str = str + NL + "For the " + jCBType.getSelectedItem() + " Server Type";
	jTA.append(str + NL);
	long time = timer.elapsed();
	jTA.append("Generating " + NUMBER_TO_GENERATE + " entries took ");
	if (time < 1000)
	{
	    jTA.append(time + " milliseconds");
	}
	else
	{
	    jTA.append(timer.getHMS(time));
	}
    } // dbGEN

    /**
     * Check to see if a file already exist
     * 
     * @param fileName
     *            - See if a File Exists
     */
    public void checkFile(File fileName)
    {
	// Check to see if file exists and deal with it!
	if (fileName.exists())
	{
	    jTA.append("File: " + fileName + " Already Exist: ");
	    JOptionPane pop = new JOptionPane("File Already Exist! OverWrite?", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	    JDialog dlg = pop.createDialog(this, "Please");
	    dlg.setVisible(true);
	    Object answer = pop.getValue();
	    if (answer instanceof Integer)
	    {
		if (((Integer) answer).intValue() == JOptionPane.OK_OPTION)
		{
		    fileName.delete();
		    jTA.append("Overwritten");
		}
		else
		{
		    jTA.setForeground(Color.red);
		    jTA.append("Warning, File Appended");
		    jTA.setForeground(Color.black);
		}
	    }
	    else
	    {
		jTA.setForeground(Color.red);
		jTA.append("Warning, File Appended");
		jTA.setForeground(Color.black);
	    }
	    jTA.append(NL);
	} // Exists?
    }

    /**
     * <p>
     * Action method for Button Push
     * </p>
     * 
     * @param e
     *            - Actions from Button
     */
    void jBRun_actionPerformed(ActionEvent e)
    {
	jTA.setText("");
	if (NUMBER_TO_GENERATE < 11)
	{
	    JOptionPane.showMessageDialog(this, "Must generate more than 10 Entries!" + NL + "you only have " + NUMBER_TO_GENERATE);
	}
	else if (jRBPeople.isSelected())
	{
	    NUMBER_TO_GENERATE = Integer.parseInt(jTFNumToGen.getText());
	}
	else
	{
	    NUMBER_TO_GENERATE = 0;

	}
	jTA.append("You are running Version: " + VERSION + "\n");
	// need to check if file exist
	checkFile(new File(jTFOutPut.getText()));
	if (jRBOUs.isSelected() || jRBPeople.isSelected())
	{
	    this.update(this.getGraphics());
	    fillArrays();
	    dbGen();
	}
	else
	{
	    JOptionPane.showMessageDialog(this, "Must generate either OUs or People to Get Anything!");
	}
    }

    void jCBType_actionPerformed(ActionEvent e)
    {
    }

    void jCBChangeType_actionPerformed(ActionEvent e)
    {
    }

    void jBExit_actionPerformed(ActionEvent e)
    {
	this.dispose();
    }

    /**
     * <p>
     * Launches default Browser and goes to URL provided.
     * </p>
     * 
     * @param e
     *            Event from Component
     * @throws <code>IOException</code> - when errors occur reading.
     */
    void jBHelp_actionPerformed(ActionEvent e)
    {
	BrowserControl.displayURL("https://ldapwiki.com/wiki/LDIF%20Generator");
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	LDIFGenerator frame = new LDIFGenerator();
	// Validate frames that have preset sizes
	// Pack frames that have useful preferred size info, e.g. from their layout
	// frame.pack();
	frame.validate();

	// Center the window
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension frameSize = frame.getSize();
	if (frameSize.height > screenSize.height)
	{
	    frameSize.height = screenSize.height;
	}
	if (frameSize.width > screenSize.width)
	{
	    frameSize.width = screenSize.width;
	}
	frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	frame.setVisible(true);
    }
}

class FrameLDIFGen_jBRun_actionAdapter implements java.awt.event.ActionListener
{
    LDIFGenerator adaptee;

    FrameLDIFGen_jBRun_actionAdapter(LDIFGenerator adaptee)
    {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
	adaptee.jBRun_actionPerformed(e);
    }
}

class FrameLDIFGen_jCBType_actionAdapter implements java.awt.event.ActionListener
{
    LDIFGenerator adaptee;

    FrameLDIFGen_jCBType_actionAdapter(LDIFGenerator adaptee)
    {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
	adaptee.jCBType_actionPerformed(e);
    }
}

class FrameLDIFGen_jCBChangeType_actionAdapter implements java.awt.event.ActionListener
{
    LDIFGenerator adaptee;

    FrameLDIFGen_jCBChangeType_actionAdapter(LDIFGenerator adaptee)
    {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
	adaptee.jCBChangeType_actionPerformed(e);
    }
}

class FrameLDIFGen_jBExit_actionAdapter implements java.awt.event.ActionListener
{
    LDIFGenerator adaptee;

    FrameLDIFGen_jBExit_actionAdapter(LDIFGenerator adaptee)
    {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
	adaptee.jBExit_actionPerformed(e);
    }
}

class FrameLDIFGen_jBHelp_actionAdapter implements java.awt.event.ActionListener
{
    LDIFGenerator adaptee;

    FrameLDIFGen_jBHelp_actionAdapter(LDIFGenerator adaptee)
    {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
	adaptee.jBHelp_actionPerformed(e);
    }

}
