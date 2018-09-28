package com.api.util.ApiSecurity;
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import com.api.util.ApiSecurity.ApiUtilException;
import org.junit.Test;

import com.api.util.ApiSecurity.ApiSigning;

/**
 * @author GDS-PDD
 */
public class RSASignatureTest {

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
            return ApiSigning.getPrivateKeyFromKeyStore(privateCertName, password, alias);
        } catch (ApiUtilException e) {
            fail("Should not throw any exception during test execution");
        }
        return null;
    }

    private static PublicKey getPublicKeyLocal(String publicCertificateFileName) {
        try {
            return ApiSigning.getPublicKeyFromX509Certificate(publicCertificateFileName);
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
            ApiSigning.getRSASignature(null, privateKey);

            fail("Expecting ApiUtilException error.");
        } catch (ApiUtilException expected) {
            assertEquals(expectedMessage, expected.getMessage());
        }

        try {
            ApiSigning.getRSASignature("", privateKey);

            fail("Expecting ApiUtilException error.");
        } catch (ApiUtilException expected) {
            assertEquals(expectedMessage, expected.getMessage());
        }
    }

    @Test
    public void L2_PrivateKey_IsNull_Test() {
        String expectedMessage = "privateKey must not be null.";

        try {
            ApiSigning.getRSASignature(baseString, null);

            fail("Expecting ApiUtilException error.");
        } catch (ApiUtilException expected) {
            assertEquals(expectedMessage, expected.getMessage());
        }
    }

    @Test
    public void L2_Verify_Signature_Test() {
        try {
            assertTrue(ApiSigning.verifyRSASignature(message, expectedSignature, publicKey));
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
                        "BcgiwVRV5NPf2D15NMA7PjfheHY+jYeODlODuaAahd5dU/fuGanMcFpFuKJtxuCQLOE3veZMCC7V+hb/LEaBfkvXw+7gl8WtLu+T927Xs+3517AZm9vZ3nU34FIMAQpTJ8QbciFcd5FAybDiMuCfzvVE59yTSL/JmzSH4188/K6Z1uZ29VizrC2BwtVA/SHaWN1SMUGX6u0tQN5nE4dGZ9lRKm1Jd2rsUNDmqsmUZDJTbgoZbTJjNQklRv48GunXYBt/cfi9T5bryIVilqUphTIe6GrjLXZ1NVVCcMCJaCzAesX2dWUwLCEULcM4Vqw+7SWN20k4zcori5+QkwNH/eyViHwKiYY+neIusUU4HcafIXNHlYQjj1OVEXqPn2P7TzH9y+7TXheNrQ03P6NnRBjEW/bAgoCplbhYWnlNtu+BBNLn9+6rN/ePJz265Wetb16ZjG+ZwbV72PUkGxeFoT7cGBNvcC5zK4bFZV4AOr7TqE9Nt/xm9Xi7/gM0oU7zgYm+32LJaAxG2vax9EFdi3yBKrGRBYLaMH/6KEreZV+iZgLsqK/7tWEQom843iTmeRaxA4/Xeg3MLPyyxrWtQBqu2O/lv6pEf+scnc2Mg6gyc5uRm0luxJUBkqI6i/BAHGZRN1cDkMhWywAcWs3yxxV6qptFYxl6ubLCbCXtiw0=",
                        "UTF8 (Chinese Traditional) Test"
                });

        // Japanese
        dataList.add(new String[]
                {
                        "員ちぞど移点お告周ひょ球独狙チウソノ法保断フヒシハ東5広みぶめい質創ごぴ採8踊表述因仁らトつ。",
                        "RtNtUoRXhNFrFPMy5aJjPTB8yI9AyvLqIKmgjmarxZhB/aOLXSJtHHJMgufOLDsUzEyDenlPuRp4ju2Dp870P19H/IxLktTqkU3DZU35tqk21TWNQDmdl/P9YjY3BNJqU4YBV3A83KRDRhJh235Hjy20dbJqZAe/oL/8GboRd0W941Oj2VfC53SmVAYWQV1aJb4qV3cvoQG2OtcBMNA+ayG+0oTB9AtGZ3CqCUPqbfbb36oc81jYQj0nElHRew7QdclfpAUQaDgCF6svduji2rdXrU+fRYaiRPtm4F1zv9JVuIjKOZRqVQeQ3Nb/X8zUMEBNeWToQPmzoHz6hAEfzYUif2IJ1KqYooV29AwOvwu1itAeUwLtqlHK3QGJYaJVrw05EyAg1IsicAQ+szP+6t6Er3GjhRSXwIcpKdxLUHVtwFoK7E1L4FqxCW+Pokm97h0/rqWREt7DJvoIofQ8rtfEfao5CTaJOQyMRUx+Ds1Kytzpzd1T7aWFvdzFxo9YLfsZ/DzIy2F7iMi9c1b8WYfStlBvfUeEEeByZj+7FrvLMo9Ys5K/UweBfTcBHdPfCmW5RTJhmfK0p+EVsntLqkCbWMoQ6JdNZoASSB7E+NPGJuk3kuVo4sPnPy9vQlHsYJWktXjwTmBp4EZzfcia6U5TSWG0Wdn4ohCYQU2Y/sg=",
                        "UTF8 (Japanese) Test"
                });

        // Korean
        dataList.add(new String[]
                {
                        "대통령은 즉시 이를 공포하여야 한다, 그 자율적 활동과 발전을 보장한다.",
                        "GW0UWsS/bdP22Zd8D+WCZtz4LhyHF/8QemS7xTDPzhSlN+yjPtu7O0f/GGl3s+U1Cm3gUjMIRKbSKyi441Z57MD/9Ju8swtAJkHh9K/LPf/fFfm3UMN0EU7jeoEUkFG3AM8rR24ih16HFpK8RcDHDRL5+tAoU6au/JRLAnuRnhcOjunSC91OhTZJqSGYukoarLYVFxnLFyZPviZPe+aaFW4ZUrD+Kc6K2C/htHS1S/7NJedDsD8If31+dh/wdkIbvhQRDgWBJlSAoqOqmeFSRIIXW/VeufOjXZ9fxa/pmsBDN5BB5Fb3MguxebD61c0MN4F+gnRQ/5arKQL5oIn/QAGan6Ll7s7nUGpa88sdVKRqw/TVcqmYeIFgWBUhnk2p54tvWbCXski63z4QRC+4TZ/ITPgn1sDqsD5Qf9/Ly1RPpJPODNgIYb5i6vh94gchqrF1g3EphbJ3riWCqREoBuCD+yqS2DSE7QWg1gjaHtT8kzcxkt3KpJoLPlZPKt92y03/av8a0AXpc2H7pw2mJ4i13xDsiRKavE4R7pwrfUJxSxYD2jBPZgNTo3XxaboHZgFbvyyw3xHreSo9CmM0mL94qha4jv2TqGuURooiBfizxzuHeMub1t8VIAXOiTk/iQtBPvGLtsQzFW3TeAeZtiYSGBeKOmb6O1vtetBurQk=",
                        "UTF8 (Korean) Test"
                });

        // Greek
        dataList.add(new String[]
                {
                        "Λορεμ ιπσθμ δολορ σιτ αμετ, τατιον ινιμιcθσ τε ηασ, ιν εαμ μοδο ποσσιμ ινvιδθντ.",
                        "G6FezmgEqrnZNxqWfIE8Rcb49L3WQRcAQxQ0xX2sibejHHiOXPXU811OIsL7hsYmyLSSoY3IXTtu271MwfR1TTiODBnIqpgZ0jwmyKK7YoHUDqRgKmVscBnwotw2ntDn1eA2BAU2yKi+UOeUbDcY8dCK/qxdoKdvQg99zjmm1P4EG0dFlmh07oa2ByH4pgioaxI0sKQdDL14qbjrKOiFtfgdv5NEd1Q3kP240p9vLOoScPsRvRZlpWGPCUa0R9wQMtXZAKB3TVs+p8hu5ZHmG9JP2Jo5FRt8EkCG6V3Fg8qlbDO5m9B49atynVBsNSQkYKpCylokJI/mcESNciliQmOwkLmqh6YeELX82PSvnErIPRSAzrqkKYed/HI5gL2Z8pCOwohSfuMeoOrba3JeD98kMQHGwhw+pxSP6lnTCxLwLREhqgSrcXfymhc2TCbA/w/1gT3MjTIDjIF1HgtT2bPpjco62iuKPyrjejb4ARGcty5mlUjbPNUCD/DB4qgghnhbtvWJFJxF7Egs/BeDk5swyyvFBrlXPd/yhCpMJRAOZ0bK3Adj1ij0tVH/kHtDzRYZnF0ZQXZBlHyP2DMvlnJQbIDrTBuojRYFb8W7CPWc/P4RQIGwRv6ZvT+LLl+uuNpvNoVFc/EB0gKII819nINmCjcmuYhsboBLkJ9XHyE=",
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
                        "PAtGMMC5vWprJh4T1QkXiZWpqH9wA1hZz6AEjvHfEIalaejYdpDG31vb1boMjnKqoF2moydAyz97pd1s6FMHYZ3cv2YI/K3Wjf2pjcepI2nXwErncSve2W45CtzJ+TQWwqcttcfm/avhFpOYw74v/AHSrWbuoqPpVLAuznLBHwkiKJPBpt/Tdj1S/6Fmqu7OJu81OEQUBdhySVXtZMBtFHEFMviR2eDG7NcOZ2fspQUrCSdtEFKVyjMAcaFY6uxP5knRoq54FEHCmYotQ/J+VIWD3I0FL1ZswVtJ1zAM41rxpvfEvQFe9jucV6KN3kXnWD6hJbu4pXnakvcQKADgcBDvX0A9dzdhB9ibiWpKT8bXQwZDxYc6HqX9p83HikodV7x6p5Cd03Tol/9JaJqRQHe5ahwucCjnP5WqbTb4PrCNHeCGRj207ncpxBuafllsYfSadGFgeafpnc+5svnuZw9v9Y/H4msFbetoXUH9AQtcs+oCal5zG+AmBNZSqRROsdE6VczPPpwwn5lUCvI5XGXcFuo4X/tcQn9i6t314lgy1XYN6PAubbGDI1rnhlohMVy0XBwEi6xNWRT2vVx5ZxJmAfkSRE12n+AtdVrUQObr8cdzF9lei+DTd1fYz7QRiaJjkljEP4/J0GAiWv8z0JyDzbF9tlypJWkdWaO86eY=",
                        "L2 BaseString with UTF8 Parameters Test"
                });

        for (String[] item : dataList) {
            System.out.println("Test Type:" + item[2]);
            L2Test(item[0], item[1], item[2]);
        }


    }

    public static void L2Test(String baseString, String expectedSignature, String message) {
        String signature = null;
        try {
            signature = ApiSigning.getRSASignature(baseString, privateKey);
        } catch (ApiUtilException e) {
            fail("Should not throw any exception during test execution");
        }

        System.out.println("Expected Signature :" + signature);

        assertEquals(message, expectedSignature, signature);
    }
}
