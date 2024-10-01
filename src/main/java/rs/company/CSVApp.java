package rs.company;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.company.datasource.DbDataSource;
import rs.company.route.csv.CSVRoute;

public class CSVApp {

    public static final Logger LOG = LoggerFactory.getLogger(CSVApp.class);

    public static void main(String[] args) {

        try (CamelContext context = new DefaultCamelContext()) {
            context.getRegistry().bind("db", DbDataSource.get());
            context.addRoutes(new CSVRoute());
            context.start();
            context.createProducerTemplate().sendBody("direct:toCSVFile", null);
            context.stop();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
