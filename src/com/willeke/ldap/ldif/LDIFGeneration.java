package com.willeke.ldap.ldif;

/**
 * Program will generate LDIF entries based on values within files.
 */

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;

/**
 * Original code donated to apache.org in early 2013.
 * 
 * @author jim@willeke.com
 * @version 2013-12-12-11:21:13
 */
public class LDIFGeneration {
	private static final String BUILD = "2013-12-12-11:21:13";
	private static final String CLASSNAME = LDIFGeneration.class.getName();
	static org.apache.log4j.Logger log = LogManager.getLogger(CLASSNAME);// DumpPasswordInformation.class.getName());
	private static ArrayList<String> orgUnits = new ArrayList<String>();
	private static ArrayList<String> areaCodes = new ArrayList<String>();
	private static ArrayList<String> employeeTypes = new ArrayList<String>();
	private static ArrayList<String> familyNames = new ArrayList<String>();
	private static ArrayList<String> givenNames = new ArrayList<String>();
	private static ArrayList<String> localities = new ArrayList<String>();
	private static ArrayList<String> mailHosts = new ArrayList<String>();
	private static ArrayList<String> positions = new ArrayList<String>();
	private static ArrayList<String> titleRanks = new ArrayList<String>();
	private static List<String> defaultObjectClasses = new ArrayList<String>();
	private static final ArrayList<String> possibleChangeTypes = new ArrayList<String>();
	private static final ArrayList<String> possibleDirectoryServerTypes = new ArrayList<String>();
	private static final String NL = System.getProperties().getProperty("line.separator");
	private static RandomAccessFile input;

	private static String dataPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "Data";
	private static String baseDN = "ou=people,dc=willeke,dc=com";
	private static String outPutFilename = System.getProperty("user.home") + System.getProperty("file.separator")
			+ "ldifgen.ldif";
	private static String[] objectClass;
	private static String changeType = "None";
	private static String serverType = "Generic"; // GENERIC, MAD, EDIRECTORY 
	private static boolean isPeople = true;
	private static boolean isCreateOUs = true;
	private static boolean isUidNaming = true;
	private static boolean useShortNames = true;
	private static boolean overWriteExitingOutput = true;
	private static boolean toLowerCase = true;
	private static int shortNameLength = 8;
	private static int numberOfEntries = 11;
		

	private static void showHelp() {
		log.info("The Defaults are: (All of these can be changed!) BUILD: " + BUILD);
		log.info("Path to Data Repository: " + dataPath);
		log.info("Base Container DN to create Users: " + baseDN);
		log.info("Output File Path: " + outPutFilename);
		log.info("ObjectClass Types:" + defaultObjectClasses);
		log.info("changeType:" + changeType);
		log.info("   Possible types are: " + possibleChangeTypes);
		log.info("serverType:" + serverType);
		log.info("   Possible types are: " + possibleDirectoryServerTypes);
		log.info("Generate People Entries: " + isPeople);
		log.info("Generate OU Entries:" + isCreateOUs);
		log.info("Use uid for Naming Values: " + isUidNaming);
		log.info("Use short names for Naming Values: " + useShortNames);
		log.info("Over-Write Exiting Output File: " + overWriteExitingOutput);
		log.info("Shift Short Name Values to lowercase: " + toLowerCase);
		log.info("Short Name Value Length: " + shortNameLength);
		log.info("Number Of Entries to Generate: " + numberOfEntries);
		log.info("Sample Command Lines:");
		log.info("java -jar " + CLASSNAME + " help\n");
		log.info("java -jar " + CLASSNAME + " baseDN  numberOfEntries\n");
		log.info("java -jar " + CLASSNAME + " baseDN  serverType numberOfEntries\n");
		log.info("java -jar " + CLASSNAME
				+ " dataPath baseDN outPutFilename objectClass changeType serverType isPeople isCreateOUs isUserUid useShortNames toLowerCase overWriteExitingOutput shortNameLength numberOfEntries\n");
	}

