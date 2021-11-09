package com.api.util.ApiSecurity;
import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

import com.api.util.ApiSecurity.ApiSigning;
import com.api.util.ApiSecurity.ApiList;
import com.api.util.ApiSecurity.ApiUtilException;

/**
 * @author GDS-PDD
 *
 */
public class BaseStringTest {

    @Test
    public void BaseString_Basic_Test() throws ApiUtilException
    {
        String url = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		String expectedBaseString = "GET&https://example.lab/api/v1/rest/level1/in-in/&ap=裕廊坊 心邻坊&apex_l1_ig_app_id=example-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l1_ig_nonce=1355584618267440511&apex_l1_ig_signature_method=HMACSHA256&apex_l1_ig_timestamp=1502175057654&apex_l1_ig_version=1.0";

        String baseString = ApiSigning.getBaseString(
            "Apex_L1_IG",
            "HMACSHA256",
            "example-4Swyn7qwKeO32EXdH1dKTeIQ",
            url,
            "get",
            null,
            "1355584618267440511",
            "1502175057654"
        );

        
        
        assertEquals(expectedBaseString, baseString);
    }

    @Test
    public void BaseString_New_Basic_Test() throws ApiUtilException
    {
        String url = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		String expectedBaseString = "GET&https://example.lab/api/v1/rest/level1/in-in/&ap=裕廊坊 心邻坊&apex_l1_ig_app_id=example-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l1_ig_nonce=1355584618267440511&apex_l1_ig_signature_method=HMACSHA256&apex_l1_ig_timestamp=1502175057654&apex_l1_ig_version=1.0";
	       
        try {
	        AuthParam authParam = new AuthParam();
	        
	        authParam.url = URI.create(url);
	        authParam.httpMethod = "GET";
	        authParam.appName = "example-4Swyn7qwKeO32EXdH1dKTeIQ";
	        authParam.nonce = "1355584618267440511";
	        authParam.timestamp = "1502175057654";
 
            AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
            assertEquals(expectedBaseString, authorizationToken.getBaseString());
        }
        catch (ApiUtilException e)
        {
            e.printStackTrace();
        }
        
    }
    
	@Test
	public void BaseString_FormData_Test() throws ApiUtilException
	{
		String url = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		String expectedBaseString = "POST&https://example.lab/api/v1/rest/level1/in-in/&ap=裕廊坊 心邻坊&apex_l1_ig_app_id=example-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l1_ig_nonce=6584351262900708156&apex_l1_ig_signature_method=HMACSHA256&apex_l1_ig_timestamp=1502184161702&apex_l1_ig_version=1.0&param1=data1";

		ApiList formList = new ApiList();
		formList.add("param1", "data1");

		String baseString = ApiSigning.getBaseString(
			"Apex_L1_IG",
			"HMACSHA256",
			"example-4Swyn7qwKeO32EXdH1dKTeIQ",
			url,
			"post",
			formList,
			"6584351262900708156",
			"1502184161702"
		);

		assertEquals(expectedBaseString, baseString);
	}
	
	@Test
	public void BaseString_New_FormData_Test() throws ApiUtilException
	{
		String url = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		String expectedBaseString = "POST&https://example.lab/api/v1/rest/level1/in-in/&ap=裕廊坊 心邻坊&apex_l1_ig_app_id=example-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l1_ig_nonce=6584351262900708156&apex_l1_ig_signature_method=HMACSHA256&apex_l1_ig_timestamp=1502184161702&apex_l1_ig_version=1.0&param1=data1";
        
		try {
			FormList formData = new FormList();
			formData.add("param1", "data1");
	
	        AuthParam authParam = new AuthParam();
	        
	        authParam.url = URI.create(url);
	        authParam.httpMethod = "POST";
	        authParam.appName = "example-4Swyn7qwKeO32EXdH1dKTeIQ";
	        authParam.nonce = "6584351262900708156";
	        authParam.timestamp = "1502184161702";
	        authParam.formData = formData;

            AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
            assertEquals(expectedBaseString, authorizationToken.getBaseString());
        }
        catch (ApiUtilException e)
        {
            e.printStackTrace();
        }
	}

	@Test
	public void BaseString_Invalid_Url_01_Test()
	{
		String url = "ftp://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";

		String expectedMessage = "Support http and https protocol only.";

		try {
	        ApiSigning.getBaseString(
				"Apex_L1_IG",
				"HMACSHA256",
				"example-4Swyn7qwKeO32EXdH1dKTeIQ",
				url,
				"post",
				null,
				"6584351262900708156",
				"1502184161702"
			);
		}
		catch (ApiUtilException expected)
		{
			assertEquals(expectedMessage, expected.getMessage());
		}
	}
	
	@Test
	public void BaseString_New_Invalid_Url_01_Test() throws ApiUtilException
	{
		String url = "ftp://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		String expectedMessage = "Support http and https protocol only.";
		
        try {
        AuthParam authParam = new AuthParam();
        
        authParam.url = URI.create(url);
        authParam.httpMethod = "POST";
        authParam.appName = "example-4Swyn7qwKeO32EXdH1dKTeIQ";
        authParam.nonce = "6584351262900708156";
        authParam.timestamp = "1502184161702";

            AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
        }
        catch (ApiUtilException e)
        {
            e.printStackTrace();
            assertEquals(expectedMessage, e.getMessage());
        }
	}

	@Test
	public void BaseString_Invalid_Url_02_Test()
	{
		String url = "://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		
		try {
	        ApiSigning.getBaseString(
				"Apex_L1_IG",
				"HMACSHA256",
				"example-4Swyn7qwKeO32EXdH1dKTeIQ",
				url,
				"post",
				null,
				"6584351262900708156",
				"1502184161702"
			);
		}
		catch (ApiUtilException expected)
		{
			if (!(expected.getCause() instanceof URISyntaxException))
			{
				fail("Expecting URISyntaxException error.");
			}
		}
	}
	
	@Test
	public void BaseString_New_Invalid_Url_02_Test() throws ApiUtilException
	{
		
		String url = "://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		
		try {
        AuthParam authParam = new AuthParam();
        
        authParam.url = URI.create(url);
        authParam.httpMethod = "POST";
        authParam.appName = "example-4Swyn7qwKeO32EXdH1dKTeIQ";
        authParam.nonce = "6584351262900708156";
        authParam.timestamp = "1502184161702";
        
            AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	if (!(e.getCause() instanceof URISyntaxException))
			{
				fail("Expecting URISyntaxException error.");
			}
            
        }
	}
	
	

}
