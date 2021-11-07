package com.api.util.ApiSecurity;
import org.junit.Test;
import java.net.URI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import static org.junit.Assert.*;

/**
 * @author GDS-ENP
 *
 */
public class AuthorizationTokenTest {

	private final static String privateCertNameP12 = getLocalPath("certificates/ssc.alpha.example.com.p12");

    private final static String alias = "alpha";

    private final static boolean liveTest = false;
	
//	private final static String realm = "http://example.api.test/token";
//	private final static String authPrefixL1 = "Apex_l1_ig";
//	private final static String authPrefixL2 = "Apex_l2_ig";
	private final static String httpMethod = "get";
	private final static String urlString = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
	private final static URI url = URI.create(urlString);
    private final static String appId = "example-4Swyn7qwKeO32EXdH1dKTeIQ";
	private final static String secret = "ffef0c5087f8dc24a3f122e1e2040cdeb5f72c73";
	private final static String nonce = "-5816789581922453013";
	private final static String timestamp = "1502199514462";
	private final static String passphrase = "passwordp12";
	
	

    private static String getLocalPath(String relativeFileName)
	{
		Path currentRelativePath = Paths.get("");
		String s = combine(currentRelativePath.toAbsolutePath().toString(), relativeFileName.replaceAll("/", File.separator));
		
		return s;
	}
    
    public static String combine(String... paths)
    {
        File file = new File(File.separator);

        for (int i = 0; i < paths.length ; i++) {
            file = new File(file, paths[i]);
        }

        return file.getPath();
    }
    
    @Test
	public void Test_L1_Basic_Test() throws ApiUtilException
	{
		String expectedToken = "Apex_l1_eg realm=\"https://example.lab\", apex_l1_eg_app_id=\"example-4Swyn7qwKeO32EXdH1dKTeIQ\", apex_l1_eg_nonce=\"-5816789581922453013\", apex_l1_eg_signature_method=\"HMACSHA256\", apex_l1_eg_timestamp=\"1502199514462\", apex_l1_eg_version=\"1.0\", apex_l1_eg_signature=\"Ili+E6mPb96dkzZM7yJaKPiGA6JjbR/B25oVmkp/Rxs=\"";

		AuthParam authParam = new AuthParam();

		authParam.url = url;
		authParam.httpMethod = httpMethod;
		authParam.appName = appId;
		authParam.appSecret = secret;
		authParam.nonce = nonce;
		authParam.timestamp = timestamp;
		
//		String authorizationToken = ApiSigning.getSignatureToken(
//            realm
//			, authPrefixL1
//			, httpMethod
//			, url
//			, appId
//			, secret
//			, null
//			, null
//			, null
//			, null
//			, nonce
//			, timestamp
//		);
		AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
//		System.out.println("expectedToken:"+expectedToken);
//		System.out.println("authorizationToken:"+authorizationToken);
		assertEquals(expectedToken, authorizationToken.getToken());
	}

	@Test
	public void Test_L2_Basic_Test() throws ApiUtilException, IOException, GeneralSecurityException
	{
		String expectedToken = "Apex_l2_eg realm=\"https://example.lab\", apex_l2_eg_app_id=\"example-4Swyn7qwKeO32EXdH1dKTeIQ\", apex_l2_eg_nonce=\"-5816789581922453013\", apex_l2_eg_signature_method=\"SHA256withRSA\", apex_l2_eg_timestamp=\"1502199514462\", apex_l2_eg_version=\"1.0\", apex_l2_eg_signature=\"aFbjNdDoSRPNcy6amYoS+gThv8q+rJhbxBWs7oyU+BN7bhkn1C+BSfQUUGb+oh1R+gKfoBwpdNptSvkCBpopzAmCRc6m79Haki4CFeWIDvqlyzbIp1MQBj36giOOxBrfijEtL5LdXG+lOb5D+WVJaOx6xstnumy1HsMdjG0mp0qbTzG2wTKNPN09/UG3wyChg7eGMtrif93gIXA7gIZmXNrm38qXJsz5A99b/PoROMS1koxK3YFkVSJ7o8cC/XDu1FJWS2Fz/U4rBQPjz1GrzUr1G0xrfeKiUX/UREVkoEvb9403LZjUP/t1uDCdFWA1ugspnHMVyePCierRtdEZ29dEBAB9jUGNJjp03xwyfG/ZLph3kknuLlLjMQoqG6RZ4USVQgIb1kENLnNxlWcR7NdMM9ebqiTNrpWCTxiSvkILN1ybX6DLvPHxXJdPP5cFHgwW4WsZV2HHzrrGLLXzis/marwsC/90cemCMO5vic71RPlqmv/xD2gMZvJ0sEjRDQ4HiCNp9CWhSy4tdu7EpZ7qeh4ecu2o1DUQdauLNFQwNUgh3K2uByOZ8TPCGjRnSqYply98BUIxHECviI77YK8Tr45GsalT+8Q5e2HMWJV/mcNHbSlz1WxT+hhc8DdBKqf23TUOgi2N/EuzU5X0DffUHAJBx9MHSpI4pMGW+TQ=\"";
//		String authorizationToken = ApiSigning.getSignatureToken(
//			realm
//			, authPrefixL2
//			, httpMethod
//			, url
//			, appId
//            , null
//            , null
//            , passphrase
//            , alias
//            , privateCertNameP12
//			, nonce
//			, timestamp
//		);

		AuthParam authParam = new AuthParam();

		authParam.url = url;
		authParam.httpMethod = httpMethod;
		authParam.appName = appId;
		// authParam.privateKey;
		authParam.nonce = nonce;
		authParam.timestamp = timestamp;

     	authParam.privateKey = ApiSigning.getPrivateKey(privateCertNameP12, passphrase, alias);
		AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
		// System.out.println("Expected Token Test_L2_Basic_Test:" + authorizationToken.getToken());
		assertEquals(expectedToken, authorizationToken.getToken());
	}
	