	// dataPath, baseDN, outPutFilename, objectClass, changeType, serverType,
	// isPeople, isCreateOUs, isUserUid, useShortNames, overWriteExitingOutput,
	// toLowerCase, shortNameLength, numberOfEntrie

	public static void generateLDIF(String help) {
		fillFixedArrays();
		showHelp();
	}

	public static void generateLDIF(String baseDN, int numberOfEntries) {
		fillFixedArrays();
		generateLDIF(dataPath, baseDN, outPutFilename, objectClass, changeType, serverType, isPeople, isCreateOUs,
				isUidNaming, useShortNames, toLowerCase, overWriteExitingOutput, shortNameLength, numberOfEntries);
	}

	public static void generateLDIF(String baseDN, int numberOfEntries, String serverType) {
		fillFixedArrays();
		generateLDIF(dataPath, baseDN, outPutFilename, objectClass, changeType, serverType, isPeople, isCreateOUs,
				isUidNaming, useShortNames, toLowerCase, overWriteExitingOutput, shortNameLength, numberOfEntries);
	}

	public static void generateLDIF(String dataPath, String baseDN, String outPutFilename, String[] objectClass,
			String changeType, String serverType, boolean isPeople, boolean isCreateOUs, boolean isUserUid,
			boolean useShortNames, boolean toLowerCase, boolean overWriteExitingOutput, int shortNameLength,
			int numberOfEntries) {
		fillFixedArrays();
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		File outPutFile = new File(outPutFilename);
		if (outPutFile.exists()) {
			try {
				log.warn("Overwriting file at: " + outPutFile.getCanonicalPath() + " Exists!");
			} catch (IOException e) {
				log.error("Could not initialise file: " + outPutFilename + "\n" + e);
				System.exit(1);
			}
			if (overWriteExitingOutput) {// delete file set no append.
				outPutFile.delete();
				try {
					fileWriter = new FileWriter(outPutFilename, false);
				} catch (IOException e) {
					log.error("Could not initialise fileWriter for file: " + outPutFilename + "\n" + e);
					System.exit(1);
				}
			} else {// Append to file
				try {
					fileWriter = new FileWriter(outPutFilename, true);
				} catch (IOException e) {
					log.error("Could not initialise fileWriter for file: " + outPutFilename + "\n" + e);
					System.exit(1);
				}
			}
		} else {// file does not exists
			try {
				fileWriter = new FileWriter(outPutFilename, false);
			} catch (IOException e) {
				log.error("Could not initialise fileWriter for file: " + outPutFilename + "\n" + e);
				System.exit(1);
			}
		}
		bufferedWriter = new BufferedWriter(fileWriter);
		log.debug("output file is: " + outPutFilename);
		init(dataPath);
		// RandomAccessFile outputFile = null;
		if (changeType.equalsIgnoreCase("None")) {
			changeType = "";
		} else {
			verifySomeInputs(changeType, possibleChangeTypes);
		}
		verifySomeInputs(serverType, possibleDirectoryServerTypes);
		dbGen(baseDN, bufferedWriter, objectClass, serverType, serverType, isPeople, isCreateOUs, isUserUid,
				useShortNames, overWriteExitingOutput, shortNameLength, numberOfEntries);
	}

	/**
	 * @param dataPath
	 */
	private static void init(String dataPath) {

		fillFixedArrays();
		fillArrays(dataPath);

	}

	/**
	 * @param changeType
	 * @param checkCollection
	 */
	private static void verifySomeInputs(String checkValue, Collection<String> checkCollection) {
		log.debug("CheckValue: " + checkValue);
		if (!checkCollection.contains(checkValue)) {
			log.error("Change type should be one of the following:");
			for (Iterator<String> iterator = checkCollection.iterator(); iterator.hasNext();) {
				log.error(iterator.next());
			}
		}
	}

