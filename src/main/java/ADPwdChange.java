import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.InitialLdapContext;

public class ADPwdChange {
	DirContext ldapContext;
	String baseName = ",OU=Service Account,OU=Servers,DC=comdev,DC=com,DC=in";
	String serverIP = "localhost";

	public ADPwdChange() {

		try {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			//env.put(Context.SECURITY_AUTHENTICATION, "Simple");
			// NOTE: Replace the user and password in next two lines, this user should have
			// privileges to change password.
			// NOTE: This is NOT the user whose password is being changed.
			env.put(Context.SECURITY_PRINCIPAL,
					"CN=user01,OU=Service Account,OU=Servers,DC=comdev,DC=com,DC=in");
			env.put(Context.SECURITY_CREDENTIALS, "password");
			env.put(Context.PROVIDER_URL, "ldap://10.11.12.13:389/");
			// env.put(Context.SECURITY_PROTOCOL, "ssl");
			ldapContext = new InitialLdapContext(env, null);

		} catch (Exception e) {
			System.out.println(" bind error: " + e);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void updatePassword(String username, String password) {
		try {

			System.out.println("1");
			String quotedPassword = "\"" + password + "\"";
			char unicodePwd[] = quotedPassword.toCharArray();
			byte pwdArray[] = new byte[unicodePwd.length * 2];
			for (int i = 0; i < unicodePwd.length; i++) {
				pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
				pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
			}

			ModificationItem[] mods = new ModificationItem[1];
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("UnicodePwd", pwdArray));

			ldapContext.modifyAttributes("CN=" + username + baseName, mods);

		} catch (Exception e) {
			System.out.println("update password error: " + e);
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		System.setProperty("javax.net.debug", "all");
		ADPwdChange adc = new ADPwdChange();
		// NOTE: Replace below with username whose password has to be changed and the
		// desired password.
		adc.updatePassword("username", "password");
	}
}
