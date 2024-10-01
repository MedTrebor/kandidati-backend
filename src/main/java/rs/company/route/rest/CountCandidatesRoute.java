package rs.company.route.rest;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;

public class CountCandidatesRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(PSQLException.class)
                .handled(true)
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Database Error");

        from("direct:countCandidates")
            .setBody(constant("select count(*) from candidates"))
            .to("jdbc:db?outputType=SelectOne");
    }
}
