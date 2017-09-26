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
 */
public class L2SignatureTest {

    // file name follow unix convention...
    private static final String privateCertName = getLocalPath("certificates/ssc.alpha.example.com.p12");
    private static final String publicCertName = getLocalPath("certificates/ssc.alpha.example.com.cer");

    private static final String baseString = "message";
    private static final String password = "passwordp12";
    private static final String alias = "alpha";

    static final PrivateKey privateKey = getPrivateKeyLocal(privateCertName, password, alias);
    static final PublicKey publicKey = getPublicKeyLocal(publicCertName);

    private static final String message = "Lorem ipsum dolor sit amet, vel nihil senserit ei. Ne quo erat feugait disputationi.";
    private static final String expectedSignature = "OsOqG/6hJfGmpCDkqBSZ4netNJDex1lzBYTzGjvjShSFEhJEzAD1zNHKg8Zf9Dve7o9lx3+Yrhrn68nMocgUSOvinhUNF3ttLWw36GzXG7BFJRSIbeUfY3C1vAhkjxmE8oiYoIWctT9qBOL/3GY5QD1H3DiWrb3OLUjy52dsAPmK2P5ofdo8Erd5/0mTxgX+OLMADLJUXq/Aajp1ZIF+djQipPHg0Ms1sNkSHCURxyCjRMKOHNe8DH15lKcApBBjd3XPlb+PGlFl/ffc5Q1ALnAOmsqN6hi8mW+R6Eb0QZsvoRMFSA7kQdWvkCrlWtP5ux+A2Ji/b48SWFSJurVz7yRBhJFDYlvTTCGcgLfwn3TJXa/YbCK05qy307i6X9jnfYaqSYhKC61ExTZYE2SyfagAcWVlSlq3bEovZXllKAwq8Yqyez2EqkOoSzJdj5gmJ1Pb4wN/ss7yYybRSvFShQunj/t6TiQDCJuhghXOfV5Scs/wqjDMWViqrA65YOQHROqAku81NiWFmciVHjk6bNAGsp7iE0p5XnA4z9B41ZVPsxsSXUg4tZvpUrZSpNzlGFBi/uEa1UYcrUd8APzBCvUa75RhZsfxRsCOkpyOEmqoFzg4ngCfegJzBpU5La9e0SOlRvW29p9CK7fS/FZC5YJtP1kucaBN5pX/mxaYeUQ=";

    private static PrivateKey getPrivateKeyLocal(String keystoreFileName, String password, String alias) {
        try {
            return ApiAuthorization.getPrivateKeyFromKeyStore(privateCertName, password, alias);
        } catch (ApiUtilException e) {
            fail("Should not throw any exception during test execution");
        }
        return null;
    }

    private static PublicKey getPublicKeyLocal(String publicCertificateFileName) {
        try {
            return ApiAuthorization.getPublicKeyFromX509Certificate(publicCertificateFileName);
        } catch (ApiUtilException e) {
            fail("Should not throw any exception during test execution");
        }
        return null;
    }

    private static String getLocalPath(String relativeFileName) {
        Path currentRelativePath = Paths.get("");
        String s = combine(currentRelativePath.toAbsolutePath().toString(), relativeFileName.replaceAll("/", File.separator));

        //log.error("Current relative path is: " + s);
        return s;
    }

    public static String combine(String... paths) {
        File file = new File(File.separator);

        for (int i = 0; i < paths.length; i++) {
            file = new File(file, paths[i]);
        }

        return file.getPath();
    }


    @Test
    public void L2_BaseString_IsNullOrEmpty_Test() {
        String expectedMessage = "baseString must not be null or empty.";

        try {
            ApiAuthorization.getL2Signature(null, privateKey);

            fail("Expecting ApiUtilException error.");
        } catch (ApiUtilException expected) {
            assertEquals(expectedMessage, expected.getMessage());
        }

        try {
            ApiAuthorization.getL2Signature("", privateKey);

            fail("Expecting ApiUtilException error.");
        } catch (ApiUtilException expected) {
            assertEquals(expectedMessage, expected.getMessage());
        }
    }

    @Test
    public void L2_PrivateKey_IsNull_Test() {
        String expectedMessage = "privateKey must not be null.";

        try {
            ApiAuthorization.getL2Signature(baseString, null);

            fail("Expecting ApiUtilException error.");
        } catch (ApiUtilException expected) {
            assertEquals(expectedMessage, expected.getMessage());
        }
    }

    @Test
    public void L2_Verify_Signature_Test() {
        try {
            assertTrue(ApiAuthorization.verifyL2Signature(message, expectedSignature, publicKey));
        } catch (ApiUtilException e) {
            fail("Should not throw any exception during test execution");
        }
    }


