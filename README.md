# APEX API Java Security Utility
[![Build Status](https://travis-ci.org/GovTechSG/java-apex-api-security.svg?branch=master)](https://travis-ci.org/GovTechSG/java-apex-api-security)
[![Coverage Status](https://coveralls.io/repos/github/GovTechSG/java-apex-api-security/badge.svg?branch=master)](https://coveralls.io/github/GovTechSG/java-apex-api-security?branch=master)
[![Known Vulnerabilities](https://snyk.io/test/github/govtechsg/java-apex-api-security/badge.svg)](https://snyk.io/test/github/govtechsg/java-apex-api-security)

A java helper utilities that form HTTP security header for authentication and verification

## Getting Started
Include this helper class in your java project to perform API Security operations

This project use Maven or Gradle as its build and management tools

### Maven Guide

+ Download and Install Maven (3.5.0 or above)
+ Java (1.8)

#### Build

**Option 1:** Compile and package into JAR

```bash

mvn package
	
```

The compiled _jar_ file will be located in the **target** folder
+ java-apex-api-security-1.0-SNAPSHOT.jar
+ java-apex-api-security-1.0-SNAPSHOT-jar-with-dependencies.jar (this includes log4j libraries)

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
    <version>1.0-SNAPSHOT</version>
</dependency>
  	
```
 
**Note:** 
* This project is leveraging on _slf4j-log4j12_ framework for the logging. If you are using logging implementation other than log4j, you can change to other type of implementation such as nop,simple,jdk14,logback. You could replace the following xml in pom.xml.
* If your are using Log4j _Version2_, please refer to [Log4j2-SLF4J Binding](https://logging.apache.org/log4j/2.x/log4j-slf4j-impl/index.html)


```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.25</version>
</dependency>
  	
```

### Gradle Guide

+ Download and Install Gradle (4.0 or above)
+ Java (1.8)

#### Preparation
As some of the test cases contains UTF-8 characters, you have to set following property before the JVM execute the Gradle Daemon.

```bash
export GRADLE_OPTS="-Dfile.encoding=utf-8" 
```

#### Build
**Option 1:** Compile and package into JAR

```bash
gradle clean build
```

#### Test
To test with Jacoco and publish a html report

```bash
gradle test jacocoTestReport
```

The compiled _jar_ file will be located in the **build/libs** folder
+ java-apex-api-security-1.0-SNAPSHOT.jar

Import this jar into your java classpath to use the utility class

**Option 2:** Compile and install the package into your local maven repository

1. Refer to Maven Guide > Build > Option 2, for the maven repo installation (excluding Maven Client Project setup)

1. For Gradle Client Project setup, add the following dependencies to your build.gradle file :

```text
repositories {
    mavenLocal()
}
dependencies {
    compile group: 'com.api.util', name: 'ApiSecurity', version: '1.0-SNAPSHOT'
}
	
```

#### Development

##### Preparing BaseString :

Method: 
* getBaseString

Params:
* authPrefix - Authorization Header scheme prefix , i.e 'prefix_appId'
* signatureMethod
* appId - App ID created in Gateway
* urlPath
* httpMethod
* formList - only needed for Content-Type: application/x-www-form-urlencoded
* nonce - set to null for random generated number
* timestamp - set to null for current timestamp

```java
String url = "https://<<URL>>/api/v1/?param1=first&ab-param2=123";

ApiList formList = new ApiList();
formList.add("param1", "data1");

String baseString;

try {
baseString = ApiSigning.getBaseString(
    "<<authPrefix>>",
    "HMACSHA256",
    "<<appId>>",
    url,
    "post",
    formList,
    "6584351262900708156",
    "1502184161702"
);

System.out.println(baseString);

} catch (ApiUtilException e) {
    e.printStackTrace();
}
                                      
```

##### Preparing L1 Security Signature :

Method:
* getHMACSignature

Params:
* baseString
* secret

```java
String baseString = "GET&https://<<URL>>/api/v1/&ap=裕廊坊 心邻坊&<<authPrefix>>_app_id=<<appID>&<<authPrefix>>_nonce=2851111144329605674&<<authPrefix>>_signature_method=HMACSHA256&<<authPrefix>>_timestamp=1502163903712&<<authPrefix>>_version=1.0";
String secret = "<<appSecret>>";
String L1Sig;
		
try {
    L1Sig = ApiSigning.getHMACSignature(baseString, secret);
    System.out.println(L1Sig);

} catch (ApiUtilException e) {
    e.printStackTrace();
}

```

##### Preparing L2 Security Signature :

Method:
* getRSASignature

Params:
* baseString
* privateKey

```java
String baseString = "GET&https://<<URL>/api/v1/&ap=裕廊坊 心邻坊&<<authPrefix>>_app_id=<<appId>>&<<authPrefix>>_nonce=7231415196459608363&<<authPrefix>>_signature_method=SHA256withRSA&<<authPrefix>>_timestamp=1502164219425&<<authPrefix>>_version=1.0&oq=c# nunit mac&q=c# nunit mac";
String alias = "alpha";
String password = "<<passphrase>>";
String keyStoreFileName = "certificates/ssc.alpha.example.com.p12";
String publicCertFileName = "certificates/ssc.alpha.example.com.cer";

try {
    
    PrivateKey privateKey = ApiSigning.getPrivateKeyFromKeyStore(keyStoreFileName, password, alias);
    
    String signature = ApiSigning.getRSASignature(baseString, privateKey);

    System.out.println(expectedSignature);
    System.out.println(signature);
    
    PublicKey publicKey = ApiSigning.getPublicKeyFromX509Certificate(publicCertFileName);
    
} catch (ApexUtilLibException e) {
    e.printStackTrace();
}

```

##### Preparing HTTP Signature Token :

Append this signature token into the Authorization header of the HTTP request

Params:
* realm
* authPrefix - Authorization Header scheme prefix , i.e 'prefix_appId'
* httpMethod
* urlPath
* appId - App ID created in Gateway
* secret - set to null for REST L2 SHA256WITHRSA
* formList
* password
* alias
* fileName
* nonce - set to null for random generated number
* timestamp - set to null for current timestamp 


```java
String url = "https://<<URL>>/api/v1/?ap=裕廊坊%20心邻坊";
String certFileName = "certificates/ssc.alpha.example.com.p12";
String password = "<<passphrase>>";
String alias = "alpha";
String appId = "<<appId>>";
String secret = null;
ApiList formList = null;
String nonce = null;
String timestamp = null;

try {
    String signature = ApiSigning.getSignatureToken("http://api.test.io/l2", "<<authPrefix>>", "get", url, appId, null, null, password, alias, certFileName, nonce, timestamp);
} catch (ApiUtilException e) {
    e.printStackTrace();
}
```

### Contributing
+ For more information about contributing PRs and issues, see [CONTRIBUTING.md](CONTRIBUTING.md).

### Release
+ See [CHANGELOG.md](CHANGELOG.md).

### Reference: 
+ [UTF-8 in Gradle](https://stackoverflow.com/questions/21267234/show-utf-8-text-properly-in-gradle)
+ [SLF4J FAQ](https://www.slf4j.org/faq.html)
+ [Akana API Consumer Security](http://docs.akana.com/ag/cm_policies/using_api_consumer_app_sec_policy.htm)
+ [RSA and HMAC Request Signing Standard](http://tools.ietf.org/html/draft-cavage-http-signatures-05)
