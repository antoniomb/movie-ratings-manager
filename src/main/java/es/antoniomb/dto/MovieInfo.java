package es.antoniomb.dto;

/**
 * Created by amiranda on 18/09/16.
 */
public class MovieInfo {

    public String id;
    public String title;
    public String year;
    public String rate;
    public String date;

    public MovieInfo() {
    }

    public MovieInfo(String title, String year) {
        this.title = title;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MovieInfoDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", rate='" + rate + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