	private static void fillFixedArrays() {
		possibleChangeTypes.clear();
		possibleChangeTypes.add("None");
		possibleChangeTypes.add("add");
		possibleChangeTypes.add("delete");
		possibleChangeTypes.add("modify");
		possibleChangeTypes.add("moddn");
		possibleChangeTypes.add("modrdn");
		possibleDirectoryServerTypes.clear();
		possibleDirectoryServerTypes.add("Generic");
		possibleDirectoryServerTypes.add("Active Directory");
		possibleDirectoryServerTypes.add("eDirectory");
		defaultObjectClasses.clear();
		defaultObjectClasses.add("top");
		defaultObjectClasses.add("person");
		defaultObjectClasses.add("organizationalPerson");
		defaultObjectClasses.add("inetOrgPerson");
		// objectClass = new String[defaultObjectClasses.size()];
		objectClass = (String[]) defaultObjectClasses.toArray(new String[defaultObjectClasses.size()]);
	}

	/**
	 * Where we do the file generation
	 * 
	 * @param baseDN
	 * @param bufferedWriter
	 * @param objectClass
	 * @param changeType
	 * @param serverType
	 * @param isPeople
	 * @param isCreateOUs
	 * @param isUserUid
	 * @param useShortNames
	 * @param overWriteExitingOutput
	 * @param shortNameLength
	 * @param numberOfEntries
	 */
	public static void dbGen(String baseDN, BufferedWriter bufferedWriter, String[] objectClass, String changeType,
			String serverType, boolean isPeople, boolean isCreateOUs, boolean isUserUid, boolean useShortNames,
			boolean overWriteExitingOutput, int shortNameLength, int numberOfEntries) {

		ArrayList<String> dnAL = new ArrayList<String>(); // dnAl is all the dns we have generated so far.
		ArrayList<String> mgrAL = new ArrayList<String>(); // mgrAL is all the managers dns we have so far
		ArrayList<String> secAL = new ArrayList<String>(); // secAL is all the secretaries we have so far.
		String str;
		// Need BaseDN, People? OU?, NUM, File for Out, Dir for In, Server Type

		try {
			bufferedWriter.write("version: 1" + NL);
			if (isCreateOUs) { // Generate OrganizationalUnits ?
				for (int i = 0; i < orgUnits.size(); i++) {
					bufferedWriter.write(NL + "dn: ou=" + orgUnits.get(i) + "," + baseDN);
					log.debug("Adding OU (" + i + 1 + "): " + "dn: ou=" + orgUnits.get(i) + "," + baseDN);
					if (!(changeType.isEmpty())) {
						bufferedWriter.write(NL + "changetype: " + changeType);
					}
					bufferedWriter.write(NL + "ou: " + orgUnits.get(i));
					bufferedWriter.write(NL + "objectClass: top" + NL + "objectClass: organizationalUnit");
					if (serverType.equalsIgnoreCase("edirectory")) {
						bufferedWriter.write(NL + "objectClass: ndsLoginProperties" + NL
								+ "objectClass: ndsContainerLoginProperties");
					}
					bufferedWriter.write(NL);
				}
			}
			// If we are doing people then do this part
			if (isPeople) {
				log.debug("Number of People to Generate: " + numberOfEntries);
				for (int userCount = 1; userCount <= numberOfEntries; userCount++) {
					// Would like an Counter to tell how far along we are. If we are generating
					// 1,000,000 it takes so long. Would like to know at each 100 or 1000 entries
					// The Manager and Sec entries do not work well with less than 10
					if ((userCount % 10 == 0) || (userCount == numberOfEntries)) { // Update ProgressBar every 10
																					// entries
																					// PBar.setValue(x);
																					// PBar.update(PBar.getGraphics());
					}
					// Get the random items for this Entry that we use in other things
					String sn = getRand(familyNames);
					String gn = getRand(givenNames);
					String ou = getRand(orgUnits);
					String ac = getRand(areaCodes);
					String loc = getRand(localities);
					String displayName = generateDisplayName(sn, gn);
					String shortName = generateShortName(sn, gn, shortNameLength, true, userCount);
					String dn = generateDn(baseDN, isUserUid, useShortNames, displayName, shortName, ou);

					// Check to see dn already exists
					if (dnAL.contains(dn)) { // Duplicate entry increment Counter and loop so we try again
						userCount = userCount - 1;
						log.debug("Duplicate (Skipping): " + dn);
					} else {
						dnAL.add(dn);
						log.debug("Adding User (" + userCount + "): " + "dn: " + dn);
						bufferedWriter.write(NL + "# Entry: (" + userCount + ") " + dn);
						bufferedWriter.write(NL + "dn: " + dn);
						if (!(changeType.isEmpty())) {
							bufferedWriter.write(NL + "changetype: " + changeType);
						}
						// Write out all the ObjectClass Entries
						for (int i = 0; i < objectClass.length; i++) {
							bufferedWriter.write(NL + "objectClass: " + objectClass[i]);
						}
						if (isUserUid) {// we are naming as uid=
							if (useShortNames) {// we are using uid as naming so set a CN value as a shortName changed
												// 2013-12-11 to work with AD.
								bufferedWriter.write(NL + "cn: " + shortName);
							} else {// we are using uid as naming so set a CN value as a displayName
								bufferedWriter.write(NL + "cn: " + displayName);
							}
						} else {// using CN as naming value so do not put in LDIF
							if (useShortNames) {// we are using uid as naming so set a CN value as a shortName changed
												// 2013-12-11 to work with AD.
								bufferedWriter.write(NL + "uid: " + shortName);
							} else {// we are using uid as naming so set a CN value as a displayName
								bufferedWriter.write(NL + "uid: " + displayName);
							}
						}
						bufferedWriter.write(NL + "sn: " + sn);
						if (serverType.equalsIgnoreCase("Active Directory")) {
							bufferedWriter.write(NL + "sAMAccountName: " + shortName.toUpperCase());
							bufferedWriter.write(NL + "userPrincipalName: " + shortName.toUpperCase() + "@" + baseDN);
						}
						bufferedWriter.write(NL + "description: This is " + gn + " " + sn + "'s description");
						bufferedWriter.write(NL + "facsimileTelephoneNumber: +1 " + ac + " "
								+ com.willeke.utility.RandConvUtility.randInt(100, 999) + "-"
								+ com.willeke.utility.RandConvUtility.randInt(1000, 9999));
						bufferedWriter.write(NL + "l: " + loc);
						bufferedWriter.write(NL + "ou: " + ou);
						bufferedWriter.write(NL + "postalAddress: " + ou + "$" + loc);
						bufferedWriter.write(NL + "telephoneNumber: +1 " + ac + " "
								+ com.willeke.utility.RandConvUtility.randInt(100, 999) + "-"
								+ com.willeke.utility.RandConvUtility.randInt(1000, 9999));
						bufferedWriter
								.write(NL + "title: " + getRand(titleRanks) + " " + ou + " " + getRand(positions));
						// Password Same as family Name
						bufferedWriter.write(NL + "userPassword: " + sn);

						bufferedWriter.write(NL + "givenName: " + gn);
						bufferedWriter.write(NL + "mail: " + shortName + "@" + getRand(mailHosts) + ".com");
						bufferedWriter.write(NL + "carLicense: " + com.willeke.utility.RandConvUtility.randASCII(6));
						bufferedWriter.write(
								NL + "departmentNumber: " + com.willeke.utility.RandConvUtility.randInt(1000, 9999));
						bufferedWriter.write(NL + "employeeType: " + getRand(employeeTypes));
						bufferedWriter.write(
								NL + "homePhone: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999)
										+ "-" + com.willeke.utility.RandConvUtility.randInt(1000, 9999));
						bufferedWriter.write(NL + "initials: " + gn.substring(0, 1) + sn.substring(0, 1));
						bufferedWriter.write(
								NL + "mobile: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999)
										+ "-" + com.willeke.utility.RandConvUtility.randInt(1000, 9999));
						bufferedWriter.write(
								NL + "pager: +1 " + ac + " " + com.willeke.utility.RandConvUtility.randInt(100, 999)
										+ "-" + com.willeke.utility.RandConvUtility.randInt(1000, 9999));
						bufferedWriter
								.write(NL + "roomNumber: " + com.willeke.utility.RandConvUtility.randInt(8000, 9999));

						// Next we will guarantee we have at least One Manager
						if (userCount == 1) {
							log.debug("Making Entry a Manager: " + dn);
							mgrAL.add(dn);
						}
						// Next we will guarantee we have at least One secretary
						if (userCount == 2) {
							log.debug("Making Entry a Secretary: " + dn);
							secAL.add(dn);
						}
						/**
						 * It is not feasible to use random managers if we are generating less than 10
						 * entries.
						 */
						if (numberOfEntries >= 10) {
							if (userCount + 1 < (numberOfEntries / orgUnits.size())) { // Based on how many entries, we
																						// generate multiple managers
																						// and sec
								if (dnAL.size() > 2) { // secretary
									String testDN = getRand(dnAL);
									if ((!(dn.equalsIgnoreCase(testDN)) && (!(mgrAL.contains(testDN))))) { // if dn is
																											// not a MGR
																											// and this
																											// is not
																											// the same
																											// DN Make a
																											// secretary!
										bufferedWriter.write(NL + "secretary: " + testDN);
										if (!(secAL.contains(testDN))) { // add to secretary array
											log.debug("Making Entry a Secretary: " + dn);
											secAL.add(dn);
										}
									}
									testDN = getRand(dnAL);
									if ((!(dn.equalsIgnoreCase(testDN))) && (!(secAL.contains(testDN)))) {
										bufferedWriter.write(NL + "manager: " + testDN);
										if (!(mgrAL.contains(testDN))) {
											log.debug("Making Entry a Manager: " + dn);
											mgrAL.add(dn);
										}
									} else { // Every one but first should have manager so
										bufferedWriter.write(NL + "manager: " + mgrAL.get(0));
									}
								} // secretary
							} else { // We should have enough managers & Sec
								if ((!(mgrAL.contains(dn))) && (!(secAL.contains(dn))) && mgrAL.size() >= 1) {
									String manager = getRand(mgrAL);
									log.debug("manager: " + manager);
									bufferedWriter.write(NL + "manager: " + manager);
								}
								if (!(secAL.contains(dn)) && secAL.size() >= 1) {
									String secretary = getRand(secAL);
									log.debug("secretary: " + secretary);
									bufferedWriter.write(NL + "secretary: " + secretary);
								}
							} // got enough mgrs/sec
						} else {
							if (userCount > 2) {// we should have at least one of each
								String manager = mgrAL.get(0);
								if (manager != null) {
									log.debug("manager: " + manager);
									bufferedWriter.write(NL + "manager: " + manager);
								} else {
									log.warn("manager was null!");
								}
								String secretary = secAL.get(0);
								if (secretary != null) {
									log.debug("secretary: " + secretary);
									bufferedWriter.write(NL + "secretary: " + secretary);
								} else {
									log.warn("secretary was null!");
								}
							} else {
								log.warn("No manager or secretary available with small user counts!");
							}

						} // end of numberOfEntries>=10
							// bufferedWriter.write(" " + NL); dropped the " " on the last line 2013-12-11
						bufferedWriter.write(NL);
					} // Duplicate??
				} // for x
				log.debug(
						"Finished Generating: " + numberOfEntries + " User Entries Output file is: " + outPutFilename);
			} // if jRBPeople
			bufferedWriter.close();
		} catch (IOException ioe) {
			log.error("File not Accessable: " + bufferedWriter + NL + ioe);
		}
		str = "Generating " + numberOfEntries + " entries.";
		if (isPeople) {
			str = str + NL + "With People entries";
		} else {
			str = str + NL + "Without People entries";
		}
		if (isCreateOUs) {
			str = str + "With OUs entries";
		} else {
			str = str + "Without OUs entries";
		}
		if (isUserUid) {
			str = str + NL + "Using UID in Distinguished Name";
		} else {
			str = str + NL + "Using CN in Distinguished Name";
		}
		str = str + "In the " + baseDN + " context";
		str = str + NL + "For the " + serverType + " Server Type";
		str = str + NL;
	}// dbGEN

