package rs.company;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.company.datasource.DbDataSource;
import rs.company.route.RESTRoute;
import rs.company.route.rest.CountCandidatesRoute;
import rs.company.route.rest.GetCandidatesRoute;
import rs.company.route.rest.InsertCandidateRoute;


public class RESTApp {

    private static final Logger LOG = LoggerFactory.getLogger(RESTApp.class);

    public static void main(String[] args) {

        Main main = new Main();

        main.bind("db", DbDataSource.get());

        try (var conf = main.configure()) {
            conf.addRoutesBuilder(RESTRoute.class, GetCandidatesRoute.class,
                    CountCandidatesRoute.class, InsertCandidateRoute.class);
            main.run(args);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
