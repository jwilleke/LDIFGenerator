# LDIFGenerator
The LDIF Generator will generate random, fictitious, LDIF records that can be imported into LDAP server or other applications for testing.

## Running
To run the application, extract the ZIP or TAR file, and follow the steps in the extracted `README`.

## Building
To build, you must have at least JDK 8. The project output currently targets Java 1.4.

### Gradle
We use Gradle as our build tool of choice. See [individual project `READMEs`](LDIFGenerator/README.md) for details.

## Purpose & History
Program was orginally from [LDAPWIKI](http://ldapwiki.com/wiki/LDIF%20Generator) and now open source.

We have used the DBGEN.PL program and had several problems with some of the aspects of the output that was generated. The attributes that used distinguished names, DBGEN only provided RDNs for these entries. When we were working with Novell's eDirectory or with Active Directory, it actually does some referential integrity checking that would prevent the importing of DN attributes that were not valid. The LDIF Generator tool will create valid manager and secretary relationships.

In addition, we wanted to generate some large trees and demo some of the Organizational chart programs that would work against a LDAP instance that would simulate a real organization.

Out of these issues with DBGEN, we wrote LDIFGen. This is a Java program that should work with Java 1.4 or above and on "nearly" any platform that has Java.

You must generate at least 10 records. There is no maximum determined. (There could be some resource issues at very large numbers over 1,000,000) Below we show typical screen output of the tool generating about 500 entries/sec:

    Generating 100000 entries.
    With People entries With 40 OUs entries
    Using CN in Distinguished NameIn the dc=willeke, dc=com context
    For the Generic Server Type
    Generating 100000 entries took 3 minute(s) 19 second(s)  or 502.513 entries/seconds

## Sample Output
### Generic LDIF
Here are a few records from a sample output when using the "Generic" output:

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

## LDIFGen Downloads
The original downloads from [ldapwiki] (https://ldapwiki.com/wiki/LDIF%20Generator)
* [LDIF Generator/LDIFGen2014-09-01.zip(info) - Version 0.1.0](http://ldapwiki.com/wiki/Java%20Versions%20And%20Running%20Programs)
* [hLDIF Generator - Version 0.0.1](https://ldapwiki.com/attach/LDIF%20Generator/LDIFGen.zip)

## Related Articles 
* [Cool Solutions: Easily Generate LDIF File for Testing](https://www.netiq.com/communities/cool-solutions/cool_tools/easily-generate-ldif-file-testing/) 
