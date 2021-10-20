package com.api.util.ApiSecurity;

import java.net.URI;
import java.security.PrivateKey;
/**
 * @author nsearch-sohboonkeong
 *
 */
public class AuthParam {
	
	public URI url;
//	String url;
    public String httpMethod;

    public String appName;
    public String appSecret;
    public FormList formData;
//    String password;
//    String alias;
//    String fileName;
    //to update fileName type to PrivateKey
    public PrivateKey privateKey;
    public String nonce;
    public String timestamp;
    public SignatureMethod signatureMethod;
    public String version = "1.0";
    public AuthParam nextHop;
    
    public AuthParam() {
    	
    }
    
 
//	private URI url;
////	String url;
//    private String httpMethod;
//
//    private String appName;
//    private String appSecret;
//    private FormData formData;
////    String password;
////    String alias;
////    String fileName;
//    //to update fileName type to PrivateKey
//    private  PrivateKey privateKey;
//    private String nonce;
//    private String timestamp;
//    //private SignatureMethod signatureMethod;
//    private String version = "1.0";
//    private AuthParam nextHop;
//    
//    private AuthParam(AuthParamBuilder builder) {
//    	this.url = builder.url;
//    	this.httpMethod = builder.httpMethod;
//    	this.appName = builder.appName;
//    	this.appSecret = builder.appSecret;
//    	this.formData = builder.formData;
//    	this.privateKey = builder.privateKey;
//    	this.nonce = builder.nonce;
//    	this.timestamp = builder.timestamp;
//    	this.version = builder.version;
//    	this.nextHop = builder.nextHop;
//    }
//    
//    
//    public URI getUrl() {
//    	return url;
//    }
//    public String getHttpMethod() {
//    	return httpMethod;
//    }
//    public String getAppName() {
//    	return appName;
//    }
//    public String getAppSecret() {
//    	return appSecret;
//    }
//    public FormList getFormData() {
//    	return formData;
//    }
//    public PrivateKey getPrivateKey() {
//    	return privateKey;
//    }
//    public String getNonce() {
//    	return nonce;
//    }
//    public String getTimestamp() {
//    	return timestamp;
//    }
//    public String getVersion() {
//    	return version;
//    }
//    public AuthParam getNextHop() {
//    	return nextHop;
//    }
    
//    public void setUrl(URI url) {
//      this.url = url;
//    }
//    public void setHttpMethod(String httpMethod) {
//      this.httpMethod = httpMethod;
//    }
//    public void setAppName(String appName) {
//      this.appName = appName;
//    }
//    public void setAppSecret(String appSecret) {
//      this.appSecret = appSecret;
//    }
//    public void setFormData(FormData formData) {
//      this.formData = formData;
//    }
//    public void setPrivateKey(PrivateKey privateKey) {
//      this.privateKey = privateKey;
//    }
//    public void setNonce(String nonce) {
//      this.nonce = nonce;
//    }
//    public void setTimestamp(String timestamp) {
//      this.timestamp = timestamp;
//    }
//    public void setVersion(String version) {
//    	this.version = version;
//    }
//    public void setNextHop(AuthParam nextHop) {
//    	this.nextHop = nextHop;
//    }
//    
//    public static class AuthParamBuilder 
//    {
//    	private URI url;
//        private String httpMethod;
//        private String appName;
//        private String appSecret;
//        private FormData formData;
//        private PrivateKey privateKey;
//        private String nonce;
//        private String timestamp;
//        private String version;
//        private AuthParam nextHop;
// 
//        public AuthParamBuilder(URI url) {
//            this.url = url;
//        }
//        public AuthParamBuilder httpMethod(String httpMethod) {
//            this.httpMethod = httpMethod;
//            return this;
//        }
//        public AuthParamBuilder phone(String appName) {
//            this.appName = appName;
//            return this;
//        }
//        public AuthParamBuilder address(String appSecret) {
//            this.appSecret = appSecret;
//            return this;
//        }
//        public AuthParamBuilder formData(FormData formData) {
//            this.formData = formData;
//            return this;
//        }
//        public AuthParamBuilder privateKey(PrivateKey privateKey) {
//            this.privateKey = privateKey;
//            return this;
//        }
//        public AuthParamBuilder nonce(String nonce) {
//            this.nonce = nonce;
//            return this;
//        }
//        public AuthParamBuilder timestamp(String timestamp) {
//            this.timestamp = timestamp;
//            return this;
//        }
//        public AuthParamBuilder version(String version) {
//            this.version = version;
//            return this;
//        }
//        public AuthParamBuilder nextHop(AuthParam nextHop) {
//            this.nextHop = nextHop;
//            return this;
//        }
//        
//        
//        //Return the finally constructed User object
//        public AuthParam build() {
//        	AuthParam authParam =  new AuthParam(this);
//            validateAuthParamObject(authParam);
//            return authParam;
//        }
//        private void validateAuthParamObject(AuthParam authParam) {
//            //Do some basic validations to check 
//            //if user object does not break any assumption of system
//        }
//    }
//    
}





//, String authPrefix
//, String httpMethod
//, String urlPath
//, String appId
//, String secret
//, ApiList formList
//, String password
//, String alias
//, String fileName
//, String nonce
//, String timestamp
//
//
//String authPrefix
//, String signatureMethod
//, String appId
//, String urlPath
//
//, ApiList formList
//, String nonce
//, String timestam
//
//
//
//String realm
//, String authPrefix
//, String httpMethod
//, String urlPath
//, String appId
//, String secret
//, ApiList formList
//, String password
//, String alias
//, String fileName
//, String nonce
//, String timestamp
//
//
//thPrefix
//, HttpMethod httpMethod
//, Uri urlPath
//, string appId
//, string secret = null
//, ApiList formList = null
//, RSACryptoServiceProvider privateKey = null
//, string nonce = null
//, string timestamp = null
//, string version = "1.0"