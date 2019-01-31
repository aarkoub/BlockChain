package test;

import java.security.Security;
import java.util.Date;

import main.Person;
import main.Transaction;
import main.TransactionInput;
import utils.StringUtil;

public class TestTransaction {

	public static void main(String[] args){
		/*Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		Personne amel = new Personne();
		Personne kenza = new Personne();		
		String name = "opera";
		String description = "opera_garnier";
		String location = "paris";
		long begin = Date.UTC(2019-1900, 0, 7, 6, 0, 0);
		long end = Date.UTC(2019-1900, 0, 8, 8, 8, 9);
		long end_subscription = Date.UTC(2019-1900, 0, 6, 22, 50, 8);
		int min_capacity = 5;
		int max_capacity = 8;
		
		TransactionCreation t = new TransactionCreation(amel.getPublicKey(),kenza.getPublicKey(), name, description, begin, end, end_subscription,
				location, min_capacity, max_capacity, null);
		System.out.println(StringUtil.getJson(t));*/
	}
}
