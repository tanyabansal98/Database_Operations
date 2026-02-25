package mvccrud.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class OracleWalletConfig {

    @PostConstruct
    public void setWalletPath() {
        // Tell Oracle JDBC where tnsnames.ora and sqlnet.ora are located
        String walletPath = System.getProperty("user.dir") + File.separator + "wallet";
        System.setProperty("oracle.net.tns_admin", walletPath);
    }
}
