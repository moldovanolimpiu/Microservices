package projectofmine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class RecommendationService {
    private Connection con;
    private Random rand = new Random();

    public RecommendationService(Connection con) {
        this.con = con;
    }

    public List<Movie> fetchRecommendedMovies(String queryGenre, boolean chaos) throws SQLException, InterruptedException, ExecutionException {
        List<Movie> movies = new ArrayList<Movie>();
        String sql = "SELECT * FROM movies WHERE genre = ?";

        int chance = rand.nextInt(100);
        System.out.println(chance);

        if(chance < 20 && chaos) {
            throw new ExecutionException(new RuntimeException("503 Not Available"));
        }

        if(chance < 40 && chaos) {
            int delay = 3000 + rand.nextInt(7000);
            Thread.sleep(delay);
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, queryGenre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("name");
                    String genre = rs.getString("genre");
                    boolean def_rec = rs.getBoolean("default_rec");
                    movies.add(new Movie(title, genre, def_rec));
                }
            }

        }
        return movies;
    }

    public List<Movie> fetchDefaultRecommendations(){
        List<Movie> movies = new ArrayList<>();
        String query1 = "select * from movies where default_rec = true";
        try(PreparedStatement ps = con.prepareStatement(query1);) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String title = rs.getString("name");
                String genre = rs.getString("genre");
                boolean default_rec = rs.getBoolean("default_rec");
                movies.add(new Movie(title, genre, default_rec));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }
}
