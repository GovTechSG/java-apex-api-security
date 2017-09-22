import static org.junit.Assert.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.api.util.ApiSecurity.ApiUtilException;
import org.junit.Test;

import com.api.util.ApiSecurity.ApiAuthorization;

/**
 * @author GDS-PDD
 *
 */
public class AuthorizationTokenTest {
	
	private final static String privateCertNameP12 = getLocalPath("certificates/alpha.test.p12");

    private final static String alias = "alpha";

    private final static boolean liveTest = false;

	private final static String realm = "http://example.api.test/token";
	private final static String authPrefixL1 = "Apex_l1_ig";
	private final static String authPrefixL2 = "Apex_l2_ig";
	private final static String httpMethod = "get";
	private final static String url = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
    private final static String appId = "example-4Swyn7qwKeO32EXdH1dKTeIQ";
	private final static String secret = "ffef0c5087f8dc24a3f122e1e2040cdeb5f72c73";
	private final static String nonce = "-5816789581922453013";
	private final static String timestamp = "1502199514462";
	private final static String passphrase = "passwordkey";

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
		String expectedToken = "Apex_l1_ig realm=\"http://example.api.test/token\", apex_l1_ig_timestamp=\"1502199514462\", apex_l1_ig_nonce=\"-5816789581922453013\", apex_l1_ig_app_id=\"example-4Swyn7qwKeO32EXdH1dKTeIQ\", apex_l1_ig_signature_method=\"HMACSHA256\", apex_l1_ig_signature=\"DoARux+dvq/A2ioQfRybInAQ4Lt4DTAI6DrDJRx7zcs=\", apex_l1_ig_version=\"1.0\"";

		String authorizationToken = ApiAuthorization.getToken(
            realm
			, authPrefixL1
			, httpMethod
			, url
			, appId
			, secret
			, null
			, null
			, null
			, null
			, nonce
			, timestamp
		);

		System.out.println("L1 Basic actualauthToken " + authorizationToken );

		assertEquals(expectedToken, authorizationToken);
	}

	@Test
	public void Test_L2_Basic_Test() throws ApiUtilException
	{
		String expectedToken = "Apex_l2_ig realm=\"http://example.api.test/token\", apex_l2_ig_timestamp=\"1502199514462\", apex_l2_ig_nonce=\"-5816789581922453013\", apex_l2_ig_app_id=\"example-4Swyn7qwKeO32EXdH1dKTeIQ\", apex_l2_ig_signature_method=\"SHA256withRSA\", apex_l2_ig_signature=\"YByxERvv+d1mN4PvUWq1iLm4aHiGeMZogpSuJIo1BzlPDa5tnMT81OrBIiK2tmX9uSxD0jlI3xaczF/uSSm7HJ/G+VdaHJOYrEGd7Jhpx7eR/g+WEhav5iUtU/8/qp4M0De/FOtzjEZ24ajDQ0fHskELe9P9WAOJ43wM7X6OoqXPYzcFukrxX9cjudQIPAxOfPTkC6K+NFW31lrxAzXQckPtkR1skLt+qfICprfTktTtdH/JeBoOSXlr6qqmbTvh0xDUVa9pd203jy8liNxnayGbk1TTiyyM8AtDkNiuEU7Cwo4sZvbsLtPnCWOHLnqcI8d+gDiXGoIk1mE02VbunA==\", apex_l2_ig_version=\"1.0\"";
		String authorizationToken = ApiAuthorization.getToken(
			realm
			, authPrefixL2
			, httpMethod
			, url
			, appId
            , null
            , null
            , passphrase
            , alias
            , privateCertNameP12
			, nonce
			, timestamp
		);

		System.out.println("L2 Basic actualauthToken " + authorizationToken );

		assertEquals(expectedToken, authorizationToken);
	}
	
	@Test
	public void Test_L2_Wrong_Password_Test() throws ApiUtilException
	{
		String expectedMessage = "keystore password was incorrect";
		
		try {
			ApiAuthorization.getToken(
				realm
				, authPrefixL2
				, httpMethod
				, url
				, appId
				, null
				, null
				, passphrase + "x"
				, alias
				, privateCertNameP12
				, null
				, null
	            );
		}
		catch (ApiUtilException expected)
		{
			assertEquals(expectedMessage, expected.getCause().getMessage());			
		}
	}

    @Test
    public void Test_L2_Not_Supported_Cert_Test() throws ApiUtilException
	{
        String fileName = getLocalPath("certificates/alpha.test.pem");

        String expectedMessage = "Invalid keystore format";
		
		try {

	        ApiAuthorization.getToken(
				realm
				, authPrefixL2
				, httpMethod
				, url
				, appId
				, null
				, null
				, passphrase
				, alias
				, fileName
				, null
				, null
				);		
	        }
		catch (ApiUtilException expected)
		{
			assertEquals(expectedMessage, expected.getCause().getMessage());			
		}
	}

    @Test
    public void Test_L2_Invalid_FileName_Test() throws ApiUtilException
	{
        String fileName = getLocalPath("Xalpha.test.p12");

        String expectedMessage = "No such file or directory";
		
		try {

	        ApiAuthorization.getToken(
				realm
				, authPrefixL2
				, httpMethod
				, url
				, appId
				, null
				, null
				, passphrase
				, alias
				, fileName
				, null
				, null
				);		
	        }
		catch (ApiUtilException expected)
		{
			assertTrue(expected.getCause().getMessage().contains(expectedMessage));			
		}
	}
    
    @Test
	public void Test_L0_Live_Test()
	{
		String urlPath = "https://example.lab:443/api/v1/rest/level0/in-in/?ap=裕廊坊%20心邻坊";

		if (liveTest)
		{
			try {
				assertEquals(200, ApiAuthorization.executeHttpRequest(urlPath, null,null,null));
			} catch (Exception e) {
				fail("Not Expecting ApiUtilException error.");
			}
		}
	}
    
    @Test
	public void Test_L1_Live_Test()
	{
		String urlPath = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";

		String authorizationToken=null;
		try {
			authorizationToken = ApiAuthorization.getToken(
				realm
				, authPrefixL1
				, httpMethod
				, urlPath
				, appId
				, secret, null, null, null, null, null, null
			);
		} catch (ApiUtilException e1) {
			fail("Not Expecting ApiUtilException error.");
		}

		if (liveTest)
		{
			try {
				assertEquals(200, ApiAuthorization.executeHttpRequest(urlPath, authorizationToken, null, httpMethod));
			} catch (Exception e) {
				fail("Not Expecting ApiUtilException error.");
			}
		}
	}
    
    @Test
	public void Test_L2_Live_Test()
	{
		String urlPath = "https://example.lab:443/api/v1/rest/level2/in-in/?ap=裕廊坊%20心邻坊";
		
		String authorizationToken = null;
		try {
			authorizationToken = ApiAuthorization.getToken(
				realm
				, authPrefixL2
				, httpMethod
				, urlPath
				, appId
				, null
				, null
				, passphrase
				, alias
				, privateCertNameP12
				, null
				, null
			);
		} catch (ApiUtilException e) {
			fail("Not Expecting ApiUtilException error.");
		}

		if (liveTest)
		{
			try {
				assertEquals(200, ApiAuthorization.executeHttpRequest(urlPath, authorizationToken,null,httpMethod));
			} catch (ApiUtilException e) {
				fail("Not Expecting ApiUtilException error.");
			}
		}
	}
    
}
