package es.antoniomb.utils;

import es.antoniomb.dto.MigrationInput;
import es.antoniomb.dto.enums.MigrationWeb;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by amiranda on 18/9/16.
 */
public abstract class MigrationUtils {

    private static Logger LOGGER = Logger.getLogger(MigrationUtils.class.getName());

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd");

    public static void parseWebCode(MigrationInput migrationInfo) {
        MigrationWeb from = MigrationWeb.parse(migrationInfo.getFrom());
        if (from == null) {
            throw new RuntimeException("Invalid source web code");
        }
        migrationInfo.setSource(from);

        MigrationWeb to = MigrationWeb.parse(migrationInfo.getTo());
        if (to == null) {
            throw new RuntimeException("Invalid target web code");
        }
        migrationInfo.setTarget(to);
    }

    public static void disableSSLCertCheck() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {}
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {}
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.log(Level.WARNING, "SSL error", e);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}
