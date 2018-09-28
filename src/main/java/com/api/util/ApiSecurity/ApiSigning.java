package com.api.util.ApiSecurity;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;


/**
 * @author GDS-PDD
 */
public class ApiSigning {

    private static final Logger log = LoggerFactory.getLogger(ApiSigning.class);

    /**
     * Create HMACRSA256 Signature (L1) with a given basestring
     *
     * @param baseString Signature Basestring to be Signed
     * @param secret     App Secret
     * @return HMACSHA256 Signature
     * @throws ApiUtilException
     */
    public static String getHMACSignature(String baseString, String secret) throws ApiUtilException {
        log.debug("Enter :: getHMACSignature :: baseString : {} , secret: {} ", baseString, secret);

        //Initialization
        String base64Token = null;
        SecretKeySpec signingKey = null;
        Mac mac = null;
        byte[] rawHmac = null;

        try {
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

        } catch (ApiUtilException ae) {
            log.error("Error :: getHMACSignature :: " + ae.getMessage());
            throw ae;
        } catch (Exception e) {
            log.error("Error :: getHMACSignature :: " + e.getMessage());
            throw new ApiUtilException("Error during L1 Signature value generation", e);
        }

        log.debug("Exit :: getHMACSignature :: base64Token : {} ", base64Token);

        return base64Token;
    }


    /**
     * Verify HMACSHA256 Signature (L1)
     *
     * @param signature  Signature to be verified
     * @param secret     App's Secret
     * @param baseString Basestring to be signed and compare
     * @return
     * @throws ApiUtilException
     */
    public static boolean verifyHMACSignature(String signature, String secret, String baseString) throws ApiUtilException {
        log.debug("Enter :: verifyHMACSignature :: signature : {} , baseString : {} , secret: {} ", signature, baseString, secret);

        String expectedSignature = null;
        expectedSignature = getHMACSignature(baseString, secret);
        boolean verified = false;
        verified = expectedSignature.equals(signature);

        log.debug("Exit :: verifyHMACSignature :: boolean : {}", verified);

        return verified;
    }

    /**
     * Get RSA256 Signature (L2)
     *
     * @param baseString Basestring to be signed and compare
     * @param privateKey Private Key
     * @return
     * @throws ApiUtilException
     */
    public static String getRSASignature(String baseString, PrivateKey privateKey) throws ApiUtilException {
        log.debug("Enter :: getRSASignature :: baseString : {} ", baseString);

        Signature rsa = null;
        byte[] encryptedData = null;
        String base64Token = null;
        try {
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

            base64Token = new String(Base64.getEncoder().encode(encryptedData));

        } catch (ApiUtilException ae) {
            log.error("Error :: getRSASignature :: " + ae.getMessage());
            throw ae;

        } catch (Exception e) {
            log.error("Error :: getRSASignature :: " + e.getMessage());
            throw new ApiUtilException("Error during L2 Signature value generation", e);
        }

        log.debug("Exit :: getRSASignature :: base64Token : {} ", base64Token);
        return base64Token;
    }

    /**
     * Verify RSA256 Signature (L2)
     *
     * @param baseString Basestring to be signed and compare
     * @param signature  Signature to be verified
     * @param publicKey  Corresponding Public Key to verify the signature
     * @return
     * @throws ApiUtilException
     */
    public static boolean verifyRSASignature(String baseString, String signature, PublicKey publicKey) throws ApiUtilException {
        log.debug("Enter :: verifyRSASignature :: baseString  : {} , signature : {} ", baseString, signature);
        Signature publicSignature = null;
        boolean verified = false;
        try {
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

            log.debug("Exit :: verifyRSASignature");
            try {
                verified = publicSignature.verify(signatureBytes);
            } catch (SignatureException se) {
                throw se;
            }
        } catch (Exception e) {
            log.error("Error :: verifyRSASignature :: " + e.getMessage());
            throw new ApiUtilException("Error during L2 Signature verification", e);
        }

        log.debug("Exit :: verifyRSASignature");

        return verified;
    }