	/**
	 * @param baseDN
	 * @param isUserUid
	 * @param sn
	 * @param gn
	 * @param ou
	 * @return
	 */
	private static String generateDn(String baseDN, boolean isUserUid, boolean useShortNames, String displayName,
			String shortName, String ou) {
		String dn = null;
		if (useShortNames) {
			if (isUserUid) {// Generate dn based on shortName && uid
				dn = "uid=" + shortName + ",ou=" + ou + "," + baseDN;
			} else {// Generate dn based on shortName && cn
				dn = "cn=" + shortName + ",ou=" + ou + "," + baseDN;
			}
		} else {// Generate dn based on Full Name && cn
			if (isUserUid) {// Generate dn based on shortName && uid
				dn = "uid=" + displayName + ",ou=" + ou + "," + baseDN;
			} else {// Generate dn based on shortName && cn
				dn = "cn=" + displayName + ",ou=" + ou + "," + baseDN;
			}
		}
		return dn;
	}

	private static String generateDisplayName(String sn, String gn) {
		return gn + " " + sn;
	}

	/**
	 * Issues with too many collisions when shortNameLength is 8 
	 * When we make shortNameLength longer, we need to be able to pad the values.
	 * @param sn
	 * @param gn
	 * @return
	 */
	private static String generateShortName(String sn, String gn, int shortNameLength, boolean toLowerCase, int userCount) {
		String shortName;
		if (sn.length() >= shortNameLength) {
			shortName = sn.substring(0, shortNameLength) + gn.substring(0, 1);
		} else {
			shortName = sn.substring(0, sn.length()) + gn.substring(0, 1);
		}
		if (shortName.length() < shortNameLength)
		{
			int countPad = String.valueOf(userCount).length();
			shortName = padRight(shortName, shortNameLength-countPad) +userCount;
		}
		if (toLowerCase) {
			return shortName.toLowerCase();
		} else {
			return shortName;
		}
	}