    @Test
    public void L2_Basic_Test() {
        ArrayList<String[]> dataList = new ArrayList<String[]>();

        dataList.add(new String[]
                {
                        "Lorem ipsum dolor sit amet, vel nihil senserit ei. Ne quo erat feugait disputationi.",
                        "OsOqG/6hJfGmpCDkqBSZ4netNJDex1lzBYTzGjvjShSFEhJEzAD1zNHKg8Zf9Dve7o9lx3+Yrhrn68nMocgUSOvinhUNF3ttLWw36GzXG7BFJRSIbeUfY3C1vAhkjxmE8oiYoIWctT9qBOL/3GY5QD1H3DiWrb3OLUjy52dsAPmK2P5ofdo8Erd5/0mTxgX+OLMADLJUXq/Aajp1ZIF+djQipPHg0Ms1sNkSHCURxyCjRMKOHNe8DH15lKcApBBjd3XPlb+PGlFl/ffc5Q1ALnAOmsqN6hi8mW+R6Eb0QZsvoRMFSA7kQdWvkCrlWtP5ux+A2Ji/b48SWFSJurVz7yRBhJFDYlvTTCGcgLfwn3TJXa/YbCK05qy307i6X9jnfYaqSYhKC61ExTZYE2SyfagAcWVlSlq3bEovZXllKAwq8Yqyez2EqkOoSzJdj5gmJ1Pb4wN/ss7yYybRSvFShQunj/t6TiQDCJuhghXOfV5Scs/wqjDMWViqrA65YOQHROqAku81NiWFmciVHjk6bNAGsp7iE0p5XnA4z9B41ZVPsxsSXUg4tZvpUrZSpNzlGFBi/uEa1UYcrUd8APzBCvUa75RhZsfxRsCOkpyOEmqoFzg4ngCfegJzBpU5La9e0SOlRvW29p9CK7fS/FZC5YJtP1kucaBN5pX/mxaYeUQ=",
                        "Basic Test"
                });

        // Chinese Traditional
        dataList.add(new String[]
                {
                        "道続万汁国圭絶題手事足物目族月会済。",
                        "my9Cn9+6sGMkm3C+OmIDiSetZrxKzSYm6HiFKMRAUlesn+swsfIaa+T7IHq0nKEhulsFOrBGRSdTW4ElJpX+aocueMgs2ayOAED8+fvkXwBJumxWSTaSN7mIfsLVaZUyK/PIhEjeq1lFPms0t/3fqObJWKvKAjihp1X2axWl4b0ouu4q0lRkZ20vomKhYSVLVNob/dy4VXgIkelnQrSxBZDPYZq7E72mcXoAaU/5dhqTHffpOlF/DjJsuHxNyAknAe5hyP8Uz1/9Imc1oSm6r45c7lykRlSfIJ3qN0Tak6y1Az9tM1NR0wTZ63299vFp7NshLadtAct7zKMNOGQmOwYdud6kDvfEeCDcIzo3izn+PSBRtTjj/M2eeA2Mz9gE8jbpW+yLQtnQ3vzHBnPCMYL+fgzzNvk4Ano25HWNyEJjPcTtupgySnk2a0je6VQ0EJbNU6yfQvwiBCXJRdghXDLHXG+XJRj5ntFwaEey1sKHflNSOTz2k7G57NvEyB/ZD4pEhL6wVJkkf5YDaNCSJtB/rWzuhe6WoNEYOVh3CuJjGBf17od4hs6nNhVfxaqsFrHSsoBT7GVPZfUDQghA2WI9VF4ywy4BqirRI1uuUUYVnGmQn7I/ICeHHkpIw0EYPay73RJrrfOt8WpLGSLLMcc/lFKRW01wAcBfy+d8a8c=",
                        "UTF8 (Chinese Traditional) Test"
                });

        // Japanese
        dataList.add(new String[]
                {
                        "員ちぞど移点お告周ひょ球独狙チウソノ法保断フヒシハ東5広みぶめい質創ごぴ採8踊表述因仁らトつ。",
                        "daWoaeanEd86Te7dzfULtO42scDKeq0Lv9GHOluloP5yc7RuNPk7M7dpxQqufZxJgK3iYRPHzs0bQfI1X+k78QnhqJK0W+WOmTQVsdRB1lE2DpnD7lhZoGJ32QAPQZlQVn1kNFvxJU6GKwcIoXHRwakM9MaegvyoFiYBfYWWpDxW4ynwsJPg8RZryS4GCLzUfW8cS9ZI4M6l2bJjnB8OChwBgliJAgnaAy14pCixFrps9Iem/j4GWkKTt+YeIkTrDfsz9HeNJypALotS4cL2juKSqElG83H3pEnVufLhXgrWBmlB1VT6+si3tbieG32Pm/XNiQQApJrPnSmNsxKJIL+aswBO6Vo40MI2CBCS/9KCO4au5WGzXC4a/628slIA5cGd/QvRxLEGhMHOxxaVaVaD6iYddgUBRgdQeWT/c4DLUTaLv+03sG/jYaM1b/neXS2Y7aZbv5I5Etl3YPjnabH0AWVwSp6Xrdb77KuA0ZLDAqlwVclTKwhNA4AyZDdshJm6IXHG88zkgygOS/ubRljciUHITrsSyN1nEGWLWuTQbdnM0ksZM+LfZ8mUSwND1okh8CSnsqjvK8CLcw91iM/hXf0iq15BsDHFW3blWqH4idiSzBhnth5h4W8cMqlgP4gCs25ab6srxaMTNgZQ2WviQHRPuYlyzgKZlg5O/CE=",
                        "UTF8 (Japanese) Test"
                });

        // Korean
        dataList.add(new String[]
                {
                        "대통령은 즉시 이를 공포하여야 한다, 그 자율적 활동과 발전을 보장한다.",
                        "Brqx+X+HvQr6z8DxMAzhy/UK6pcJJFjWgw0DepBwOH97NmXVpsgJQ9IINv0pcvJpqS2KpDg3DWAhO5W7i6Drtqmzs6lTwqBHrZqJWcgxL5yURhkkVC05ZHmbLPa0s/AtCG2zZPvEc7f8eyc9MK+7K/WBu7GKXOgsZACCBLIh1+o3fBECBfWOY0LGyOXVVJGPZjwigoXiC2H4zXYbEo4g4TJhH64lvNwy5MHzWdHe2KVLWdkSoK+iQ8XVNpfh3IPz9e3ocMpR2Pz6ouzKaFaHp2YgtYeVXRe0k7KbHlDy63BZ4nUojO4QFvFwsFlRXdNUPcImGJnl0UN3pYQnVxKh8vfwCrZx1/tUPYi6Y1wJu4dfslmnmUUYFpL7HRPVM3Al4pyAG4LMHz/5uu75w1AnkmOSMCKE40QH1U6GHrWMJLa9FT26nXdx2IGC6u1jmzKZSk64FrV0VnueHZk9BYCPDaMbmniAeNxDn/GG98L1osaSV4290VxQrpp52IMcaztm+QOPulfbLF2vRxDyRyTYNFrTPL2CGmANrrAJq/HHbeCQEbypIDU+/08pTRhek2rgKAVFsPwfK3usRV4evM/dKPAto/4pNPatoi7oVPu64zw7jx2CzC3NJ1eouIqA7/vk6EHnsJJu5dxePnou5PJcIUdIKWjp0r/7lmjQY8hqvls=",
                        "UTF8 (Korean) Test"
                });

        // Greek
        dataList.add(new String[]
                {
                        "Λορεμ ιπσθμ δολορ σιτ αμετ, τατιον ινιμιcθσ τε ηασ, ιν εαμ μοδο ποσσιμ ινvιδθντ.",
                        "hVA9C6Kk4QYMCqKewmsMYy4BpbZQtdZ8dcqYQT4lKz0gugM4SD9PStr+PdQ89Bzlgb+7JzyjBZ0SvSuWioeaSk7FXBpGDbetp8UUObmNYG8EEkxXq4ScDM24iNV8yGhepC4p7QYJq1gOLtyn0UX7XZEDsIXGAhNrrxOLaSc9zhCsq2EW99SjF1pySF/zzOx9VDMjWYPSY+zhtSN0Ou9jGfhrCQlOcTVydDpL3iYOPWHX+RvtIGG1/WkMPMr016FtACuk60ZlNufOWwpfP4hIeDQfAwF7+EpMyaM5r+UM+E9ejX180cuBpsXG+LRUNbqPemfqJSJNdzCRSLBJ1Qd9KneeGaALSBZa8iXiu4SDXhbwwY6oiXwsyKAySYmdsA3fnnIU3stuGKK7LNllW/3pYyoaM15z21eLEOUSWpB4O90+fQfhYRWVUlZoZhiKu1qTk5NY+gLA0OXufQQHx8bDL4KPHGoitZghoboXmIVTTY3feusJ+zkPpcqbQWh7ubVvtksTwVAQ7RC0/97Zqy7C0YPjANGpyCzSr7n8LW6LImJtD5YZW7KT/nTw0ZZTrLXZ36Zd59oHwQbakHHN/7KlGHgFOMZYdNEuu7yzTZRb3ZT71LwvVLG4l9uTx1JbplEjY2c5ExnqgVEwbyZxDIaISUoPfH+h+F/hvDy+uK3teB4=",
                        "UTF8 (Greek) Test"
                });

        dataList.add(new String[]
                {
                        "GET&https://loadtest-pvt.api.lab/api/v1/rest/level2/in-in/&apex_l2_ig_app_id=loadtest-pvt-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l2_ig_nonce=7798278298637796436&apex_l2_ig_signature_method=SHA256withRSA&apex_l2_ig_timestamp=1502163142423&apex_l2_ig_version=1.0",
                        "SEPV4fLkd0QAXHdWwLqJ9QKgerDWrwU6DTeqxFz25GKYkcWfnua23NSxn6nvKEZawSS4glF5Ii3MUd1GQTeez4wRJAIRi01+6HZk58jtH4adYlJ7cW+qHOGz9iOFLJG7023j0S5FaIaxjkKjFx9xncP6CPB3Sd+MbbeinBZc7z8Efe8rNAAi+F0iPLECJq6/UiuRrxxb9VbRSNfvWtkD1NIlIXDabPEcE72Z7KEK9v+Olj36VTbELAxzN2zIrqqNM3PbDaOgJ6gkQ4oiIb/Uxv+9Se8FlWz5dnef2PUkoAxNFF5alzSn8cx+egtb1lZs7VHHTUQ7BVfLdI57VoR+rabIcUOz/ar/DjGEMNDUwc/Hg9kEWHjw48Pck53A1UmTBpJTA4QAORxKe0CbVexMGaOwXtAT2X7IPxs4cRCWbE8Tl0BmfTOPrvGP+3QlrmelnXMSkrWVxdo3Auf62CuRb5zO8iajazl5REW+HN+ggDfjy0HmYS1t1NoKqwjb5EiKTsfcOO7WLp7FhzGu8S+LtqbUphZ+dh40gvNhjT94iOZVuzwIZRpsp2IQLLrEUkCzw5Z6BWvB+WQgywKcpx+xiONR/tMmKVC16zP3nthpcQsvpygQnygL2wfUBHU6sTQBUpqwgGnHBDH3ZWWnFQr5Ymyalm0KJIvp45tM04uYvEE=",
                        "L2 BaseString Happy Path Test"
                });

        dataList.add(new String[]
                {
                        "GET&https://loadtest-pvt.api.lab/api/v1/rest/level2/in-in/&ap=裕廊坊 心邻坊&apex_l2_ig_app_id=loadtest-pvt-4Swyn7qwKeO32EXdH1dKTeIQ&apex_l2_ig_nonce=7231415196459608363&apex_l2_ig_signature_method=SHA256withRSA&apex_l2_ig_timestamp=1502164219425&apex_l2_ig_version=1.0&oq=c# nunit mac&q=c# nunit mac",
                        "P4wFYdulIfZXeR9ASiS7s4NKTUTAW0v6IA3nq9Lqo+nJf3I40ZqDJIfq9OhPgT1viSN0muF0g9QdixtUx5xZ5ZynvhSKVnuhUveXFtOo3mfUULC74dpnCI7Sla3dvPz1x+sS7dTmzTy2o0ggc5dU2zIrozYB3M/NHMRrrHcv0kE4L70eWNC1hCBoV7eJd8A1UDjKMCZZEdexMLZRLLYHS13e8IkJmg8HAlYGPCMq5yu94nkWV/m9B9A2N5RGx/rz178z20u1+WSLyNKkfLVE2uOZYhGWGBLDzIMZWB8v18i+G9Mqu6ygMlCah4l2Ez60rdTy7BajQ/v2wS90Kl+79AyN+LgOqm6C5elTZ/SUcGXLNC7UE0SFrB0g9mmllwSZrY5uOh5k1Q2SlfTpfLSQacpGfhT8KXGEtWE15keC2Y77VPaGQMx6db6p0rJWWtx91X8jWgQwkfXrE1WBp4Y0P/cERl9vxOt+gysaZM+E5rkfKGH/mtBNR/cj5gEB/pnUV10dpLcRxq2D3Nlh4b1WgxiuBDmMmdl5U3Cf2OCmzUbHlVezCMhaL89pS7UjZrWtNiUBpKkBXKMeuLeMmpjioY/KeIqejEgI0AjhZ5a6sRera2Sqge9R3A6gaaoyW146Rt/o7XvY4y7+jjQZQTjl3VBcC7tCwCtuAHy2CFaIRAc=",
                        "L2 BaseString with UTF8 Parameters Test"
                });

        for (String[] item : dataList) {
            L2Test(item[0], item[1], item[2]);
        }


    }

    public static void L2Test(String baseString, String expectedSignature, String message) {
        String signature = null;
        try {
            signature = ApiAuthorization.getL2Signature(baseString, privateKey);
        } catch (ApiUtilException e) {
            fail("Should not throw any exception during test execution");
        }

        assertEquals(message, expectedSignature, signature);
    }
}
