package rs.company.route.rest;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.postgresql.util.PSQLException;
import rs.company.candidate.validator.CandidateValidationException;
import rs.company.candidate.validator.CandidateValidator;

public class InsertCandidateRoute extends RouteBuilder {

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
                .to("log:PSQLException?level=ERROR")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant("Database error");

        from("direct:insertCandidate")
            .bean(CandidateValidator.class, "validateInsert")
            .setHeader("jmbg", simple("${body.getJmbg()}"))
            .setHeader("firstName", simple("${body.getFirstName()}"))
            .setHeader("lastName", simple("${body.getLastName()}"))
            .setHeader("birthYear", simple("${body.getBirthYear()}"))
            .setHeader("email", simple("${body.getEmail()}"))
            .setHeader("phone", simple("${body.getPhone()}"))
            .setHeader("isEmployed", simple("${body.isEmployed()}"))
            .setBody(constant("insert into candidates (" +
                        "jmbg, first_name, last_name, birth_year, email, phone, is_employed) values " +
                        "(:?jmbg, :?firstName, :?lastName, :?birthYear, :?email, :?phone, :?isEmployed)"))
            .to("jdbc:db?useHeadersAsParameters=true")
            .removeHeaders("*")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
            .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
            .setBody().constant("Candidate created");
    }
}
