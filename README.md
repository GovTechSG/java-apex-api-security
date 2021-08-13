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
        + [Constructing Signature BaseString](#constructing-signature-basestring)
        + [Constructing HMAC256 L1 Header](#constructing-hmac256-l1-header)
        + [Constructing RSA256 L2 Header](#constructing-rsa256-l2-header)
        + [Preparing HTTP Signature Token](#preparing-http-signature-token)
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
    <version>1.3.5-SNAPSHOT</version>
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
    compile group: 'com.api.util', name: 'ApiSecurity', version: '1.3.5-SNAPSHOT'
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

#### authPrefix

Authorization Header scheme prefix. There are 4 possible values for this
depending on the zone and the authentication method.

1. Apex_l1_ig
2. Apex_l1_eg
3. Apex_l2_ig
4. Apex_l2_eg

#### httpMethod

The HTTP method, i.e. `GET`, `POST`, etc.

#### signingUrl
The full API endpoint (with query parameters if any). This will be in
the form of `https://<<tenant>>.e.api.gov.sg/xxx/yyy` or
`https://<<tenant>>-pvt.i.api.gov.sg/xxx/yyy`.

**Note:** Please note that you **must** have `.e` or `.i` in the URL.
Otherwise you can encounter authorization failures.

#### appId
The APEX App ID.

#### secret
The APEX App secret. Set to value to `null` if you want to use L2
authentication with SHA256WITHRSA.

#### formData
Data which should be passed in the request (for `POST` requests
usually). For `GET` requests, set this value to `null`.

#### password
The password of the keystore. Set `null` for L1. 

#### alias
The alias of the keystore. Set `null` for L1.

#### fileName
The p12 file path. Set `null` for L1.

#### nonce
The random generated string which to be used to generate the token. If
you set this to `null`, a new random string will be generated.

#### timestamp
Timestamp which should be used to generate the token. Set to `null` if
you want to use the current timestamp.



### Example GET Request

```java
String realm = "<<your_client_host_url>>";
String authPrefix = "<<authPrefix>>";
String httpMethod = "GET";
//Append the query param in the url or else add as ApiList 
String signingUrl = "https://<<URL>>/api/v1/?param1=first&param2=123";
String certFileName = "certificates/ssc.alpha.example.com.p12";
String password = "<<passphrase>>";
String alias = "alpha";
String appId = "<<appId>>";
String secret = null;
//only needed for Content-Type: application/x-www-form-urlencoded, else null
ApiList formData = null;
String nonce = null;
String timestamp = null;

try {
    String signature = ApiSigning.getSignatureToken(authPrefix, authPrefix, httpMethod, signingUrl, appId, secret, formData, password, alias, certFileName, nonce, timestamp);
    // Add this signature value to the authorization header when sending the request.
} catch (ApiUtilException e) {
    e.printStackTrace();
}
```


### Example POST Request

```java
String realm = "<<your_client_host_url>>";
String authPrefix = "<<authPrefix>>";
String httpMethod = "POST";
//Append the query param in the url or else add as ApiList 
String signingUrl = "https://<<URL>>/api/v1";
String certFileName = "certificates/ssc.alpha.example.com.p12";
String password = "<<passphrase>>";
String alias = "alpha";
String appId = "<<appId>>";
String secret = null;
//only needed for Content-Type: application/x-www-form-urlencoded, else null
ApiList formData = null;
String nonce = null;
String timestamp = null;


//optional for QueryParam - in-case not append the query parameters in the signingUrl
//Sring signingUrl = "https://<<tenant>>-pvt.i.api.gov.sg/api/v1"
ApiList queryParam = new ApiList();
queryParam.add("query1","value1");

//optional for formData
formData = new ApiList();
formData.add("param1", "data1");

//If queryParam and formData are both available, combine the list before submitting
formData.addAll(queryParam);

try {
    String signature = ApiSigning.getSignatureToken(authPrefix, authPrefix, httpMethod, signingUrl, appId, secret, formData, password, alias, certFileName, nonce, timestamp);
    // Add this signature value to the authorization header when sending the request.
} catch (ApiUtilException e) {
    e.printStackTrace();
}
```
**NOTE** 

For **formData** parameter used for Signature generation, the key value parameters **do not** need to be URL encoded, 
When your client program is making the actual HTTP POST call, the key value parameters **has** to be URL encoded (refer to **formPostData**)


#### Constructing Signature BaseString (for reference only)

**Please note that this section is for reference only. The actual token
generation is done using the `ApiSigning.getSignatureToken()` method.**

Method: 
* getBaseString

Params:
* authPrefix - Authorization Header scheme prefix , i.e 'Apex_l2_eg'
* signatureMethod
* appId - App ID created in Gateway
* urlPath
* httpMethod
* formData - only needed for Content-Type: application/x-www-form-urlencoded
* nonce - set to null for random generated number
* timestamp - set to null for current timestamp

```java
String signingUrl = "https://<<URL>>/api/v1/?param1=first&param2=123";

ApiList formData = new ApiList();
formData.add("param1", "data1");

String baseString;

try {
baseString = ApiSigning.getBaseString(
    "<<authPrefix>>",
    "HMACSHA256",
    "<<appId>>",
    signingUrl,
    "post",
    formData,
    "6584351262900708156",
    "1502184161702"
);

System.out.println(baseString);

} catch (ApiUtilException e) {
    e.printStackTrace();
}
                                      
```

#### Constructing HMAC256 L1 Header (for reference only)

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

#### Constructing RSA256 L2 Header (for reference only)

Method:
* getRSASignature

Params:
* baseString
* privateKey

```java
String baseString = "GET&https://<<URL>/api/v1/&ap=裕廊坊 心邻坊&<<authPrefix>>_app_id=<<appId>>&<<authPrefix>>_nonce=7231415196459608363&<<authPrefix>>_signature_method=SHA256withRSA&<<authPrefix>>_timestamp=1502164219425&<<authPrefix>>_version=1.0&oq=123&q=abc";
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

#### Sample HTTP GET Call with APEX L1 Security (for reference only)

**Please note that this is for reference only. The actual implementation
might be different than this.**

```java

@Test
public void Http_GET_Test() throws ApiUtilException, IOException
{
	
	String httpMethod = "GET";
	//URL for actual HTTP API call
	String url = "https://tenant.api.gov.sg:443/api14021live/resource";
	//URL for passing as parameter for APEX Signature Token generation
	String signUrl = "https://tenant.e.api.gov.sg:443/api14021live/resource";
	String appId = "tenant-1X2w7NQPzjO2azDu904XI5AE";
	String secret = "s0m3s3cr3t";
	
	String authorizationToken = ApiSigning.getSignatureToken(
        realm
		, authPrefixL1
		, httpMethod
		, signUrl
		, appId
		, secret
		, null;
		, null
		, null
		, null
		, null
		, null
	);
	System.out.println("authorizationToken : "+authorizationToken);
	
	try {
		//ignore SSL
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, getTrustManager(), new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setDoOutput(false);
		con.setDoInput(true);
		con.setRequestMethod(httpMethod);
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Authorization", authorizationToken);
		con.setUseCaches(false);
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);

		System.out.println("Start http call ...");
		int status = -1;
		status = con.getResponseCode();
		System.out.println("HTTP Status:" + status);
		
		System.out.println("End http call ...");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		
		System.out.println("Content:" + content);
		in.close();
		con.disconnect();
	}catch(Exception e){
		System.out.println("Error executing Http_Call_Test() : " + e);
	}
	//force to true to pass the test case
	assertTrue(true);
}

```


#### Sample HTTP POST Call for x-www-form-urlencoded with APEX L1 Security (for reference only)

**Please note that this is for reference only. The actual implementation
might be different than this.**

```java

@Test
public void Http_POST_Test() throws ApiUtilException, IOException
{
	
	String httpMethod = "POST";
	//URL for actual HTTP API call
	String url = "https://tenant.api.gov.sg:443/api14021live/resource";
	//URL for passing as parameter for APEX Signature Token generation
	String signUrl = "https://tenant.e.api.gov.sg:443/api14021live/resource";
	String appId = "tenant-1X2w7NQPzjO2azDu904XI5AE";
	String secret = "s0m3s3cr3t";
	ApiList formData = new ApiList();
	formData.add("key1", "value1");
	formData.add("key2","value2");
	
	String authorizationToken = ApiSigning.getSignatureToken(
        realm
		, authPrefixL1
		, httpMethod
		, signUrl
		, appId
		, secret
		, formData
		, null
		, null
		, null
		, null
		, null
	);
	System.out.println("authorizationToken : "+authorizationToken);
	
	try {
		//ignore SSL
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, getTrustManager(), new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod(httpMethod);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");			
		con.setRequestProperty("charset", "utf-8");
		con.setRequestProperty("Authorization", authorizationToken);
		con.setUseCaches(false);
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		ApiList formPostData = new ApiList();
		formPostData.add("key1",URLEncoder.encode("value1", "UTF-8"));
		formPostData.add("key2",URLEncoder.encode("value2", "UTF-8"));
		out.writeBytes(formPostData.toString(false));
		out.flush();
		out.close();
		System.out.println("Start http call ...");
		int status = -1;
		status = con.getResponseCode();
		System.out.println("HTTP Status:" + status);
		
		System.out.println("End http call ...");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		
		System.out.println("Content:" + content);
		in.close();
		con.disconnect();
	}catch(Exception e){
		System.out.println("Error executing Http_Call_Test() : " + e);
	}
	//force to true to pass the test case
	assertTrue(true);
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
