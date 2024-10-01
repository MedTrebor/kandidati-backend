package rs.company;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVApp {

    public static final Logger LOG = LoggerFactory.getLogger(CSVApp.class);

    public static void main(String[] args) {

        try (CamelContext context = new DefaultCamelContext()) {
            context.start();
            context.stop();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
