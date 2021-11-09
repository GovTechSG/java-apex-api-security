# APEX API Java Security Utility
[![Build Status](https://travis-ci.org/GovTechSG/java-apex-api-security.svg?branch=master)](https://travis-ci.org/GovTechSG/java-apex-api-security)
[![Coverage Status](https://coveralls.io/repos/github/GovTechSG/java-apex-api-security/badge.svg?branch=master)](https://coveralls.io/github/GovTechSG/java-apex-api-security?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/govtechsg/java-apex-api-security/badge.svg)](https://snyk.io/test/github/govtechsg/java-apex-api-security)

A java helper utilities that form HTTP security header for authentication and verification

## Table of Contents
- [Getting Started](#getting-started)
    * [Using Maven](#maven-guide)
        + [Maven Build Option](#maven-build-option)
        + [Maven Test](#maven-test)
    * [Using Gradle](#gradle-guide)
        + [Preparation](#preparation)
        + [Gradle Build Option](#gradle-build-option)
        + [Gradle Test](#gradle-test)
    * [Development](#development)
        + [How to create QueryData and FormData](#how-to-create-querydata-and-formdata)
        + [Generate L1 Authorization Header](#generate-l1-authorization-header)
        + [Generate L2 Authorization Header](#generate-l2-authorization-header)
        + [Generate L21 Authorization Header](#generate-l21-authorization-header)
        + [Generate L12 Authorization Header](#generate-l12-authorization-header)
- [Release](#release)
- [Contributing](#contributing)
- [License](#license)
- [References](#references)

## Getting Started
Include this helper class in your java project to perform API Security operations

This project use Maven or Gradle as its build and management tools


### Maven Guide

+ Download and Install Maven (3.5.0 or above)
+ Java (1.8)

#### Maven Build Option

**Option 1:** Compile and package into JAR

```bash
mvn package
```

The compiled _jar_ file will be located in the **target** folder
+ java-apex-api-security-<version>-SNAPSHOT.jar
+ java-apex-api-security-<version>-SNAPSHOT-jar-with-dependencies.jar (this includes log4j libraries)

Import this jar file into your java classpath to use the utility class

**Option 2:** Compile and install the package into your local maven repository

```bash
mvn install
```

1. The compiled _package_ file will be installed under _com.api.util.ApiSecurity_ in the .m2 repo

1. For Maven Client Project setup, add the following dependencies to your pom.xml file :

```xml
<dependency>
    <groupId>com.api.util</groupId>
    <artifactId>ApiSecurity</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```
 
**Note:** 
* This project is leveraging on Log4j _Version2_  framework for the logging. If you are using logging implementation other than Log4j _Version2_ , you can change to other type of implementation such as nop,simple,jdk14,logback. You could replace the following xml in pom.xml.


```xml
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-api</artifactId>
	<version>2.14.1</version>
</dependency>
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-core</artifactId>
	<version>2.14.1</version>
</dependency>
```

#### Maven Test
Pull centralised Unit Test-cases from the following Github url: <https://github.com/GovTechSG/test-suites-apex-api-security/tree/master> with Mavem command (in project root folder)

```bash
mvn scm:checkout -D checkoutDirectory=src/main/resources/test-suites
```

To execute unit-test with Maven command (in project root folder)

```bash
mvn test
```

### Gradle Guide

+ Download and Install Gradle (4.0 or above)
+ Java (1.8)

#### Preparation
As some of the test cases contains UTF-8 characters, you have to set following property before the JVM execute the Gradle Daemon.

```bash
export GRADLE_OPTS="-Dfile.encoding=utf-8" 
```

#### Gradle Build Option
**Option 1:** Compile and package into JAR

```bash
gradle clean build
```

#### Gradle Test
To test with Jacoco and publish a html report

```bash
gradle test jacocoTestReport
```

The compiled _jar_ file will be located in the **build/libs** folder
+ java-apex-api-security-2.0.0-SNAPSHOT.jar

Import this jar into your java classpath to use the utility class

**Option 2:** Compile and install the package into your local maven repository

1. Refer to Maven Guide > Build > Option 2, for the maven repo installation (excluding Maven Client Project setup)

1. For Gradle Client Project setup, add the following dependencies to your build.gradle file :

```text
repositories {
    mavenLocal()
}
dependencies {
    compile group: 'com.api.util', name: 'ApiSecurity', version: '2.0.0-SNAPSHOT'
}
	
```

### Development

#### Preparing HTTP Signature Token 

Append this signature token into the Authorization header of the HTTP
request.

#### Example Generated Token -

```
Apex_l1_eg realm="https://XYZ.api.gov.sg/abc/def", apex_l1_eg_app_id="APP_ID", apex_l1_eg_nonce="SOME_RANDOM_STRING", apex_l1_eg_signature_method="HMACSHA256", apex_l1_eg_timestamp="SOME_TIMESTAMP", apex_l1_eg_version="1.0", apex_l1_eg_signature="SOME_SIGNATURE"
```

#### Example Authorization Header - 

```
Authorization: Apex_l1_eg realm="https://XYZ.api.gov.sg/abc/def", apex_l1_eg_app_id="APP_ID", apex_l1_eg_nonce="SOME_RANDOM_STRING", apex_l1_eg_signature_method="HMACSHA256", apex_l1_eg_timestamp="SOME_TIMESTAMP", apex_l1_eg_version="1.0", apex_l1_eg_signature="SOME_SIGNATURE"
```

### Parameters

#### realm
This is an identifier for the caller. Any value can be used here.

**Note:** This is currently handled by the library

#### authPrefix

Authorization Header scheme prefix. There are 4 possible values for this
depending on the zone and the authentication method.

1. Apex_l1_ig
2. Apex_l1_eg
3. Apex_l2_ig
4. Apex_l2_eg

**Note:** This is currently handled by the library

#### httpMethod

The HTTP method, i.e. `GET`, `POST`, etc.

#### url
The full API endpoint (with query parameters if any).

#### appId
The APEX App ID.

#### secret
The APEX App secret. Not required if you want to use L2
authentication with SHA256WITHRSA.

#### formData
Data which should be passed in the request (for `POST` requests
usually). For `GET` requests, set this value to `null`.

#### password
The password of the keystore. Not required for L1. 

#### alias
The alias of the keystore. Not required for L1.

#### fileName
The p12 file path. Not required for L1.

#### nonce
The random generated string which to be used to generate the token. If
not set, a new random string will be generated.

#### timestamp
Timestamp which should be used to generate the token. Not required if
you want to use the current timestamp.



### Example GET Request

```java
try {
	AuthParam authParam = new AuthParam();
				
	authParam.url = URI.create("https://<<URL>>/api/v1");
	authParam.httpMethod = "GET";
	authParam.appName = "<<appId>>";
	String certFileName = "certificates/ssc.alpha.example.com.p12";
	String password = "<<passphrase>>";
	String alias = "alpha";
	authParam.privateKey = ApiSigning.getPrivateKey(certFileName, password, alias);

	AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
	// Add this signature value to the authorization header when sending the request.
	// authorizationToken.getToken();
	
}
catch (ApiUtilException e)
{
	e.printStackTrace();
}
```


### Example POST Request

```java
try {
	AuthParam authParam = new AuthParam();
				
	authParam.url = URI.create("https://<<URL>>/api/v1");
	authParam.httpMethod = "POST";
	authParam.appName = "<<appId>>";
	String certFileName = "certificates/ssc.alpha.example.com.p12";
	String password = "<<passphrase>>";
	String alias = "alpha";
	authParam.privateKey = ApiSigning.getPrivateKey(certFileName, password, alias);

	AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
	// Add this signature value to the authorization header when sending the request.
	// authorizationToken.getToken();
	
}
catch (ApiUtilException e)
{
	e.printStackTrace();
}
```

### How to create QueryData and FormData

The ApiSecurity Library provide the utility class ApiList and FormList to construct request Query String and Form Data.



**Generate QueryString**

```java
ApiList  queryData = new ApiList();
queryData.add("clientId", "1256-1231-4598");
queryData.add("accountStatus", "active");
queryData.add("txnDate", "2017-09-29");

String queryString = queryData.toString(true);
String baseUrl = String.format("https://example.com/resource?%s", queryString);
// https://example.com/resource?accountStatus=active&clientId=1256-1231-4598&txnDate=2017-09-29                              
```


**Generate FormData**

```java
FormList formData = new FormList();
formData.add("phoneNo", "+1 1234 4567 890");
formData.add("street", "Hellowood Street");
formData.add("state", "AP");

String formDataString = formData.toFormData();
// phoneNo=%2B1+1234+4567+890&street=Hellowood+Street&state=AP
```
```java
String signingUrl = "https://<<URL>>/api/v1/?param1=first&param2=123";

try {
	FormList formData = new FormList();
	formData.add("param1", "data1");
	
	AuthParam authParam = new AuthParam();
	
	authParam.url = URI.create(https://<<URL>>/api/v1");
	authParam.httpMethod = "POST";
	authParam.appName = "<<appId>>";
	authParam.nonce = "<<nonce>>";
	authParam.timestamp = "<<timestamp>>";
	authParam.formData = formData;
	
	AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
	assertEquals(expectedBaseString, authorizationToken.getBaseString());
}
catch (ApiUtilException e)
{
	e.printStackTrace();
}                                   
```

**NOTE** 

For **formData** parameter used for Signature generation, the key value parameters **do not** need to be URL encoded, 
When your client program is making the actual HTTP POST call, the key value parameters **has** to be URL encoded (refer to **formPostData**)


### Generate L1 Authorization Header

```java

FormList formData = new FormList();
formData.add("phoneNo", "+1 1234 4567 890");
formData.add("street", "Hellowood Street");
formData.add("state", "AP");

try {
	AuthParam authParam = new AuthParam();
				
	authParam.url = URI.create("https://<<URL>>/api/v1");
	authParam.httpMethod = "GET";
	authParam.appName = "<<appId>>";
	authParam.appSecret = "<<appSecret>>";
	authParam.formData = formData;

	// get the authorization token for L1
	AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
	
	System.out.println("BaseString :: " + authorizationToken.getBaseString());
	System.out.println("Authorization Token  :: " + authorizationToken.getToken());

	// make api call with authorizationToken.getToken()
	
}
catch (ApiUtilException e)
{
	e.printStackTrace();
}
```

### Generate L2 Authorization Header

```java
FormList formData = new FormList();
formData.add("phoneNo", "+1 1234 4567 890");
formData.add("street", "Hellowood Street");
formData.add("state", "AP");

ApiList  queryData = new ApiList();
queryData.add("clientId", "1256-1231-4598");
queryData.add("accountStatus", "active");
queryData.add("txnDate", "2017-09-29");
String queryString = queryData.toString(true);
String baseUrl = String.format("https://<<URL>>/api/v1?%s", queryString);

try {
	AuthParam authParam = new AuthParam();
				
	authParam.url = URI.create(baseUrl);
	authParam.httpMethod = "GET";
	authParam.appName = "<<appId>>";
	String certFileName = "certificates/ssc.alpha.example.com.p12";
	String password = "<<passphrase>>";
	String alias = "alpha";
	authParam.privateKey = ApiSigning.getPrivateKey(certFileName, password, alias);
	authParam.formData = formData;

	// get the authorization token for L2
	AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
	
	System.out.println("BaseString :: " + authorizationToken.getBaseString());
	System.out.println("Authorization Token  :: " + authorizationToken.getToken());

	// make api call with authorizationToken.getToken()
	
}
catch (ApiUtilException e)
{
	e.printStackTrace();
}
```

### Generate L21 Authorization Header
(for cross zone api from internet to intranet)

```java
FormList formData = new FormList();
formData.add("phoneNo", "+1 1234 4567 890");
formData.add("street", "Hellowood Street");
formData.add("state", "AP");

ApiList  queryData = new ApiList();
queryData.add("clientId", "1256-1231-4598");
queryData.add("accountStatus", "active");
queryData.add("txnDate", "2017-09-29");
String queryString = queryData.toString(true);


try {
	AuthParam authParam_WWW = new AuthParam();
	String baseUrl_WWW = String.format("https://<<URL_WWW>>/api/v1?%s", queryString);
	authParam_WWW.url = URI.create(baseUrl_WWW);
	authParam_WWW.httpMethod = "GET";
	authParam_WWW.appName = "<<appId_WWW>>";
	String certFileName = "certificates/ssc.alpha.example.com.p12";
	String password = "<<passphrase>>";
	String alias = "alpha";
	authParam_WWW.privateKey = ApiSigning.getPrivateKey(certFileName, password, alias);
	authParam_WWW.formData = formData;
	
	AuthParam authParam_WOG = new AuthParam();
	authParam_WOG.httpMethod = "GET";
	authParam_WOG.appName = "<<appId_WOG>>";
	authParam_WOG.appSecret = "<<appSecret_WOG>>";
	String baseUrl_WOG = String.format("https://<<URL_WOG>>/api/v1?%s", queryString);
	authParam_WOG.url = URI.create(baseUrl1);
	authParam_WWW.nextHop = authParam_WOG;
	

	// get the authorization token for L21
	AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam_WWW);
	
	System.out.println("BaseString :: " + authorizationToken.getBaseString());
	System.out.println("Authorization Token  :: " + authorizationToken.getToken());

	// make api call with authorizationToken.getToken()
	
}
catch (ApiUtilException e)
{
	e.printStackTrace();
}
```

### Generate L12 Authorization Header
(for cross zone api from intranet to internet)

```java
FormList formData = new FormList();
formData.add("phoneNo", "+1 1234 4567 890");
formData.add("street", "Hellowood Street");
formData.add("state", "AP");

ApiList  queryData = new ApiList();
queryData.add("clientId", "1256-1231-4598");
queryData.add("accountStatus", "active");
queryData.add("txnDate", "2017-09-29");
String queryString = queryData.toString(true);


try {
	AuthParam authParam_WOG = new AuthParam();
	authParam_WOG.httpMethod = "GET";
	authParam_WOG.appName = "<<appId_WOG>>";
	authParam_WOG.appSecret = "<<appSecret_WOG>>";
	String baseUrl_WOG = String.format("https://<<URL_WOG>>/api/v1?%s", queryString);
	authParam_WOG.url = URI.create(baseUrl1);
	
	AuthParam authParam_WWW = new AuthParam();
	String baseUrl_WWW = String.format("https://<<URL_WWW>>/api/v1?%s", queryString);
	authParam_WWW.url = URI.create(baseUrl_WWW);
	authParam_WWW.httpMethod = "GET";
	authParam_WWW.appName = "<<appId_WWW>>";
	String certFileName = "certificates/ssc.alpha.example.com.p12";
	String password = "<<passphrase>>";
	String alias = "alpha";
	authParam_WWW.privateKey = ApiSigning.getPrivateKey(certFileName, password, alias);
	authParam_WWW.formData = formData;
	authParam_WOG.nextHop = authParam_WWW;
	

	// get the authorization token for L12
	AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam_WOG);
	
	System.out.println("BaseString :: " + authorizationToken.getBaseString());
	System.out.println("Authorization Token  :: " + authorizationToken.getToken());

	// make api call with authorizationToken.getToken()
	
}
catch (ApiUtilException e)
{
	e.printStackTrace();
}
```
	
## Contributing
For more information about contributing PRs and issues, see [CONTRIBUTING.md](.github/CONTRIBUTING.md).

## Release
See [CHANGELOG.md](CHANGELOG.md).

## License
[MIT LICENSE](LICENSE).

## References
+ [UTF-8 in Gradle](https://stackoverflow.com/questions/21267234/show-utf-8-text-properly-in-gradle)
+ [LOG4J2 FAQ](https://logging.apache.org/log4j/2.x/faq.html)
+ [Akana API Consumer Security](http://docs.akana.com/cm/learnmore/app_security.htm)
+ [RSA and HMAC Request Signing Standard](https://tools.ietf.org/id/draft-cavage-http-signatures-08.html)
