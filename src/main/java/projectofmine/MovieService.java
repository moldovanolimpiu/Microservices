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

    public void movieRequest(String query, boolean chaos) throws SQLException {
        Movie movie = fetchMovie(query);
        boolean found = true;
        if(movie != null){
            movie.printMovie();
        }else{
            System.out.println("Movie not found");
            found = false;
        }
        System.out.println("Other recommendations:");
        List<Movie> movies;
        if(!found){
            movies = recommendationService.fetchDefaultRecommendations();
            for(Movie m : movies){
                m.printMovie();
            }
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<List<Movie>> task = () ->
                recommendationService.fetchRecommendedMovies(movie.getGenre(), chaos);

        Future<List<Movie>> future = executor.submit(task);

        try {
            movies = future.get(1500, TimeUnit.MILLISECONDS);

        } catch (TimeoutException e) {

            System.out.println("Recommendation service timeout.");
            movies = recommendationService.fetchDefaultRecommendations();


        } catch (ExecutionException e) {

            System.out.println("Recommendation service failed.");
            movies = recommendationService.fetchDefaultRecommendations();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            movies = recommendationService.fetchDefaultRecommendations();

        } finally {

            future.cancel(true);
            executor.shutdown();

        }

        for(Movie m : movies){
            m.printMovie();
        }
    }

    private Movie fetchMovie(String query){
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



}
