package projectofmine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MovieService {

    Connection con;
    RecommendationService recommendationService;
    public MovieService(Connection con) {
        this.con = con;
        recommendationService = new RecommendationService(con);
    }

    public Movie fetchMovie(String query){
        Movie movie = null;
        String query1 = "select * from movies where name like ?";
        try(PreparedStatement ps = con.prepareStatement(query1);) {
            ps.setString(1, "%"+query+"%");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String title = rs.getString("name");
                String genre = rs.getString("genre");
                boolean default_rec = rs.getBoolean("default_rec");
                movie = new Movie(title, genre, default_rec);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }

    public List<Movie> movieRecommendationRequest(Movie movie, boolean chaos) throws SQLException, TimeoutException {
        List<Movie> movies = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<List<Movie>> task = () ->
                recommendationService.fetchRecommendedMovies(movie, chaos);

        Future<List<Movie>> future = executor.submit(task);

        try {
            movies = future.get(1500, TimeUnit.MILLISECONDS);

        } catch (TimeoutException e) {
            throw new TimeoutException("Recommendation Service timed out");


        }catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Recommendation Service failed: 503 error", e);


        } finally {

            future.cancel(true);
            executor.shutdown();

        }
        return movies;

    }

    public List<Movie> fetchDefaultRecommendationRequest() throws SQLException, TimeoutException {
        return recommendationService.fetchDefaultRecommendations();
    }







}