	@Test
	public void Test_L2_Wrong_Password_Test() throws ApiUtilException
	{
		String expectedMessage = "keystore password was incorrect";
		
		try {
//			ApiSigning.getSignatureToken(
//				realm
//				, authPrefixL2
//				, httpMethod
//				, url
//				, appId
//				, null
//				, null
//				, passphrase + "x"
//				, alias
//				, privateCertNameP12
//				, null
//				, null
//	            );
			AuthParam authParam = new AuthParam();
			
			authParam.url = url;
			authParam.httpMethod = httpMethod;
			authParam.appName = appId;
			String wrongPassphrase = passphrase + "x";
	     	authParam.privateKey = ApiSigning.getPrivateKey(privateCertNameP12, wrongPassphrase, alias);
			ApiSigning.getSignatureTokenV2(authParam);
		}
		catch (ApiUtilException expected)
		{
			assertEquals(expectedMessage, expected.getCause().getMessage());			
		}
	}

    @Test
    public void Test_L2_Not_Supported_Cert_Test() throws ApiUtilException
//    , IOException, GeneralSecurityException
	{
		String fileName = getLocalPath("certificates/ssc.alpha.example.com.pem");
    	String expectedMessage = "unable to read encrypted data: Error finalising cipher";
    	//String fileName = getLocalPath("certificates/team20.pem");
        //String expectedMessage = "org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo cannot be cast to org.bouncycastle.openssl.PEMKeyPair";
				
		try {

//	        ApiSigning.getSignatureToken(
//				realm
//				, authPrefixL2
//				, httpMethod
//				, url
//				, appId
//				, null
//				, null
//				, passphrase
//				, alias
//				, fileName
//				, null
//				, null
//				);		
			AuthParam authParam = new AuthParam();
			
			authParam.url = url;
			authParam.httpMethod = httpMethod;
			authParam.appName = appId;
//	     	try {
//				authParam.privateKey = ApiSigning.getPrivateKey(fileName, passphrase, alias);
//			} catch (IOException e) {
//				System.out.println("WHAT IS THE ERROR MESSAGE: IOIO ");
//				System.out.println("WHAT IS THE ERROR MESSAGE: IOIO "+ e.getCause().getMessage());
//				// TODO Auto-generated catch block
//				throw new ApiUtilException(e.getCause().getMessage(),e);
////				e.printStackTrace();
//			} catch (GeneralSecurityException e) {
//				System.out.println("WHAT IS THE ERROR MESSAGE: EEE ");
//				System.out.println("WHAT IS THE ERROR MESSAGE: EEE "+ e.getCause().getMessage());
//				// TODO Auto-generated catch block
////				e.printStackTrace();
//				throw new ApiUtilException(e.getCause().getMessage(),e);
//			} catch (ClassCastException e) {
//				System.out.println("WHAT IS THE ERROR MESSAGE: EEE ");
//				System.out.println("WHAT IS THE ERROR MESSAGE: EEE "+ e.getMessage());
//				// TODO Auto-generated catch block
////				e.printStackTrace();
//				throw new ApiUtilException(e.getMessage(),e);
//			}
			authParam.privateKey = ApiSigning.getPrivateKey(fileName, passphrase, alias);
	     	ApiSigning.getSignatureTokenV2(authParam);
	     	
	         }
		 catch (ApiUtilException expected)
		 {
		 	assertEquals(expectedMessage, expected.getCause().getMessage());
			
		 }
	}

    @Test
    public void Test_L2_Invalid_FileName_Test() throws ApiUtilException, IOException, GeneralSecurityException
	{
        String fileName = getLocalPath("Xalpha.test.p12");

        String expectedMessage = "No such file or directory";
		
		try {

//	        ApiSigning.getSignatureToken(
//				realm
//				, authPrefixL2
//				, httpMethod
//				, url
//				, appId
//				, null
//				, null
//				, passphrase
//				, alias
//				, fileName
//				, null
//				, null
//				);		
			AuthParam authParam = new AuthParam();
			
			authParam.url = url;
			authParam.httpMethod = httpMethod;
			authParam.appName = appId;
	     	authParam.privateKey = ApiSigning.getPrivateKey(fileName, passphrase, alias);
	     	ApiSigning.getSignatureTokenV2(authParam);
	        }
		catch (ApiUtilException expected)
		{
			assertTrue(expected.getCause().getMessage().contains(expectedMessage));			
		}
	}
    
}
