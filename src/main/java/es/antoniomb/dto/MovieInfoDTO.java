package es.antoniomb.dto;

/**
 * Created by amiranda on 18/09/16.
 */
public class MovieInfoDTO {

    public Long id;
    public String value;

    public MovieInfoDTO(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
