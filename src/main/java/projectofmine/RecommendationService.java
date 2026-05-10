package projectofmine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecommendationService {
    private Connection con;

    public RecommendationService(Connection con) {
        this.con = con;
    }

    public List<Movie> fetchRecommendedMovies(String queryGenre) throws SQLException {
        List<Movie> movies = new ArrayList<Movie>();
        String sql = "SELECT * FROM movies WHERE genre = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, queryGenre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("name");
                    String genre = rs.getString("genre");
                    boolean def_rec = rs.getBoolean("default_rec");
                    movies.add(new Movie(title, genre, def_rec));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
