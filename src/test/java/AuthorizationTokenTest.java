import com.api.util.ApiSecurity.ApiSigning;
import com.api.util.ApiSecurity.ApiUtilException;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author GDS-PDD
 *
 */
public class AuthorizationTokenTest {

	private final static String privateCertNameP12 = getLocalPath("certificates/ssc.alpha.example.com.p12");

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
		String expectedToken = "Apex_l1_ig realm=\"http://example.api.test/token\", apex_l1_ig_timestamp=\"1502199514462\", apex_l1_ig_nonce=\"-5816789581922453013\", apex_l1_ig_app_id=\"example-4Swyn7qwKeO32EXdH1dKTeIQ\", apex_l1_ig_signature_method=\"HMACSHA256\", apex_l1_ig_signature=\"DoARux+dvq/A2ioQfRybInAQ4Lt4DTAI6DrDJRx7zcs=\", apex_l1_ig_version=\"1.0\"";

		String authorizationToken = ApiSigning.getToken(
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
		System.out.println("expectedToken:"+expectedToken);
		System.out.println("authorizationToken:"+authorizationToken);
		assertEquals(expectedToken, authorizationToken);
	}

	@Test
	public void Test_L2_Basic_Test() throws ApiUtilException
	{
		String expectedToken = "Apex_l2_ig realm=\"http://example.api.test/token\", apex_l2_ig_timestamp=\"1502199514462\", apex_l2_ig_nonce=\"-5816789581922453013\", apex_l2_ig_app_id=\"example-4Swyn7qwKeO32EXdH1dKTeIQ\", apex_l2_ig_signature_method=\"SHA256withRSA\", apex_l2_ig_signature=\"Za7B8MaOlGZjc8DTEh9HwhcL+5DiiuTMy+s0bQ8/lajy1Ug64gPCyNEbcYkD/XBEHFyg6vlY9/J85Y+Ui6DeYbXmUFnQjDWdOKf13xJvpsnAQgOqWi+LSc0+gy3pvsQ50nyES3E04vb3RvGwd7UC6SyBhmQ5P8Mz0UUgWBX6L6N3n+xergTg3DKWEPyQih+dqN3DkOmNE8fstAp+HOqiVq2OBxNeg9x5Kp0tq2vka7cC86zdYSNhsQR+D7hC+S1NPninWvdxUF1EwrPrEZYSYXka0Md1XFVjaL6b0htcFo6LxwJ8X6wsOqS4g4qmrAadwm7fITZLxcI0Zdaz7dRw9UFUsGWEVPG8MQztVXleimDxYvorLKTD5bhWGHe+XNwyL+IdR7ErooOHP9pTslJ7yBEmsePTRIAL//h0AEXaBN4pCmBPJnVtYtUWdQsUq/iv/4FLtWvOK77EReAtq3uqndJfGInXUMESqS4PzGDajTZj+oDP7xektLh7umELQBnSKNuv3BR9H63sf+Z9mZQ1531LYEmQWR8p3LCP8E0DcROo0OP1gcE76N9Z1HKLtJjLYDRyQRUQMM2FlJRkb3sy2g60yNThkPprzohBvHowCRFs02tlkyBbOuKC2cV9hwSz8eMqhUTzNn/WMi2Dr2V7iTJtyJHT9kdebVY2Cvnlt5I=\", apex_l2_ig_version=\"1.0\"";
		String authorizationToken = ApiSigning.getToken(
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

		System.out.println("Expected Token:" + authorizationToken);
		assertEquals(expectedToken, authorizationToken);
	}
	
	@Test
	public void Test_L2_Wrong_Password_Test() throws ApiUtilException
	{
		String expectedMessage = "keystore password was incorrect";
		
		try {
			ApiSigning.getToken(
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
		String fileName = getLocalPath("certificates/ssc.alpha.example.com.pem");

        String expectedMessage = "Invalid keystore format";
		
		try {

	        ApiSigning.getToken(
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

	        ApiSigning.getToken(
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
    
}