    /**
     * Get Private key from Keystore
     *
     * @param keystoreFileName Keystore file Path
     * @param password         Keystore passsword
     * @param alias            Keystore's alias
     * @return private key
     * @throws ApiUtilException
     */
    public static PrivateKey getPrivateKeyFromKeyStore(String keystoreFileName, String password, String alias) throws ApiUtilException {
        log.debug("Enter :: getPrivateKeyFromKeyStore :: keystoreFileName : {} , password: {} , alias: {} ", keystoreFileName, password, alias);
        //Initialization
        KeyStore ks = null;
        PrivateKey privateKey = null;
        java.io.FileInputStream fis = null;
        KeyStore.PrivateKeyEntry keyEnt = null;

        try {

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


        } catch (Exception e) {
            log.error("Error :: getPrivateKeyFromKeyStore :: " + e.getMessage());
            throw new ApiUtilException("Error while getting Private Key from KeyStore", e);
        }

        log.debug("Exit :: getPrivateKeyFromKeyStore");

        return privateKey;
    }
    
    /**
     * Get Public Key from Certificate
     *
     * @param publicCertificateFileName Certificate file path
     * @return Public Key
     * @throws ApiUtilException
     */
    public static PublicKey getPublicKeyFromX509Certificate(String publicCertificateFileName) throws ApiUtilException {
        log.debug("Enter :: getPublicKeyFromX509Certificate :: publicCertificateFileName : {} ", publicCertificateFileName);

        //Initialization
        FileInputStream fin = null;
        CertificateFactory f = null;
        PublicKey pk = null;
        try {

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
                certificate = (X509Certificate) f.generateCertificate(fin);
            } catch (CertificateException ce) {
                throw ce;
            }
            pk = certificate.getPublicKey();

        } catch (Exception e) {
            log.error("Error :: getPublicKeyFromX509Certificate :: " + e.getMessage());
            throw new ApiUtilException("Error while getting Public Key from X509 Certificate", e);
        } finally {
            if (null != fin) {
                try {
                    fin.close();
                } catch (IOException e) {
                    throw new ApiUtilException("Error while closing FileInputStream from X509 Certificate", e);
                }
            }
        }

