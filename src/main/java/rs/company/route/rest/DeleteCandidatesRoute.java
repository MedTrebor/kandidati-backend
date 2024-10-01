package rs.company.route.rest;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;
import rs.company.candidate.validator.CandidateValidationException;
import rs.company.candidate.validator.CandidateValidator;
import rs.company.processor.CandidateDeleteProcessor;

public class DeleteCandidatesRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(CandidateValidationException.class)
                .handled(true)
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().simple("${exception.message}");

        onException(PSQLException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Database error");

        from("direct:deleteCandidates")
                .bean(CandidateValidator.class, "validateDelete")
                .process(new CandidateDeleteProcessor())
                .to("jdbc:db?outputClass=rs.company.candidate.Candidate")
                .choice()
                .when(simple("${body} == null || ${body.isEmpty()}"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody(constant("No candidates to delete were found"))
                .otherwise()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody(constant("Deletion successful"));
    }
}
