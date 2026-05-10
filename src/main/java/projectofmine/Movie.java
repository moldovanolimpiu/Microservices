package projectofmine;

public class Movie {
    private String title;
    private String genre;
    private boolean default_rec;

    public Movie(String title, String genre, boolean default_rec) {
        this.title = title;
        this.genre = genre;
        this.default_rec = default_rec;
    }

    public void printMovie(){
        System.out.println("Title: '" + title + "' Genre: " + genre);
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isDefault_rec() {
        return default_rec;
    }
}
