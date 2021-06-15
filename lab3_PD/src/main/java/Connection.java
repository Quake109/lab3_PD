import com.azure.cosmos.implementation.apachecommons.lang.StringUtils;

public class Connection {

    public static String MASTER_KEY =
            System.getProperty("ACCOUNT_KEY",
                    StringUtils.defaultString(StringUtils.trimToNull(
                            System.getenv().get("ACCOUNT_KEY")),
                            "47VJcFChrbLXPgluC4pm9PCUBmhAsopxshyGPZ9XVr6c0E5NY83F39vuPHKlkoS20eF3NWEyEivj4m51ZYqvhQ=="));

    public static String HOST =
            System.getProperty("ACCOUNT_HOST",
                    StringUtils.defaultString(StringUtils.trimToNull(
                            System.getenv().get("ACCOUNT_HOST")),
                            "https://e05e00bf-0ee0-4-231-b9ee.documents.azure.com:443/"));
}
