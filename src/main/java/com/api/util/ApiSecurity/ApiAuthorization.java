package com.api.util.ApiSecurity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author GDS-PDD
 *
 */
public class ApiAuthorization {
	
	private static final Logger log = LoggerFactory.getLogger(ApiAuthorization.class);
	private final static String USER_AGENT = "Mozilla/5.0";
	
	/**
	 * @param baseString
	 * @param secret
	 * @return
	 * @throws ApiUtilException
	 */
	public static String getL1Signature(String baseString, String secret) throws ApiUtilException
	{
		log.debug("Enter :: getL1Signature :: baseString : {} , secret: {} ",baseString , secret);
		
		//Initialization
		String base64Token=null;
		SecretKeySpec signingKey = null;
		Mac mac = null;
		byte[] rawHmac = null;
		
		try{	
			//Validation
			if (baseString == null || baseString.isEmpty()) {
				throw new ApiUtilException("baseString must not be null or empty.");
			}
			
			if (secret == null || secret.isEmpty()) {
				throw new ApiUtilException("secret must not be null or empty.");
			}
			
			// get an hmac_sha256 key from the raw key bytes			
			try {
				signingKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HMACSHA256");
			} catch (UnsupportedEncodingException uee) {
				throw uee;
			}
			
			// get an hmac_sha256 Mac instance and initialize with the signing key			
			try {
				mac = Mac.getInstance("HMACSHA256");
			} catch (NoSuchAlgorithmException nsae) {
				throw nsae;
			}
			
			try {
				mac.init(signingKey);
			} catch (InvalidKeyException ike) {
				throw ike;
			}
	
			// compute the hmac on input data bytes					
			try {
				rawHmac = mac.doFinal(baseString.getBytes("UTF-8"));
			} catch (IllegalStateException | UnsupportedEncodingException e1) {
				throw e1;
			}
	
			// base64-encode the hmac
			 base64Token = new String(Base64.getEncoder().encodeToString(rawHmac));
			 
		}catch(ApiUtilException ae){
			log.error("Error :: getL1Signature :: " + ae.getMessage());
			throw ae;
		}catch(Exception e){
			log.error("Error :: getL1Signature :: " + e.getMessage());
			throw new ApiUtilException("Error during L1 Signature value generation",e);
		}
		
		log.debug("Exit :: getL1Signature :: base64Token : {} ", base64Token);
		
		return base64Token;
	}
	
	
	/**
	 * @param signature
	 * @param secret
	 * @param baseString
	 * @return
	 * @throws ApiUtilException
	 */
	public static boolean verifyL1Signature(String signature, String secret, String baseString) throws ApiUtilException
	{
		log.debug("Enter :: verifyL1Signature :: signature : {} , baseString : {} , secret: {} ", signature, baseString , secret);
		
		String expectedSignature=null;
		expectedSignature = getL1Signature(baseString, secret);
		boolean verified=false;
		verified=expectedSignature.equals(signature);
		
		log.debug("Exit :: verifyL1Signature :: boolean : {}", verified);
		
		return verified;
	}
	
	/**
	 * @param keystoreFileName
	 * @param password
	 * @param alias
	 * @return
	 * @throws ApiUtilException
	 */
	public static PrivateKey getPrivateKeyFromKeyStore(String keystoreFileName, String password, String alias) throws ApiUtilException
	{	
		log.debug("Enter :: getPrivateKeyFromKeyStore :: keystoreFileName : {} , password: {} , alias: {} ",keystoreFileName , password, alias);
		
		//Initialization
		KeyStore ks = null;
		PrivateKey privateKey = null;
		java.io.FileInputStream fis = null;		
		KeyStore.PrivateKeyEntry keyEnt = null;
		
		try{
		
			try {
				ks = KeyStore.getInstance("JKS");
			} catch (KeyStoreException kse) {
				throw kse;
			}
	
			// keystore and key password
			char[] passwordChar = password.toCharArray();
			
			try {
				fis = new java.io.FileInputStream(keystoreFileName);
				
				ks.load(fis, passwordChar);
			} catch (IOException ioe) {
				throw ioe;
			} catch (NoSuchAlgorithmException nsae) {
				throw nsae;
			} catch (CertificateException ce) {
				throw ce;
			} finally {
				if (fis != null) {
				  try {
						fis.close();
					} catch (IOException ioe) {
						throw ioe;
					}
				}
			}			
			
			try {
				keyEnt = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(passwordChar));
			} catch (NoSuchAlgorithmException nsae) {
				throw nsae;
			} catch (UnrecoverableEntryException uee) {
				throw uee;
			} catch (KeyStoreException kse) {
				throw kse;
			}
			privateKey = keyEnt.getPrivateKey();
		
		
		}catch(Exception e){
			log.error("Error :: getPrivateKeyFromKeyStore :: " + e.getMessage());
			throw new ApiUtilException("Error while getting Private Key from KeyStore",e);
		}
		
