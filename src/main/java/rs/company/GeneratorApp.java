package rs.company;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rs.company.candidate.generator.CandidateGeneratorOptions;
import rs.company.datasource.DbDataSource;
import rs.company.route.GenerateCandidatesRoute;

public class GeneratorApp {

    private static final boolean DELETE = true;
    private static final int COUNT = 1000;

    private static final Logger LOG = LoggerFactory.getLogger(GeneratorApp.class);

    public static void main(String[] args) {
        try (CamelContext context = new DefaultCamelContext()) {
            context.getRegistry().bind("db", DbDataSource.get());
            context.addRoutes(new GenerateCandidatesRoute());
            context.start();
            context.createProducerTemplate().sendBody("direct:generateCandidates",
                    new CandidateGeneratorOptions(DELETE, COUNT));
            context.stop();

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
