# Running
On unix run `./LDIFGenerator`. On Windows, run the `LDIFGenerator.bat` file.

Note that the program expects to find the "Data" directory where the JAR file is located. This can be changed within the GUI.

## Customization
You may need to change the following to fit your needs:

* Base Added to Generated Records: All records will be added to this base context.
* Number of Records - This is the number of records that will be generated. (Defaults to 100). Must be more than 10. NOTE: there maybe a couple of less records as some duplicates maybe created, and they may be dropped.
* Generate OUs - If this is checked, OUs will be generated in the LDIF file. This should normally be checked unless the OUs have already been created. If Not checked, then all users will be put in the "Base Added to Generated Records"
* Generate People - If this is checked, the people entries will be generated. This is normally checked and inetOrgPerson objects will be generated. If not checked only the OUs will be generated.
* DN Uses uid vs cn - For those installations that need the uid=username instead of cn=username. Normally this is not checked with eDirectory.
* "Numeric in DN, CN UID" - Allows the DN and cn or UID to be generated as numbers. This is often helpful if you need to generate test accounts that can be programatically tested by binding with cn=1096 and then cn=1096+1 (cn=1097).
* Directory where input data is stored - This is where the data files are located. By default, the data input files are stored in the data directory in the root where the application was installed.
* File where output records will be written - The generated records will be written to this file.
* Server Specific (DropDown)- Normally this should be left at Generic. If you are using Active Directory, you must choose it here as Active Directory requires a specific attribute, (sAMAccountName), to be added to the inetOrgPerson objectclass.
* ChangeType (DropDown) - Some server import routines need a ChangeType record to for importing. Also, you might want to set the changeType to Add, so you can do a search and replace to change it to delete so you could delete the records that were added.

### Microsoft Active Directory
When you select "Microsoft Active Directory" in the UI we:

* add `SamAccountName` to attributes to person entries using the value for UID

You may want to change the data file `objectclasses.txt` file to be:
```
top
person
organizationalPerson
user
```

### EDirectory
When you select "eDirectory" in the UI we add these `objectClasses` to OUs:

* `ndsLoginProperties`
* `ndsContainerLoginProperties`

## Data Files
By default, the data input files are stored in the `Data` directory in the root where the application was unpacked. You could modify these files to make them look more like you company or situation. The files and their uses are described as follows:

* `area-codes.txt` - List of area codes that will be randomly used.
* `employee-types.txt` - List of employee types that will be randomly used.
* `family-names.txt` - List of surnames that will be randomly used.
* `given-names.txt` - List of givenname that will be randomly used.
* `localities.txt` - List of localities (city) that will be randomly used.
* `mail-hosts.txt` - List of mail hosts that will be randomly used to generate email addresses.
* `organizational-units.txt` - List of OUs that will be randomly used.
* `positions.txt` - List of Titles that will be randomly used along with some other information to generate titles.
* `title-ranks.txt` - List of Ranks that will be randomly used along with some other information to generate titles.
* `objectclasses.txt` - The values for ObjectClass used when creating people entries.

Feel free to change these files as you wish, just do not alter the format of the file.