        log.debug("Exit :: getPublicKeyFromX509Certificate");
        return pk;
    }
    
    /**
     * @param key
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static PublicKey getPublicKeyPEM(String publicCertificateFileName) throws IOException, GeneralSecurityException {
    	//log.debug("Enter :: getPublicKeyFromPubKey :: publicCertificateFileName : {} ", publicCertificateFileName);
    	
    	
    	log.debug("Enter :: getPublicKeyPEM :: publicCertificateFileName : {} ", publicCertificateFileName);
		PublicKey key = null;
		PEMParser pemParser = null;
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		try{
			File publicCertificateFile = new File(publicCertificateFileName); // private key file in PEM format
			pemParser = new PEMParser(new FileReader(publicCertificateFile));
			Object object = pemParser.readObject();
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
			SubjectPublicKeyInfo keyInfo;
			
			KeyPair kp = null;
			if(object instanceof SubjectPublicKeyInfo){
				keyInfo = (SubjectPublicKeyInfo) object;
				key = converter.getPublicKey(keyInfo);
			}else{
				kp = converter.getKeyPair(((PEMKeyPair) object));
				key = kp.getPublic();
			}
			
		}catch(Exception e){
			log.error(e.getMessage(),e);
			throw e;
		}finally{
			if(null!=pemParser){
				pemParser.close();
			}
		}     
        log.debug("Exit :: getPublicKeyPEM");
        
        return key;         
    }
    
    /**
     * Formulate Signature BaseString
     *
     * @param authPrefix Authorization Header scheme prefix , i.e 'prefix_appId'
     * @param signatureMethod Signature signing method
     * @param appId App ID
     * @param urlPath API Service URL
     * @param httpMethod HTTP Operation
     * @param formList form data
     * @param nonce Random Nonce
     * @param timestamp Timestamp
     *
     * @return Base String for signing
     * @throws ApiUtilException
     */
    public static String getBaseString(String authPrefix
            , String signatureMethod
            , String appId
            , String urlPath
            , String httpMethod
            , ApiList formList
            , String nonce
            , String timestamp) throws ApiUtilException {
        log.debug("Enter :: getBaseString :: authPrefix  : {} , signatureMethod : {} , appId : {} , "
                        + "urlPath : {} , httpMethod : {} , nonce : {} , timestamp : {}",
                authPrefix, signatureMethod, appId, urlPath, httpMethod, nonce, timestamp);

        String baseString = null;

        try {
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

            if (!siteUri.getScheme().equals("http") && !siteUri.getScheme().equals("https")) {
                throw new ApiUtilException("Support http and https protocol only.");
            }

            // make sure that the port no and querystring are remove from url
            String url = String.format("%s://%s%s", siteUri.getScheme(), siteUri.getHost(), siteUri.getPath());
            log.debug("url:: {}", url);

            // helper calss that handle parameters and form fields
            ApiList paramList = new ApiList();

            // process QueryString from url by transfering it to paramList
            if (null != siteUri.getQuery()) {
                String queryString = siteUri.getRawQuery();
                log.debug("queryString:: {}", queryString);

                String[] paramArr = queryString.split("&");
                for (String item : paramArr) {
                    log.debug("queryItem:: {}", item);
                    String[] itemArr = item.split("=");
                    try {
                    	if(itemArr.length == 1) {
                    		paramList.add(itemArr[0], "");
                    	}else {
                    		paramList.add(itemArr[0], java.net.URLDecoder.decode(itemArr[1], StandardCharsets.UTF_8.toString()));
                    	}
                        //paramList.add(itemArr[0], java.net.URLDecoder.decode(itemArr[1], StandardCharsets.UTF_8.toString()));
                    } catch (UnsupportedEncodingException e) {
                        throw e;
                    }
                }

            }

            // add the form fields to paramList
            if (formList != null && formList.size() > 0) {
                paramList.addAll(formList);
            }

            paramList.add(authPrefix + "_timestamp", timestamp);
            paramList.add(authPrefix + "_nonce", nonce);
            paramList.add(authPrefix + "_app_id", appId);
            paramList.add(authPrefix + "_signature_method", signatureMethod);
            paramList.add(authPrefix + "_version", "1.0");

            baseString = httpMethod.toUpperCase() + "&" + url + "&" + paramList.toString(true);

        } catch (ApiUtilException ae) {
        	ae.printStackTrace();
            log.error("Error :: getBaseString :: " + ae.getMessage() ,ae);
            throw ae;
        } catch (Exception e) {
        	e.printStackTrace();
            log.error("Error :: getBaseString :: " + e.getMessage());
            throw new ApiUtilException("Error while getting Base String", e);
        }

        log.debug("Exit :: getBaseString :: baseString : {} ", baseString);

        return baseString;
    }

    /**
     * Get Signature Token for HTTP Authorization Header
     *
     * @param realm Identifier for message that comes from the realm for your app
     * @param authPrefix Authorization Header scheme prefix , i.e 'prefix_appId'
     * @param httpMethod API Service URL
     * @param urlPath API Service endpoint
     * @param appId App's ID
     * @param secret App's Secret
     * @param formList Form Data
     * @param password Keystore's password
     * @param alias Keystore's Alias
     * @param fileName Private Keystore Filepath
     * @param nonce Random Nonce, Optional
     * @param timestamp Timestamp , Optional
     * @return
     * @throws ApiUtilException
     */
    public static String getSignatureToken(
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
            , String timestamp) throws ApiUtilException {
        log.debug("Enter :: getToken :: realm : {} , authPrefix  : {} , appId : {} , "
                        + "urlPath : {} , httpMethod : {} , nonce : {} , timestamp : {} , secret : {} , password : {} , alias : {} , fileName : {}",
                realm, authPrefix, appId, urlPath, httpMethod, nonce, timestamp, secret, password, alias, fileName);

        String authorizationToken = null;
        String signatureMethod = "";
        String base64Token = "";

        try {

            authPrefix = authPrefix.toLowerCase();

            // Generate the nonce value
            try {
                nonce = (nonce != null && !nonce.isEmpty())  ? nonce : getNewNonce();
            } catch (NoSuchAlgorithmException nsae) {
                throw nsae;
            }
            timestamp = timestamp != null ? timestamp : Long.toString(getNewTimestamp());

            if(authPrefix.toLowerCase().contains("l1")){
    			signatureMethod = "HMACSHA256";
    		}else if(authPrefix.toLowerCase().contains("l2")){
    			signatureMethod = "SHA256withRSA";
    		}else{
    			throw new ApiUtilException("Invalid Authorization Prefix.");
    		}

            String baseString = getBaseString(authPrefix, signatureMethod
                    , appId, urlPath, httpMethod
                    , formList, nonce, timestamp);

            if ("HMACSHA256".equals(signatureMethod)) {
                base64Token = getHMACSignature(baseString, secret);
            } else if("SHA256withRSA".equals(signatureMethod)){
            	PrivateKey privateKey = null;
            	if(null!=fileName && (fileName.contains(".key")||fileName.contains(".pem"))){
            		privateKey = ApiSigning.getPrivateKeyPEM(fileName, password);
            	}else{
            		//For JKS file
            		privateKey = ApiSigning.getPrivateKeyFromKeyStore(fileName, password, alias);
            	}
            			
                //PrivateKey privateKey = getPrivateKeyFromKeyStore(fileName, password, alias);
                base64Token = getRSASignature(baseString, privateKey);

            }

            ApiList tokenList = new ApiList();

            tokenList.add("realm", realm);
            tokenList.add(authPrefix + "_app_id", appId);
            tokenList.add(authPrefix + "_nonce", nonce);     
            tokenList.add(authPrefix + "_signature_method", signatureMethod);
            tokenList.add(authPrefix + "_timestamp", timestamp);
            tokenList.add(authPrefix + "_version", "1.0");
            tokenList.add(authPrefix + "_signature", base64Token);
            

            authorizationToken = String.format("%s %s", authPrefix.substring(0, 1).toUpperCase() + authPrefix.substring(1), tokenList.toString(", ", false, true, false));

        } catch (ApiUtilException ae) {
            log.error("Error :: getToken :: " + ae.getMessage());
            throw ae;
        } catch (Exception e) {
            log.error("Error :: getToken :: " + e.getMessage());
            throw new ApiUtilException("Error while getting Token", e);
        }

        log.debug("Exit :: getToken :: authorizationToken : {} ", authorizationToken);

        return authorizationToken;
    }

    private static long getNewTimestamp() {
        return System.currentTimeMillis();
    }

    private static String getNewNonce() throws NoSuchAlgorithmException {
        String nonce = null;
        byte[] b = new byte[32];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(b);
        nonce = Base64.getEncoder().encodeToString(b);
        
        return nonce;
    }


	public static PrivateKey getPrivateKeyPEM(String privateKeyFileName, String password) throws IOException, GeneralSecurityException{
		log.debug("Enter :: getPrivateKeyPEM :: privateKeyFileName : {} ", privateKeyFileName);
		PrivateKey key = null;
		PEMParser pemParser = null;
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		try{
			File privateKeyFile = new File(privateKeyFileName); // private key file in PEM format
			pemParser = new PEMParser(new FileReader(privateKeyFile));
			Object object = pemParser.readObject();
			PEMDecryptorProvider decProv = new    JcePEMDecryptorProviderBuilder().build(password.toCharArray());
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
			KeyPair kp = null;
			if (object instanceof PEMEncryptedKeyPair) {
			    kp = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
			}else{
				kp = converter.getKeyPair(((PEMKeyPair) object));
			}
			key = kp.getPrivate();
		}catch(Exception e){
			throw e;
		}finally{
			if(null!=pemParser){
				pemParser.close();
			}
		}
		log.debug("Exit :: getPrivateKeyPEM");
		
        return key;
        
	}

}
