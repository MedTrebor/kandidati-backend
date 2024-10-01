package rs.company.route.rest;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;
import rs.company.candidate.validator.CandidateValidationException;
import rs.company.candidate.validator.CandidateValidator;
import rs.company.processor.CandidateUpdateProcessor;

public class UpdateCandidateRoute extends RouteBuilder {

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
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Database Error");

        from("direct:updateCandidate")
                .bean(CandidateValidator.class, "validateUpdate")
                .process(new CandidateUpdateProcessor())
                .to("jdbc:db?useHeadersAsParameters=true&outputClass=rs.company.candidate.Candidate&outputType=SelectOne")
                .choice()
                .when(simple("${body} == null"))
                .setBody().simple("Candidate with jmbg=${header.jmbg} not found")
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .otherwise()
                .removeHeaders("*")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Candidate updated");
    }
}
