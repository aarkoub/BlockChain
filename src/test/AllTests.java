package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        TestCryptoHex.class,
        TestHMAC.class,
        TestMerkle.class,
        TestSHA256.class,
        TestSECP256R1.class
        })
public class AllTests {
}
