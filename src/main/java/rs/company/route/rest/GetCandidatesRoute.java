package rs.company.route.rest;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;
import rs.company.candidate.validator.CandidateValidationException;
import rs.company.candidate.validator.CandidateValidator;

import java.util.Map;

public class GetCandidatesRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(CandidateValidationException.class)
                .handled(true)
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody(simple("${exception.message}"));

        onException(PSQLException.class)
                .handled(true)
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Database Error");

        from("direct:getCandidates")
            .bean(CandidateValidator.class, "validateLimitAndOffsetQueryParams")
            .process(exchange -> {
                    Message msg = exchange.getMessage();
                    msg.setHeaders(Map.of("limit", Integer.parseInt((String) msg.getHeader("limit")),
                            "offset", Integer.parseInt((String) msg.getHeader("offset"))));
                })
            .setBody().constant("select * from candidates "
                        + "order by last_updated desc, last_name, first_name "
                        + "limit :?limit offset :?offset")
            .to("jdbc:db?useHeadersAsParameters=true&outputClass=rs.company.candidate.Candidate");
    }
}
