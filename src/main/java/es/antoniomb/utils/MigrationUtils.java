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

    public static void validateParams(MigrationInput migrationInfo) {
        if (migrationInfo == null) {
            throw new RuntimeException("Invalid input data");
        }

        if (migrationInfo.getFromUsername() == null || migrationInfo.getFromPassword() == null) {
            throw new RuntimeException("Invalid source user data");
        }

        parseWebCode(migrationInfo);

        if (!(MigrationWeb.CSV.equals(migrationInfo.getTarget()) ||
                MigrationWeb.ANALYSIS.equals(migrationInfo.getTarget())) &&
                (migrationInfo.getToUsername() == null)) {
            throw new RuntimeException("Invalid target user data");
        }
    }

    public static void parseWebCode(MigrationInput migrationInfo) {
        MigrationWeb from = MigrationWeb.parse(migrationInfo.getFrom());
        if (from == null) {
            throw new RuntimeException("Invalid source");
        }
        migrationInfo.setSource(from);

        MigrationWeb to = MigrationWeb.parse(migrationInfo.getTo());
        if (to == null) {
            throw new RuntimeException("Invalid target");
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

    /**
     * Calculates the similarity (a number within 0 and 100) between two strings.
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return ((longerLength - editDistance(longer, shorter)) / (double) longerLength) * 100;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://r...content-available-to-author-only...e.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

}