	  // pad with " " to the right to the given length (n)
	  static String padRight(String s, int n) {
		 return String.format("%1$-" + n + "s", s).replace(' ', '0');
	  }
	
	/**
	 * Check to see if a file already exist
	 * 
	 * @param fileName
	 *            - See if a File Exists
	 */
	public void checkFile(File fileName) {
		// Check to see if file exists and deal with it!
		if (fileName.exists()) {
			System.out.println("File: " + fileName + " Already Exist: ");

			// JOptionPane pop = new JOptionPane("File Already Exist! OverWrite?",
			// JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			// JDialog dlg = pop.createDialog(this, "Please");
			// dlg.setVisible(true);
			// Object answer = pop.getValue();
			String answer = "Yes";
			if (answer.equalsIgnoreCase("Yes")) {
				fileName.delete();
				System.out.println("Overwritten");
			} else {
				System.out.println("Warning, File Appended");
			}
		} else {
			System.out.println("Warning, File Appended");
		}
		System.out.println(NL);
	} // Exists?

	/**
	 * <p>
	 * Gets a random entry from the arrayList provided
	 * </p>
	 * 
	 * @param al
	 *            the ArrayList to look for entry
	 * @throws <code>IOException</code>
	 *             - when errors occur reading.
	 * @return String of Entry from ArrayList
	 */
	private static String getRand(ArrayList<String> al) {
		try {
			if (al.size() <= 1) {
				log.debug("al.size:" + al.size());
			}
			int i = com.willeke.utility.RandConvUtility.getIntInRange(1, (al.size() - 1));
			i = i - 1;
			if (i <= 0) {
				i = 0;
			}
			// log.debug("Size ="+al.size() + " Random Value derived: "+ i);
			return (String) al.get(i);
		} catch (IndexOutOfBoundsException ex) {
			log.error("Sorry, an Error has occurred. Please Close and Restart.\n " + ex);
			return null;
		}
	}

