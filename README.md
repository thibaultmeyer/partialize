# Partialize


[![Latest release](https://img.shields.io/badge/latest_release-16.12-orange.svg)](https://github.com/0xbaadf00d/partialize/releases)
[![JitPack](https://jitpack.io/v/0xbaadf00d/partialize.svg)](https://jitpack.io/#0xbaadf00d/partialize)
[![Build](https://img.shields.io/travis-ci/0xbaadf00d/partialize.svg?branch=master&style=flat)](https://travis-ci.org/0xbaadf00d/partialize)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/0xbaadf00d/partialize/master/LICENSE)

Partialize is a Java based library helping you to implement the JSON partial responses on your project.
*****



## Questions and issues
The GitHub issue tracker is only for bug reports and feature requests. Anything
else, such as questions for help in using the library, should be posted in
[StackOverflow](http://stackoverflow.com/questions/tagged/partialize?sort=active)
under tags `partialize` and `java`.




## Build the library
To compile Partialize, you must ensure that Java 8 and Maven are correctly
installed.

    #> mvn compile
    #> mvn package



## Query syntax

##### Example #1

    fields=firstName,lastName,posts(title,createDate)

##### Example #2

    fields=firstName,lastName,posts(*)



## Usage


### Objects to render as JSON

```java
@Partialize(
    allowedFields = {"uid", "firstName", "lastName", "emails", "createDate"},
    defaultFields = {"uid", "firstName", "lastName"}
)
public class AccountModel {
    private UUID uid;
    private String firstName;
    private String lastName;
    private String password;
    private List<AccountEmailModel> emails;
    private DateTime createDate;

    /* ADD GETTER / SETTER METHODS HERE */
}
```

```java
@Partialize(
    allowedFields = {"uid", "email", "isDefault"},
    defaultFields = {"email", "isDefault"}
)
public class AccountEmailModel {
    private UUID uid;
    private String email;
    private Boolean isDefault;

    /* ADD GETTER / SETTER METHODS HERE */
}
```


### Joda DateTime converter

```java
public class JodaDateTimeConverter implements Converter<DateTime> {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public void convert(final String fieldName, final DateTime data, final ObjectNode node) {
        node.put(fieldName, data.toString(JodaDateTimeConverter.DATETIME_FORMAT));
    }

    @Override
    public void convert(final String fieldName, final DateTime data, final ArrayNode node) {
        node.add(data.toString(JodaDateTimeConverter.DATETIME_FORMAT));
    }

    @Override
    public Class<DateTime> getManagedObjectClass() {
        return DateTime.class;
    }
}
```


### Code
```java
PartializeConverterManager.getInstance().registerConverter(new JodaDateTimeConverter());
final AccountModel account = AccountModel.find().where().eq("id", 1).findUnique();

final String fields = "firstName,lastName,emails(email,isDefault),createDate";
final Partialize partialize = new Partialize();
final ContainerNode result = partialize.buildPartialObject(fields, AccountModel.class, account);
System.out.println(result);
```


### Output
```json
{
    "firstName": "John",
    "lastName": "Smith",
    "emails": [
      {
        "email": "john.smith@domain.local",
        "isDefault": true
      },
      {
        "email": "john@domain.local",
        "isDefault": false
      }
    ],
    "createDate": "2016-01-15T23:45:12"
}
```


### Field aliases
```java
final AccountModel account = AccountModel.find().where().eq("id", 1).findUnique();

final String fields = "alias1,alias2";
final Partialize partialize = new Partialize();
partialize.setAliases(new HashMap<String, String>() {{
    put("alias1", "firstName");
    put("alias2", "lastName");
}});
final ContainerNode result = partialize.buildPartialObject(fields, AccountModel.class, account);
System.out.println(result);
```


### Access policies
```java
final AccountModel account = AccountModel.find().where().eq("id", 1).findUnique();

final String fields = "uid,firstName,lastName,password";
final Partialize partialize = new Partialize();
partialize.setAccessPolicy(accessPolicy -> {
    return Arrays.asList(
        "AccountModel.uid",
        "AccountModel.firstName",
        "AccountModel.lastName"
    ).contains(accessPolicy.clazz.getSimpleName() + "." + accessPolicy.field);
});
final ContainerNode result = partialize.buildPartialObject(fields, AccountModel.class, account);
System.out.println(result);
```



## License
This project is released under terms of the [MIT license](https://raw.githubusercontent.com/0xbaadf00d/partialize/master/LICENSE).
