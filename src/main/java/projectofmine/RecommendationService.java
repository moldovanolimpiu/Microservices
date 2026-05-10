package projectofmine;

import java.sql.Connection;

public class RecommendationService {
    private Connection con;

    public RecommendationService(Connection con) {
        this.con = con;
    }
}
