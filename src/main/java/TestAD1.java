
import java.security.cert.X509Certificate;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
 
public class TestAD1 {
	static DirContext ldapContext;
    public static void main(String[] args) throws Exception {
    	//Bypass security certificate validation and connect to normal port 389
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
		/*
		 * HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		 * 
		 * URL url = new URL("https://www.nakov.com:2083/"); URLConnection con =
		 * url.openConnection(); Reader reader = new
		 * InputStreamReader(con.getInputStream()); while (true) { int ch =
		 * reader.read(); if (ch==-1) { break; } System.out.print((char)ch); }
		 */
        
        System.out.println("testing Active Directory");
        
        Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
        //ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL,  "ldap://10.11.12.13:389/");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, "CN=user01,DC=comnet,DC=com,DC=in");
        ldapEnv.put(Context.SECURITY_CREDENTIALS, "pwd");
        
        ldapContext = new InitialDirContext(ldapEnv);
        System.out.println("ldap connected");
    }
}