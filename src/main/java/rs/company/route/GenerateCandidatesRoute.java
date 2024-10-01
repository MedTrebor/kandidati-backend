package rs.company.route;

import org.apache.camel.builder.RouteBuilder;
import rs.company.candidate.Candidate;
import rs.company.candidate.generator.CandidateGenerator;
import rs.company.candidate.generator.CandidateGeneratorOptions;

import java.util.HashSet;
import java.util.Set;

public class GenerateCandidatesRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:generateCandidates")
            .process(exchange -> {
                    CandidateGeneratorOptions options = exchange.getMessage()
                            .getBody(CandidateGeneratorOptions.class);

                    if (options.delete()) {
                        try (var template = exchange.getContext().createProducerTemplate()) {
                            template.sendBody("direct:deleteCandidates", null);
                        }
                    }

                    StringBuilder insert = new StringBuilder("insert into candidates (jmbg,")
                            .append("first_name,last_name,birth_year,email,phone,is_employed) values ");

                    Set<String> emails = new HashSet<>();

                    for (int i = 0; i < options.count(); i++) {
                        Candidate candidate = CandidateGenerator.candidate();

                        while (emails.contains(candidate.getEmail())) {
                            candidate = CandidateGenerator.candidate();
                        }

                        emails.add(candidate.getEmail());

                        insert.append("('").append(candidate.getJmbg()).append("','");
                        insert.append(candidate.getFirstName()).append("','");
                        insert.append(candidate.getLastName()).append("',");
                        insert.append(candidate.getBirthYear()).append(",'");
                        insert.append(candidate.getEmail()).append("','");
                        insert.append(candidate.getPhone()).append("',");
                        insert.append(candidate.isEmployed()).append(")");

                        if (i != options.count() - 1) {
                            insert.append(",");
                        }
                    }

                    exchange.getMessage().setBody(insert.toString());
                })
            .to("jdbc:db");

        from("direct:deleteCandidates")
            .setBody().constant("delete from candidates")
            .to("jdbc:db");
    }
}