	/**
	 * <p>
	 * Fill arrays to speed execution
	 * </p>
	 * 
	 * @throws <code>IOException</code>
	 *             - when errors occur reading file.
	 */
	private static void fillArrays(String dataPath) {
		fillThisArray(areaCodes, dataPath + System.getProperty("file.separator") + "area-codes.txt");
		log.debug("area-codes.txt Size: " + areaCodes.size());
		fillThisArray(employeeTypes, dataPath + System.getProperty("file.separator") + "employee-types.txt");
		log.debug("employee-types.txt Size: " + employeeTypes.size());
		fillThisArray(familyNames, dataPath + System.getProperty("file.separator") + "family-names.txt");
		log.debug("family-names.txt Size: " + familyNames.size());
		fillThisArray(givenNames, dataPath + System.getProperty("file.separator") + "given-names.txt");
		log.debug("given-names.txt Size: " + givenNames.size());
		fillThisArray(localities, dataPath + System.getProperty("file.separator") + "localities.txt");
		log.debug("localities.txt Size: " + localities.size());
		fillThisArray(mailHosts, dataPath + System.getProperty("file.separator") + "mail-hosts.txt");
		log.debug("mail-hosts.txt Size: " + mailHosts.size());
		fillThisArray(orgUnits, dataPath + System.getProperty("file.separator") + "organizational-units.txt");
		log.debug("organizational-units.txt Size: " + orgUnits.size());
		fillThisArray(positions, dataPath + System.getProperty("file.separator") + "positions.txt");
		log.debug("positions.txt Size: " + positions.size());
		fillThisArray(titleRanks, dataPath + System.getProperty("file.separator") + "title-ranks.txt");
		log.debug("title-ranks.txt Size: " + titleRanks.size());
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
	 * @throws <code>IOException</code>
	 *             - when errors occur reading.
	 * @throws <code>EOFException</code>
	 *             - when errors occur reading.
	 */
	private static void fillThisArray(ArrayList<String> al, String fileName) {
		try {
			input = new RandomAccessFile(fileName, "r");
			String str = "fwefwef";
			try {
				while (!(str == null)) {
					str = input.readLine();
					if (str == null) {
						break;
					}
					// System.out.println(str);
					al.add(str);
				}
				// jTA.append(getRand(al)+NL);
				// return al;
				try {
					Thread.sleep(10);
				} catch (InterruptedException ex) {
					System.out.println(ex);
				}
			} catch (EOFException EOFex) {
				System.out.println(EOFex);
				input.close();
			}
		} catch (IOException ioe) {
			log.error("This file does not exist: " + fileName + NL + ioe);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 2) {
			LDIFGeneration.generateLDIF(args[0], Integer.parseInt(args[1]));
			System.exit(0);
		} else if (args.length == 3) {
			baseDN = args[0];
			serverType = args[2];
			numberOfEntries = Integer.parseInt(args[3]);
			LDIFGeneration.generateLDIF(baseDN, numberOfEntries, serverType);
		} else if (args.length == 14) {
			dataPath = args[0];
			baseDN = args[1];
			outPutFilename = args[2];
			objectClass = args[3].split(" ");
			changeType = args[4];
			serverType = args[5];
			isPeople = Boolean.parseBoolean(args[6]);
			isCreateOUs = Boolean.parseBoolean(args[7]);
			isUidNaming = Boolean.parseBoolean(args[8]);
			useShortNames = Boolean.parseBoolean(args[9]);
			overWriteExitingOutput = Boolean.parseBoolean(args[10]);
			toLowerCase = Boolean.parseBoolean(args[11]);
			shortNameLength = Integer.parseInt(args[12]);
			numberOfEntries = Integer.parseInt(args[13]);
			LDIFGeneration.generateLDIF(dataPath, baseDN, outPutFilename, objectClass, changeType, serverType, isPeople,
					isCreateOUs, isUidNaming, useShortNames, overWriteExitingOutput, toLowerCase, shortNameLength,
					numberOfEntries);
		} else {
			fillFixedArrays();
			showHelp();
			System.exit(0);
		}
	}
}
