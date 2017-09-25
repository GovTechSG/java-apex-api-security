import static org.junit.Assert.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import com.api.util.ApiSecurity.ApiUtilException;
import org.junit.Test;

import com.api.util.ApiSecurity.ApiAuthorization;

/**
 * @author GDS-PDD
 *
 */
public class L2SignatureTest {
	
	// file name follow unix convention...
	private static final String privateCertName = getLocalPath("certificates/alpha.test.p12");
	private static final String publicCertName = getLocalPath("certificates/alpha.test.cer");

	private static final String baseString = "message";
	private static final String password = "passwordkey";
	private static final String alias = "alpha";

	static final PrivateKey privateKey = getPrivateKeyLocal(privateCertName, password, alias);
	static final PublicKey publicKey = getPublicKeyLocal(publicCertName);

	private static final String message = "Lorem ipsum dolor sit amet, vel nihil senserit ei. Ne quo erat feugait disputationi.";
	private static final String expectedSignature = "o6Z6W8JzBgxjq1WpW7l4LR8rVWyl8wHAPrPkCJ9Jmz1P3jX4EmF+4e7+X8dX4JDQzxrAVErGJb15DpqGDnYfhozCIm68UswYEKsUFRJTC1X7cFDSP6WcIjBU9tfw1BBYQdLK5EzzXKudXayRSq2E6a9Pqlu0UodMjJpkdyT5HnOKzs+ao72tloJROctBGsE8rX/rURrhx5qVWJg1jnn8GbOexPHTvaM5vzdWMwFfNKPjBXYj1YwmCt+EFBb2W9pvKzIVsCQ5M+r2hNl2FRInq41v61xpiwMSLxpHdDXz7YYZtSByWH7/0mjwt86EPwkes9Bj5AOO6ZXUjuDiyGAjUQ==";

	private static PrivateKey getPrivateKeyLocal(String keystoreFileName, String password, String alias)
	{
		try {
			return ApiAuthorization.getPrivateKeyFromKeyStore(privateCertName, password, alias);
		} catch (ApiUtilException e) {
			fail("Should not throw any exception during test execution");
		}
		return null;
	}
	
	private static PublicKey getPublicKeyLocal(String publicCertificateFileName)
	{
		try {
			return ApiAuthorization.getPublicKeyFromX509Certificate(publicCertificateFileName);
		} catch (ApiUtilException e) {
			fail("Should not throw any exception during test execution");
		}
		return null;
	}

