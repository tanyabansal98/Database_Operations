package mvccrud;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class util {
    private static final String TNS_ALIAS = "damg6210db_high";
    private static final String DB_USER = "MVC_APP";
    private static final String DB_PASS = "MVC_Password@123";

    public static Connection getConnection() throws Exception {

        //Gets your Project Directory and stores it in projectDir
        String projectDir = System.getProperty("user.dir");

        //Builds a wallet path -> ProjectDirectory + / + Wallet
        String walletPath = projectDir + File.separator + "wallet";

        //Tell oracle where the wallet is (It tells Oracle JDBC driver that your tnsnames.ora and sqlnet.ora are located in this folder.)
        System.setProperty("oracle.net.tns_admin", walletPath);

        //Build JDBC URL -> Where TNS_ALIAS matches the name inside tnsnames.ora
        String jdbcUrl = "jdbc:oracle:thin:@" + TNS_ALIAS;

        Properties props = new Properties();
        props.put("user", DB_USER);
        props.put("password", DB_PASS);

        //Opens Database connection
        return DriverManager.getConnection(jdbcUrl, props);

    }

}
