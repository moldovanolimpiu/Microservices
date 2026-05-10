package projectofmine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    Connection con;
    public MovieService(Connection con) {
        this.con = con;
    }

    public void movieRequest(String query){
        Movie movie = fetchMovie(query);
        if(movie != null){
            movie.printMovie();
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
