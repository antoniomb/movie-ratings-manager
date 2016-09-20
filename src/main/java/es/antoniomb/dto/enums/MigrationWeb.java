package es.antoniomb.dto.enums;

/**
 * Created by amiranda on 20/9/16.
 */
public enum MigrationWeb {
    FILMAFFINITY("fa"),
    IMDB("imdb"),
    LETSCINE("letscine");
    String webCode;
    private MigrationWeb(String webCode) {
        this.webCode = webCode;
    }
    public String getWebCode() {
        return webCode;
    }
    public static MigrationWeb parse(String webCode){
        for (MigrationWeb migrationWebCode : values()) {
            if (webCode.equals(migrationWebCode.getWebCode())) {
                return migrationWebCode;
            }
        }
        return null;
    }
}
