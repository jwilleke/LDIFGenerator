# LDIFGenerator
The LDIF Generator will generate random, fictitious, LDIF records that can be imported into LDAP server or other applications for testing.

Program was orginally from [LDAPWIKI](http://ldapwiki.com/wiki/LDIF%20Generator) and now open source.

The LDIF Generator will generate random, fictitious, LDIF records that can be imported into LDAP server or other applications for testing.
We have used the DBGEN.PL program and had several problems with some of the aspects of the output that was generated. The attributes that used distinguished names, DBGEN only provided RDNs for these entries. When we were working with Novell's eDirectory or with Active Directory, it actually does some referential integrity checking that would prevent the importing of DN attributes that were not valid. The LDIF Generator tool will create valid manager and secretary relationships.

In addition, we wanted to generate some large trees and demo some of the Organizational chart programs that would work against a LDAP instance that would simulate a real organization.

Out of these issues with DBGEN, we wrote LDIFGen. This is a Java program that should work with Java 1.4 or above and on "nearly" any platform that has Java.

You must generate at least 10 records. There is no maximum determined. (There could be some resource issues at very large numbers over 1,000,000) Below we show typical screen output of the tool generating about 500 entries/sec:
    Generating 100000 entries.
    With People entries With 40 OUs entries
    Using CN in Distinguished NameIn the dc=willeke, dc=com context
    For the Generic Server Type
    Generating 100000 entries took 3 minute(s) 19 second(s)  or 502.513 entries/seconds

# Set Up
Extract the ZIP File contents to a folder.
Windows: If JAVA is in your path, simply run the LDIFGen.bat file. Otherwise edit LDIFGen.bat
If you are not on Windows, can run:
java -jar LDIFGen.jar
The program expects to find the "Data" directory where the LDIFGen.jar file is located. This can be changed within the GUI.

You will need to change the following to fit your needs:

Base Added to Generated Records: All records will be added to this base context.
Number of Records - This is the number of records that will be generated. (Defaults to 100). Must be more than 10. NOTE: there maybe a couple of less records as some duplicates maybe created, and they may be dropped.
Generate OUs - If this is checked, OUs will be generated in the LDIF file. This should normally be checked unless the OUs have already been created. If Not checked, then all users will be put in the "Base Added to Generated Records"
Generate People - If this is checked, the people entries will be generated. This is normally checked and inetOrgPerson objects will be generated. If not checked only the OUs will be generated.
DN Uses uid vs cn - For those installations that need the uid=username instead of cn=username. Normally this is not checked with eDirectory.
"Numeric in DN, CN UID" - Allows the DN and cn or UID to be generated as numbers. This is often helpful if you need to generate test accounts that can be programatically tested by binding with cn=1096 and then cn=1096+1 (cn=1097).
Directory where input data is stored - This is where the data files are located. By default, the data input files are stored in the data directory in the root where the application was installed.
File where output records will be written - The generated records will be written to this file.
Server Specific (DropDown)- Normally this should be left at Generic. If you are using Active Directory, you must choose it here as Active Directory requires a specific attribute, (sAMAccountName), to be added to the inetOrgPerson objectclass.
ChangeType (DropDown) - Some server import routines need a ChangeType record to for importing. Also, you might want to set the changeType to Add, so you can do a search and replace to change it to delete so you could delete the records that were added.

# Microsoft Active Directory
When you select Microsoft Active Directory we:
add SamAccountName to attributes to person entries using the value for UID
You may want to change the Data File objectclasses.txt file to be:
    top
    person
    organizationalPerson
    user

# EDirectory
When you select "eDirectory" we add objectClasses to OUs:
* ndsLoginProperties
* ndsContainerLoginProperties

#Data Files
The data files are similar to the files used in DBGEN program and the files that are used for DBGEN can be used with this program. By default, the data input files are stored in the data directory in the root where the application was installed. You could modify these files to make them look more like you company or situation. The files and their uses are described as follows:
* area-codes.txt - List of area codes that will be randomly used.
* employee-types.txt - List of employee types that will be randomly used.
* family-names.txt - List of surnames that will be randomly used.
* given-names.txt - List of givenname that will be randomly used.
* localities.txt - List of localities (city) that will be randomly used.
* mail-hosts.txt - List of mail hosts that will be randomly used to generate email addresses.
* organizational-units.txt - List of OUs that will be randomly used.
* positions.txt - List of Titles that will be randomly used along with some other information to generate titles.
* title-ranks.txt - List of Ranks that will be randomly used along with some other information to generate titles.
* objectclasses.txt - The values for ObjectClass used when creating people entries.
Feel free to change these files as you wish, just do not alter the format of the file.


# Sample Output#[Edit]
## Generic LDIF
Here is a few records from a sample output when using the "Generic" output:
    dn: ou=Payroll,dc=willeke, dc=com
    changetype: add
    ou: Payroll
    objectClass: top
    objectClass: organizationalUnit


    dn: cn=1000002, ou=Payroll, dc=willeke, dc=com
    changetype: add
    objectClass: top
    objectClass: person
    objectClass: organizationalPerson
    objectClass: inetOrgPerson
    cn: 1000002
    cn: KayC
    sn: Kay
    description: This is Chicky Kay's description
    facsimileTelephoneNumber: +1 213 210-3794
    l: Alameda
    ou: Payroll
    postalAddress: Payroll$Alameda
    telephoneNumber: +1 213 993-9665
    title: Elite Payroll Assistant
    userPassword: Kay
    uid: KayC
    givenName: Chicky
    mail: KayC@ns-mail4.com
    carLicense: RR3XG6
    departmentNumber: 8982
    employeeType: Contract
    homePhone: +1 213 492-4709
    initials: C. K.
    mobile: +1 213 915-4261
    pager: +1 213 780-4510
    roomNumber: 9562
    secretary: cn=100135, ou=Human Resources, dc=willeke, dc=com
    manager: cn=100474, ou=Administrative, dc=willeke, dc=com 

[Java Versions And Running Programs](http://ldapwiki.com/wiki/Java%20Versions%20And%20Running%20Programs)

# LDIFGen Downloads
* [LDIF Generator/LDIFGen2014-09-01.zip(info) - Latest Version](http://ldapwiki.com/wiki/Java%20Versions%20And%20Running%20Programs)

# Related Articles 
* [Cool Solutions: Easily Generate LDIF File for Testing](https://www.netiq.com/communities/cool-solutions/cool_tools/easily-generate-ldif-file-testing/)