		log.debug("Exit :: getPrivateKeyFromKeyStore");
		
		return privateKey;
	}
	
	/**
	 * @param publicCertificateFileName
	 * @return
	 * @throws ApiUtilException
	 */
	public static PublicKey getPublicKeyFromX509Certificate(String publicCertificateFileName) throws ApiUtilException {
		log.debug("Enter :: getPublicKeyFromX509Certificate :: publicCertificateFileName : {} ", publicCertificateFileName);
		
		//Initialization
		FileInputStream fin = null;
		CertificateFactory f = null;
		PublicKey pk =  null;
		try{
		
			try {
				fin = new FileInputStream(publicCertificateFileName);
			} catch (FileNotFoundException fnfe) {
				throw fnfe;
			} 
			
			try {
				f = CertificateFactory.getInstance("X.509");
			} catch (CertificateException ce) {
				throw ce;
			} 
			X509Certificate certificate = null;
			try {
				certificate = (X509Certificate)f.generateCertificate(fin);
			} catch (CertificateException ce) {
				throw ce;
			} 
			pk = certificate.getPublicKey();
			
		}catch(Exception e){
			log.error("Error :: getPublicKeyFromX509Certificate :: " + e.getMessage());
			throw new ApiUtilException("Error while getting Public Key from X509 Certificate",e);
		}finally{
			if(null!=fin){
				try {
					fin.close();
				} catch (IOException e) {
					throw new ApiUtilException("Error while closing FileInputStream from X509 Certificate",e);
				}
			}
		}
		
		log.debug("Exit :: getPublicKeyFromX509Certificate");
		return pk;
	}
	
	/**
	 * @param baseString
	 * @param privateKey
	 * @return
	 * @throws ApiUtilException
	 */
	public static String getL2Signature(String baseString, PrivateKey privateKey) throws ApiUtilException
		{
			log.debug("Enter :: getL2Signature :: baseString : {} ", baseString);
			
			Signature rsa=null;
			byte[] encryptedData=null;
			String base64Token=null;
			try{
				//Validation
				if (baseString == null || baseString.isEmpty()) {
					throw new ApiUtilException("baseString must not be null or empty.");
				}
				
				if (privateKey == null) {
					throw new ApiUtilException("privateKey must not be null.");
				}
				
				try {
					rsa = Signature.getInstance("SHA256withRSA");
				} catch (NoSuchAlgorithmException nsae) {
					throw nsae;
				} 
				try {
					rsa.initSign(privateKey);
				} catch (InvalidKeyException ike) {
					throw ike;
				}
				try {
					rsa.update(baseString.getBytes());
				} catch (SignatureException se) {
					throw se;
				}
				
				try {
					encryptedData = rsa.sign();
				} catch (SignatureException se) {
					throw se;
				}
				log.debug("encryptedData length:" + encryptedData.length);
				
				base64Token = new String( Base64.getEncoder().encode(encryptedData) );
					
			}catch(ApiUtilException ae){
				log.error("Error :: getL2Signature :: " + ae.getMessage());
				throw ae;
			
			}catch(Exception e){
				log.error("Error :: getL2Signature :: " + e.getMessage());
				throw new ApiUtilException("Error during L2 Signature value generation",e);
			}
			
			log.debug("Exit :: getL2Signature :: base64Token : {} ", base64Token);
			return base64Token;
		}

	/**
	 * @param baseString
	 * @param signature
	 * @param publicKey
	 * @return
	 * @throws ApiUtilException
	 */
	public static boolean verifyL2Signature(String baseString, String signature, PublicKey publicKey) throws ApiUtilException {
		log.debug("Enter :: verifyL2Signature :: baseString  : {} , signature : {} ", baseString,signature);
		Signature publicSignature=null;
		boolean verified=false;
		try{	
			try {
				publicSignature = Signature.getInstance("SHA256withRSA");
			} catch (NoSuchAlgorithmException snae) {
				throw snae;
			}
		    try {
				publicSignature.initVerify(publicKey);
			} catch (InvalidKeyException e) {
				throw e;
			}
		    try {
				publicSignature.update(baseString.getBytes("UTF-8"));
			} catch (SignatureException se) {
				throw se;
			} catch (UnsupportedEncodingException uee) {
				throw uee;
			}
	
		    byte[] signatureBytes = Base64.getDecoder().decode(signature);
		    
		    log.debug("Exit :: verifyL2Signature");
		    try {
		    	verified=publicSignature.verify(signatureBytes);
			} catch (SignatureException se) {
				throw se;
			}
		}catch(Exception e){
			log.error("Error :: verifyL2Signature :: " + e.getMessage());
			throw new ApiUtilException("Error during L2 Signature verification",e);
		}
		
		log.debug("Exit :: verifyL2Signature");
		
		return verified;
	}

	/**
	 * @param authPrefix
	 * @param signatureMethod
	 * @param appId
	 * @param urlPath
	 * @param httpMethod
	 * @param formList
	 * @param nonce
	 * @param timestamp
	 * @return
	 * @throws ApiUtilException
	 */
	public static String getBaseString(String authPrefix
			, String signatureMethod
			, String appId
			, String urlPath
			, String httpMethod
			, ApiList formList
			, String nonce
			, String timestamp) throws ApiUtilException
		{
			log.debug("Enter :: getBaseString :: authPrefix  : {} , signatureMethod : {} , appId : {} , "
					+ "urlPath : {} , httpMethod : {} , nonce : {} , timestamp : {}", 
					authPrefix,signatureMethod,appId,urlPath,httpMethod,nonce,timestamp);
			
			String baseString = null;
			
			try{
			
				authPrefix = authPrefix.toLowerCase();
				
				// make sure that the url are valid
				URI siteUri = null;
				try {
					siteUri = new URI(urlPath);
				} catch (URISyntaxException e1) {
					throw e1;
				}
				log.debug("raw url:: {}", urlPath);
			
	
				log.debug("siteUri.getScheme():: {}", siteUri.getScheme());
				
	            if (!siteUri.getScheme().equals("http") && !siteUri.getScheme().equals("https"))
	            {
	               
					throw new ApiUtilException("Support http and https protocol only.");
					
	            }
	
	            // make sure that the port no and querystring are remove from url
	            String url = String.format("%s://%s%s", siteUri.getScheme(), siteUri.getHost(), siteUri.getPath());
				log.debug("url:: {}", url);
		
				// helper calss that handle parameters and form fields
				ApiList paramList = new ApiList();
	
				
				// process QueryString from url by transfering it to paramList
				if (siteUri.getQuery().length() > 1)
				{
					String queryString = siteUri.getRawQuery();
					log.debug("queryString:: {}", queryString);
	
					String[] paramArr = queryString.split("&");
					for (String item : paramArr)
					{
						log.debug("item:: {}", item);
						String[] itemArr = item.split("=");
						try {
							paramList.add(itemArr[0], java.net.URLDecoder.decode(itemArr[1], StandardCharsets.UTF_8.toString()));
						} catch (UnsupportedEncodingException e) {
							throw e;
						}
					}
					
				}
				
	
				// add the form fields to paramList
				if (formList != null && formList.size() > 0)
				{
					paramList.addAll(formList);
				}
	
				paramList.add(authPrefix + "_timestamp", timestamp);
				paramList.add(authPrefix + "_nonce", nonce);
				paramList.add(authPrefix + "_app_id", appId);
				paramList.add(authPrefix + "_signature_method", signatureMethod);
				paramList.add(authPrefix + "_version", "1.0");
				
				baseString = httpMethod.toUpperCase() + "&" + url + "&" + paramList.toString();
				
			}catch(ApiUtilException ae){
				log.error("Error :: getBaseString :: " + ae.getMessage());
				throw ae;
			}catch(Exception e){
				log.error("Error :: getBaseString :: " + e.getMessage());
				throw new ApiUtilException("Error while getting Base String",e);
			}
			
			log.debug("Exit :: getBaseString :: baseString : {} ", baseString);
			
			return baseString;
		}

	private static long getNewTimestamp() {
		return System.currentTimeMillis();
	}
	
	private static long getNewNonce() throws NoSuchAlgorithmException {
		long nonce = 0;
		
		nonce = SecureRandom.getInstance ("SHA1PRNG").nextLong();
		
		return nonce;
	}

	/**
	 * @param realm
	 * @param authPrefix
	 * @param httpMethod
	 * @param urlPath
	 * @param appId
	 * @param secret
	 * @param formList
	 * @param password
	 * @param alias
	 * @param fileName
	 * @param nonce
	 * @param timestamp
	 * @return
	 * @throws ApiUtilException
	 */
	public static String getToken (
        String realm
		, String authPrefix
		, String httpMethod
		, String urlPath
		, String appId
		, String secret
		, ApiList formList
		, String password
		, String alias
		, String fileName
		, String nonce
		, String timestamp) throws ApiUtilException
	{
		log.debug("Enter :: getToken :: realm : {} , authPrefix  : {} , appId : {} , "
				+ "urlPath : {} , httpMethod : {} , nonce : {} , timestamp : {} , secret : {} , password : {} , alias : {} , fileName : {}",
				realm, authPrefix,appId,urlPath,httpMethod,nonce,timestamp,secret,password,alias,fileName);
		
		String authorizationToken = null;
		String signatureMethod = "";
		String base64Token = "";
		
		try{
			
	        authPrefix = authPrefix.toLowerCase();
				
			// Generate the nonce value
	        try {
				nonce = nonce != null ? nonce : Long.toString(getNewNonce());
			} catch (NoSuchAlgorithmException nsae) {
				throw nsae;
			}
	        timestamp = timestamp != null ? timestamp : Long.toString(getNewTimestamp());
			
	
			
			if (secret != null)
			{
				signatureMethod = "HMACSHA256";
			}
			else
			{
				signatureMethod = "SHA256withRSA";
			}
	
			String baseString = getBaseString(authPrefix, signatureMethod
	            , appId, urlPath, httpMethod
	            , formList, nonce, timestamp);
			
			if (secret != null)
			{
				base64Token = getL1Signature(baseString, secret);
			}
			else
			{
				PrivateKey privateKey = getPrivateKeyFromKeyStore(fileName, password, alias);
				base64Token = getL2Signature(baseString, privateKey);
				
			}
	
			ApiList tokenList = new ApiList();
	
			tokenList.add("realm", realm);
			tokenList.add(authPrefix + "_timestamp", timestamp);
			tokenList.add(authPrefix + "_nonce", nonce);
			tokenList.add(authPrefix + "_app_id", appId);
			tokenList.add(authPrefix + "_signature_method", signatureMethod);
			tokenList.add(authPrefix + "_signature", base64Token);
			tokenList.add(authPrefix + "_version", "1.0");
	
			authorizationToken = String.format("%s %s", authPrefix.substring(0, 1).toUpperCase() + authPrefix.substring(1), tokenList.toString(", ", false, true));
			
        }catch(ApiUtilException ae){
			log.error("Error :: getToken :: " + ae.getMessage());
			throw ae;
		}catch(Exception e){
			log.error("Error :: getToken :: " + e.getMessage());
			throw new ApiUtilException("Error while getting Token",e);
		}	
		
		log.debug("Exit :: getToken :: authorizationToken : {} ", authorizationToken);
		
		return authorizationToken;
	}
	
	private static TrustManager[] getTrustManager(){
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]{
		    new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		    }
		};
		
		return trustAllCerts;
		
		
	}
	
	/**
	 * @param url
	 * @param token
	 * @param postData
	 * @param httpMethod
	 * @return
	 * @throws ApiUtilException
	 */
	public static int executeHttpRequest(String url, String token, String postData, String httpMethod) throws ApiUtilException {
		log.debug("Enter :: executeHttpRequest :: url  : {} , token : {} , postData : {} , httpMethod : {} ", url, token, postData , httpMethod);;
		int responseCode;
		
		try{
			if(null!=httpMethod&&"GET".equalsIgnoreCase(httpMethod)){
				
				log.info("Send Http GET request");
				responseCode = sendGet(url,token);
			}else{
				
				log.info("Send Http POST request");
				responseCode = sendPost(url,postData,token);
			}
		}catch (ApiUtilException e){
			log.error(e.getMessage(),e);
			throw e;
		}
		
		
		log.debug("Exit :: executeHttpRequest :: responseCode : {} ", responseCode);
		return responseCode;
	}
	
		// HTTP GET request
		private static int sendGet(String url, String token) throws ApiUtilException {

			
			URL obj = null;
			HttpURLConnection con = null;
			int responseCode = 0;
			BufferedReader in = null;
			try{
				
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, getTrustManager(), new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				
				try {
					obj = new URL(url);
				} catch (MalformedURLException e) {
					throw e;
				}
				
				try {
					con = (HttpURLConnection) obj.openConnection();
				} catch (IOException e) {
					throw e;
				}
	
				// optional default is GET
				try {
					con.setRequestMethod("GET");
				} catch (ProtocolException e) {
					throw e;
				}
	
				//add request header
				con.setRequestProperty("User-Agent", USER_AGENT);
	
				//add Token
				if(null!=token&&token.length()>0){
					con.setRequestProperty("Authorization", token);
				}
				
				try {
					responseCode = con.getResponseCode();
				} catch (IOException e) {
					throw e;
				}
				log.info("\nSending 'GET' request to URL : " + url);
				log.info("Response Code : " + responseCode);
	
				
				try {
					in = new BufferedReader(
					        new InputStreamReader(con.getInputStream()));
				} catch (IOException e) {
					throw e;
				}
				String inputLine;
				StringBuffer response = new StringBuffer();
	
				try {
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}

					in.close();
				} catch (IOException e) {
					throw e;
				}
				//print result
				log.info(response.toString());
			}catch(Exception e){
				throw new ApiUtilException("Error while sending HTTP GET method",e);
			}
			
			return responseCode;
		}
		
		
		// HTTP POST request
		private static int sendPost(String url,String postData,String token) throws ApiUtilException {
			
			URL obj = null;
			HttpsURLConnection con;
			int responseCode = 0;
			BufferedReader in = null;
			
			try{
				
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, getTrustManager(), new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				    
				try {
					obj = new URL(url);
				} catch (MalformedURLException e) {
					throw e;
				}
				
				try {
					con = (HttpsURLConnection) obj.openConnection();
				} catch (IOException e) {
					throw e;
				}
	
				//add request header
				try {
					con.setRequestMethod("POST");
				} catch (ProtocolException e) {
					throw e;
				}
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				
				//add Token
				if(null!=token&&token.length()>0){
					con.setRequestProperty("Authorization", token);
				}
				
				
				// Send post request
				if(null!=postData&&postData.length()>0){				
					byte[] byteArray = null;
					try {
						byteArray = postData.getBytes("UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw e;
					}
					con.setDoOutput(true);
					con.setRequestProperty("Content-Length", Integer.toString(byteArray.length));
					con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					OutputStream os = null;
					try {
						os = con.getOutputStream();
					} catch (IOException e) {
						throw e;
					}
					try {
						os.write(byteArray);
						os.close();
					} catch (IOException e) {
						throw e;
					}
				}
				
				con.setDoInput(true);
				
				try {
					responseCode = con.getResponseCode();
				} catch (IOException e) {
					throw e;
				}
				log.info("\nSending 'POST' request to URL : " + url);
				log.info("Post Data : " + postData);
				log.info("Response Code : " + responseCode);
	
				
				try {
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				} catch (IOException e) {
					throw e;
				}
				String inputLine;
				StringBuffer response = new StringBuffer();
	
				try {
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
				} catch (IOException e) {
					throw e;
				}
	
				//print result
				log.info(response.toString());
				
			}catch(Exception e){
				throw new ApiUtilException("Error while sending HTTP POST method",e);
			}
			
			return responseCode;
		}
		
}
