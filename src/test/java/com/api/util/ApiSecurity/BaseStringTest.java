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
	
	
	@Test
	public void BaseString_New_FormData_Test2() throws ApiUtilException
	{
		String url = "https://example.lab:443/api/v1/rest/level1/in-in/?ap=裕廊坊%20心邻坊";
		String expectedBaseString = "POST&https://example.lab/api/v1/rest/level1/in-in/&ap=裕廊坊 心邻坊&apex_l1_ig_app_id=example-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l1_ig_nonce=6584351262900708156&apex_l1_ig_signature_method=HMACSHA256&apex_l1_ig_timestamp=1502184161702&apex_l1_ig_version=1.0&param1=data1";
        
//
			FormList formData = new FormList();
			formData.add("phoneNo", "+1 1234 4567 890");
		    formData.add("street", "Hellowood Street");
		    formData.add("state", "AP");
//
//	        String formString = formData.toFormData();
//	        // phoneNo=%2B1+1234+4567+890&street=Hellowood+Street&state=AP
//	        // phoneNo=%2B1+1234+4567+890&street=Hellowood+Street&state=AP
//	        
//	        
	        ApiList  queryData = new ApiList();
	        queryData.add("clientId", "1256-1231-4598");
	        queryData.add("accountStatus", "active");
	        queryData.add("txnDate", "2017-09-29");
//
	        String queryString = queryData.toString(true);
//
	        String baseUrl = String.format("https://example.com/resource?%s", queryString);
	      //   https://example.com/resource?accountStatus=active&clientId=1256-1231-4598&txnDate=2017-09-29
		
		AuthParam authParam = new AuthParam();
		
		authParam.url = URI.create(baseUrl);
		authParam.httpMethod = "GET";
		authParam.appName = "iuhuh2";
//		authParam.appSecret = "iuhuh2";
		String certFileName = "src/main/resources/test-suites/cert/alpha/key.alpha.pkcs8.encrypted.pem";
		String password = "passwordkey";
		//String alias = "alpha";
		authParam.privateKey = ApiSigning.getPrivateKey(certFileName, password);
		authParam.formData = formData;
		
		
		AuthParam authParam1 = new AuthParam();
		authParam1.httpMethod = "GET";
		authParam1.appName = "L1";
		authParam1.appSecret = "pwpwpw";
		String baseUrl1 = String.format("https://example-pvt.com/resource?%s", queryString);
		authParam1.url = URI.create(baseUrl1);
		authParam1.nextHop = authParam;

		// get the authorization token for L1
		AuthToken authorizationToken = ApiSigning.getSignatureTokenV2(authParam1);
		// Add this signature value to the authorization header when sending the request.
		// authorizationToken.getToken();
		
		// make api call with authorizationToken.getToken()
	        
	        System.out.println("formData121 test token " + authorizationToken.getToken());
	        System.out.println("formData121 test bs " + authorizationToken.getBaseString());
  
	}
	

}
