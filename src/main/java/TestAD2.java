import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

class TestAD2 {
	static DirContext ldapContext;

	public static void main(String[] args) throws NamingException {
		String status = authenticateUser("username", "pwd");
		System.out.println(status);
	}

	// service user
	static String serviceUserDN = "CN=user01,OU=Service Account,OU=Servers,DC=comnet,DC=com,DC=in";
	static String serviceUserPassword = "password";

	// user to authenticate
	String identifyingAttribute = "sAMAccountName";
	String base = "DC=comnet,DC=com,DC=in";

	// LDAP connection info
	static String ldapUrl = "ldaps://10.11.12.14:636/";

	public static String authenticateUser(String userName, String password) {
		String status = "FAILED";
		String identifyingAttribute = "sAMAccountName";
		String base = "DC=comnet,DC=com,DC=in";

		// first create the service context
		DirContext serviceCtx = null;
		try {
			System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");
			Properties serviceEnv = new Properties();
			serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
			serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			serviceEnv.put(Context.SECURITY_PRINCIPAL, serviceUserDN);
			serviceEnv.put(Context.SECURITY_CREDENTIALS, serviceUserPassword);
			serviceCtx = new InitialDirContext(serviceEnv);

			String[] attributeFilter = { identifyingAttribute };
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(attributeFilter);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// use a search filter to find only the user we want to authenticate
			String searchFilter = "(" + identifyingAttribute + "=" + userName + ")";
			NamingEnumeration<SearchResult> results = serviceCtx.search(base, searchFilter, sc);

			if (results.hasMore()) {
				// get the users DN (distinguishedName) from the result
				SearchResult result = results.next();
				String distinguishedName = result.getNameInNamespace();

				// attempt another authentication, now with the user
				Properties authEnv = new Properties();
				authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
				authEnv.put(Context.PROVIDER_URL, ldapUrl);
				authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
				authEnv.put(Context.SECURITY_CREDENTIALS, password);
				new InitialDirContext(authEnv);
				status = "SUCCESS";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serviceCtx != null) {
				try {
					serviceCtx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
		return status;
	}
}