	private static String getLocalPath(String relativeFileName)
	{
		Path currentRelativePath = Paths.get("");
		String s = combine(currentRelativePath.toAbsolutePath().toString(), relativeFileName.replaceAll("/", File.separator));
		
		//log.error("Current relative path is: " + s);
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
	public void L2_BaseString_IsNullOrEmpty_Test()
	{
		String expectedMessage = "baseString must not be null or empty.";
    	
	    	try 
	    	{
	    		ApiAuthorization.getL2Signature(null, privateKey);
	    		
	    		fail("Expecting ApiUtilException error.");
	    	}
	    	catch (ApiUtilException expected)
	    	{
	    		assertEquals(expectedMessage, expected.getMessage());
	    	}
	    	
	    	try 
	    	{
	    		ApiAuthorization.getL2Signature("", privateKey);
	    		
	    		fail("Expecting ApiUtilException error.");
	    	}
	    	catch (ApiUtilException expected)
	    	{
	    		assertEquals(expectedMessage, expected.getMessage());
	    	}		
	}
	
	@Test
	public void L2_PrivateKey_IsNull_Test()
	{
		String expectedMessage = "privateKey must not be null.";
    	
	    	try 
	    	{
	    		ApiAuthorization.getL2Signature(baseString, null);
	    		
	    		fail("Expecting ApiUtilException error.");
	    	}
	    	catch (ApiUtilException expected)
	    	{
	    		assertEquals(expectedMessage, expected.getMessage());
	    	}
	}

	@Test
	public void L2_Verify_Signature_Test() 
	{
        try {
			assertTrue(ApiAuthorization.verifyL2Signature(message, expectedSignature, publicKey));
		} catch (ApiUtilException e) {
			fail("Should not throw any exception during test execution");
		}
	}


	@Test
	public void L2_Basic_Test() 
	{
		ArrayList<String[]> dataList = new ArrayList<String[]>();

		dataList.add(new String[]
		{
			"Lorem ipsum dolor sit amet, vel nihil senserit ei. Ne quo erat feugait disputationi.",
			"o6Z6W8JzBgxjq1WpW7l4LR8rVWyl8wHAPrPkCJ9Jmz1P3jX4EmF+4e7+X8dX4JDQzxrAVErGJb15DpqGDnYfhozCIm68UswYEKsUFRJTC1X7cFDSP6WcIjBU9tfw1BBYQdLK5EzzXKudXayRSq2E6a9Pqlu0UodMjJpkdyT5HnOKzs+ao72tloJROctBGsE8rX/rURrhx5qVWJg1jnn8GbOexPHTvaM5vzdWMwFfNKPjBXYj1YwmCt+EFBb2W9pvKzIVsCQ5M+r2hNl2FRInq41v61xpiwMSLxpHdDXz7YYZtSByWH7/0mjwt86EPwkes9Bj5AOO6ZXUjuDiyGAjUQ==",
			"Basic Test"
		});

		// Chinese Traditional
		dataList.add(new String[]
		{
			"道続万汁国圭絶題手事足物目族月会済。",
			"aUl0Oo0TBpfKQXcbLSJHTA6DjgSmTH3Fn/01YcPG0oM1w68+orizcuSxsCpRfAbc7IDxizuMivQpUm5abwbuyQlLNNy3oH7v0jT29+MkqoeMdDfOlGZZyxb8rZZn5j1N8+k5Y8/Kn0CTk/GQrL/5IYcIXKc/W8lOxnxRBuzENxNz1QgdecordhW/1IQrcAnLlt5dFXGdCRXFkWDd1FwKs7e3+154cQgwWdFOw7AzqnjDpTrIYlDsSdHCtqjU72PxHW6jCBS2NG6VNeplH6EKjqmVD+M1op+k3QJeKnP7LsWbk0ngneX0GEXSrYtCkDEfqxCFmtOFdsGRBVRw+cAU6A==",
			"UTF8 (Chinese Traditional) Test"
		});

		// Japanese
		dataList.add(new String[]
		{
			"員ちぞど移点お告周ひょ球独狙チウソノ法保断フヒシハ東5広みぶめい質創ごぴ採8踊表述因仁らトつ。",
			"J1rPZ2i7JdDXzgrQ77ToMtts8So+qw5yevrde8xO8Dib6Irqehg8uz1/0RPkwdniR7HbZQbhFM26Ps038qZ80IoEJvNZEuU3EnSJaIZGBK/0/RjLZFWX0o7HGv4sBh8MQMXtoPIDu8D8GQKlUNnOM0r2sd/T6DbqVv7L53VFpTRBOG9p7tSbkE+TZiMmYFiQFWt0NhAxpTOCsEA75LQvRHws3qlNofZ4expLBdGvhFJpxe01Hx14LMgRPA+m/ohjfFcSyENs6WDTDmOm0oRjIePKVFsKbg9xd9FPUIxs5YBTkGvLopiuE/DgILL1hvBmvSzLCizGbEqyAfg6DgTg/Q==",
			"UTF8 (Japanese) Test"
		});

		// Korean
		dataList.add(new String[]
		{
			"대통령은 즉시 이를 공포하여야 한다, 그 자율적 활동과 발전을 보장한다.",
			"ornQ5HrBiKzcHdtxC4403I5U8Ns3LpIFz3ILkem0KqOHdGlr6/UhdUbtMj+3r9tgSK08bFlIpULLm6MwfxMcopX4JReYRERVvUq2FyPyYXSeUOwph16wHb3pNWiK5rIbS5w1SGonbCSFR8ZhFTeLxDUEJQAO81/UR6Hj53rVRhYvmH6UcuLJbcuItzAx9yZce4BT4/XHEdMgSZdeA8XoRwUpBWRGPNfUD/Dp+pVyIlnHokQ6hU/krYlxesIjRgdmtImhj9eCY/srr+9bKKR20tz/nVRhq2qLEzNqHCBGSArF9LNEFMlcw7BY3CoicYbqirmSRYmAOvneyxyfoQbOEg==",
			"UTF8 (Korean) Test"
		});

		// Greek
		dataList.add(new String[]
		{
			"Λορεμ ιπσθμ δολορ σιτ αμετ, τατιον ινιμιcθσ τε ηασ, ιν εαμ μοδο ποσσιμ ινvιδθντ.",
			"34ZlIvQhUoQuWTaG/ELU4d9T7ILE4xciQ/VuIDtBAIB+o7hCx2QXb+gQpVodKyANIDNPzxsl+2Ow6J92wtGv80o7KZli2YFuNRazgY5+2KwGai6gMQZkwGPciM3HxCXZ0rMnvUlOrSEFubUSy2Ozzl/x4csyuqUpyMKS1hIoNcxE95nHpPhPQ4vzbm6ye31y+g/Kl2i2TcQzsPXnZgMhSh8lsewneEwYw0jAyiQRSqPzsV8R6DqqJQ4UuIVmKOEOwStL7ExJHQGtDzWRF+pdCF8NyOdEjq04jEtm/1kxvVq3CI8HWyxkIicLxcdJmbf50J7bWL6Yhln1HXk5vBD0hw==",
			"UTF8 (Greek) Test"
		});

		dataList.add(new String[]
		{
			"GET&https://loadtest-pvt.api.lab/api/v1/rest/level2/in-in/&apex_l2_ig_app_id=loadtest-pvt-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l2_ig_nonce=7798278298637796436&apex_l2_ig_signature_method=SHA256withRSA&apex_l2_ig_timestamp=1502163142423&apex_l2_ig_version=1.0",
			"H9GB2yv1+jIbeJQ2lpX54CVNltS0ye03v2mQc0bUayRXkZj+hlK4dPjlzcfzpzoeMVxVOt7k6sHUzzdl+svWrMJ0kQW7MY4steDCRadJVAnQXzdnRFtbLmIrRUnbfI+4Gxy2zBHnCNEFCBvHRElHoe95KzjIP3TtlXt3pntUAYKsJf7IKyWz558Oach8tBIcqG9Ykl7PcBnmoppSP0ofOr47TWGArpQ22XPozRueiwmTwc7WYUc5Bdvhh1ut25ox7V6G4T0gwsO//9pwSgA1aSoJ2TRwsKuE3MgHGnP6pMvLAA7wZ0L3k/2xOHQhSRzo+goLgRTjrbVzECDg4g06pQ==",
			"L2 BaseString Happy Path Test"
		});

		dataList.add(new String[]
		{
			"GET&https://loadtest-pvt.api.lab/api/v1/rest/level2/in-in/&ap=裕廊坊 心邻坊&apex_l2_ig_app_id=loadtest-pvt-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l2_ig_nonce=7231415196459608363&apex_l2_ig_signature_method=SHA256withRSA&apex_l2_ig_timestamp=1502164219425&apex_l2_ig_version=1.0&oq=c# nunit mac&q=c# nunit mac",
			"LrgtqO/GRgN26wU65ugIU9IzMSyXDKMXDGFaZck135HIuOw39Ed9Tn69FrudXL+QV4S59oiYuKdqeKOK7knbk0NW3a/R9LV5tG/gYkOaNWhWz28OUniYHsnT4J4k7qH5zsQJrT42RvxBOljWQqlzjARFQcIWvdjKl7VwAslCR80KmBlsMKbuec+5QhxLRxb5Hkyr/Fp13sQJx4SxRsFqOzwCIkQvQal0mmM8E6uwrz9V8M4ozVVIa/KTqE2PzeIp45p4sqGWiuKTyNuU0fjiTN3GU43/Z03Gcth3NeJORa8kFMru9aZ3LSMNOAruuZdtK8zfnXe/To2fRhqQkNXA1g==",
			"L2 BaseString with UTF8 Parameters Test"
		});

		for (String[] item : dataList)
		{
			L2Test(item[0], item[1], item[2]);
		}
	}

	public static void L2Test(String baseString, String expectedSignature, String message)
	{
		String signature = null;
		try {
			signature = ApiAuthorization.getL2Signature(baseString, privateKey);
		} catch (ApiUtilException e) {
			fail("Should not throw any exception during test execution");
		}

        assertEquals(message, expectedSignature, signature);
	}
}
