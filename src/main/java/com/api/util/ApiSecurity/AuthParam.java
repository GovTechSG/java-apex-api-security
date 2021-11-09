package com.api.util.ApiSecurity;

import java.net.URI;
import java.security.PrivateKey;
/**
 * @author nsearch-sohboonkeong
 *
 */
public class AuthParam {
	
	public URI url;
    public String httpMethod;

    public String appName;
    public String appSecret;
    public FormList formData;
    public PrivateKey privateKey;
    public String nonce;
    public String timestamp;
    public SignatureMethod signatureMethod;
    public String version = "1.0";
    public AuthParam nextHop;
    
    public AuthParam() {
    	
    }

}



