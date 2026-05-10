package projectofmine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class APIGateway {

    private String user = "postgres";
    private String pass = "123456";
    private String url = "jdbc:postgresql://localhost:5432/moviedb";

    private Connection con = DriverManager.getConnection(url,user,pass);
    private MovieService movieService = new MovieService(con);

    public APIGateway() throws SQLException {

    }

    public void APIRequestMovie(String query) throws SQLException {
        movieService.movieRequest(query);
    }




}
