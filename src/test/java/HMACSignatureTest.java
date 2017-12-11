import static org.junit.Assert.*;
import java.util.ArrayList;

import com.api.util.ApiSecurity.ApiUtilException;
import org.junit.Test;

import com.api.util.ApiSecurity.ApiSigning;

/**
 * @author GDS-PDD
 *
 */
public class HMACSignatureTest {

    String baseString = "message";
    String secret = "secret";
    String expectedResult = "i19IcCmVwVmMVz2x4hhmqbgl1KeU0WnXBgoDYFeWNgs=";
    String expectedErrMsg = "message and secret must not be null or empty!";

    @Test
    public void L1_BaseString_IsNullOrEmpty_Test()
    {
    		String expectedMessage = "baseString must not be null or empty.";
    	
	    	try 
	    	{
	    		ApiSigning.getHMACSignature(null, secret);
	    		
	    		fail("Expecting ApiUtilException error.");
	    	}
	    	catch (ApiUtilException expected)
	    	{
	    		assertEquals(expectedMessage, expected.getMessage());
	    	}
	    	
	    	try 
	    	{
	    		ApiSigning.getHMACSignature("", secret);
	    		
	    		fail("Expecting ApiUtilException error.");
	    	}
	    	catch (ApiUtilException expected)
	    	{
	    		assertEquals(expectedMessage, expected.getMessage());
	    	}
    }
    
    @Test
    public void L1_Secret_IsNullOrEmpty_Test()
    {
    		String expectedMessage = "secret must not be null or empty.";
		
		try 
		{
			ApiSigning.getHMACSignature(baseString, null);
			
			fail("Expecting ApiUtilException error.");
		}
		catch (ApiUtilException expected)
		{
			assertEquals(expectedMessage, expected.getMessage());
		}
		
		try 
		{
			ApiSigning.getHMACSignature(baseString, "");
			
			fail("Expecting ApiUtilException error.");
		}
		catch (ApiUtilException expected)
		{
			assertEquals(expectedMessage, expected.getMessage());
		}
	}
    
    @Test
    public void L1_Verify_Signature_Test() throws ApiUtilException
    {
        assertTrue(ApiSigning.verifyHMACSignature(expectedResult, secret, baseString));
    }
    
	@Test
	public void L1_Verify_Signature_With_Wrong_Secret_Test() throws ApiUtilException
	{
        assertFalse(ApiSigning.verifyHMACSignature(expectedResult, secret + 'x', baseString));
	}
    
    @Test
    public void L1_Verify_Signature_With_Wrong_BaseString_Test() throws ApiUtilException
    {
        assertFalse(ApiSigning.verifyHMACSignature(expectedResult, secret, baseString + 'x'));
    }
    
	@Test
	public void L1_BaseString_Encoding_Test()
	{
		ArrayList<String[]> dataList = new ArrayList<String[]>();

		dataList.add(new String[]
		{
			"Lorem ipsum dolor sit amet, vel nihil senserit ei. Ne quo erat feugait disputationi.",
			secret,
			"cL3lY5/rhmkxMw/dCHCa4b9Lpp/soPPACnIxtQwRQI8=",
			"Basic Test"
		});
		
		// Chinese Traditional
		dataList.add(new String[]
		{
			"道続万汁国圭絶題手事足物目族月会済。",
			secret,
			"wOHv68zuoiIjfJHW0hZcOk4lORyiAL/IGK8WSkBUnuk=",
			"UTF8 (Chinese Traditional) Test"
		});

		// Japanese
		dataList.add(new String[]
		{
			"員ちぞど移点お告周ひょ球独狙チウソノ法保断フヒシハ東5広みぶめい質創ごぴ採8踊表述因仁らトつ。",
			secret,
			"L0ft4O8R2hxpupJVkLbgQpW0+HRw3KDgNUNf9DAEY7Y=",
			"UTF8 (Japanese) Test"
		});

		// Korean
		dataList.add(new String[]
		{
			"대통령은 즉시 이를 공포하여야 한다, 그 자율적 활동과 발전을 보장한다.",
			secret,
			"a6qt0t/nQ3GQFAEVTH+LMvEi0D41ZaKqC7LWJcVmHlE=",
			"UTF8 (Korean) Test"
		});

		// Greek
		dataList.add(new String[]
		{
			"Λορεμ ιπσθμ δολορ σιτ αμετ, τατιον ινιμιcθσ τε ηασ, ιν εαμ μοδο ποσσιμ ινvιδθντ.",
			secret,
			"WUGjbeO8Jy8Rvs5tD2biLHPR0+qtAmXeZKqX6acYL/4=",
			"UTF8 (Greek) Test"
		});

		dataList.add(new String[]
		{
			"GET&https://loadtest-pvt.api.lab/api/v1/rest/level1/in-in/&apex_l1_ig_app_id=loadtest-pvt-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l1_ig_nonce=-4985265956715077053&apex_l1_ig_signature_method=HMACSHA256&apex_l1_ig_timestamp=1502159855341&apex_l1_ig_version=1.0&param1=def+123",
			"ffef0c5087f8dc24a3f122e1e2040cdeb5f72c73",
			"jhf8sldyAqT2LYwROXpQW/k1vwK9WZsTi/JEovTutso=",
			"L1 BaseString Happy Path Test"
		});

		dataList.add(new String[]
		{
			"GET&https://loadtest-pvt.api.lab/api/v1/rest/level1/in-in/&ap=裕廊坊 心邻坊&apex_l1_ig_app_id=loadtest-pvt-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l1_ig_nonce=2851111144329605674&apex_l1_ig_signature_method=HMACSHA256&apex_l1_ig_timestamp=1502163903712&apex_l1_ig_version=1.0",
			"ffef0c5087f8dc24a3f122e1e2040cdeb5f72c73",
			"egtc4AdR5BQzhvLXH3hqw/JNcw6P3AmZROjX1Woi7QQ=",
			"L1 BaseString with UTF8 Parameters Test"
		});

		// excute test
		for (String[] item : dataList)
		{
			try {
				L1Test(item[0], item[1], item[2], item[3]);
			} catch (ApiUtilException e) {
				fail("Should not throw any exception during test execution");
			}
		}
	}
	
	private void L1Test(String baseString, String secret, String expectedSignature, String message) throws ApiUtilException
	{
		String signature = ApiSigning.getHMACSignature(baseString, secret);

		assertEquals(message, expectedSignature, signature);
	}
}
