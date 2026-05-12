package projectofmine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class APIGateway {

    private String user = "postgres";
    private String pass = "123456";
    private String url = "jdbc:postgresql://localhost:5432/moviedb";

    private Connection con = DriverManager.getConnection(url,user,pass);
    private MovieService movieService = new MovieService(con);

    public APIGateway() throws SQLException {

    }

    public void APIRequestMovie(String query, boolean chaos) throws SQLException, TimeoutException {
        //movieService.movieRequest(query, chaos);
        Movie movie = movieService.fetchMovie(query);
        boolean found = true;
        List<Movie> recoms;
        if(movie == null) {
           found = false;
        }

        if(!found){
            System.out.println("Movie not found");
            System.out.println("Other recommendations:");
            recoms = movieService.fetchDefaultRecommendationRequest();
            printMovieList(recoms);
            return;
        }

        movie.printMovie();
        try{
            System.out.println("Similar movies:");
            recoms = movieService.movieRecommendationRequest(movie, chaos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException | RuntimeException e) {
            System.out.println(e.getMessage());
            System.out.println("Other recommendations:");
            recoms = movieService.fetchDefaultRecommendationRequest();
        }

        printMovieList(recoms);

    }

    private void printMovieList(List<Movie> movies) {
        for(Movie movie : movies) {
            movie.printMovie();
        }
    }




}
