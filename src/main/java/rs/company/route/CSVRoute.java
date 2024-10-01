package rs.company.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import rs.company.candidate.Candidate;

public class CSVRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:toCSVFile")
            .setBody(constant("select * from candidates"))
            .to("jdbc:db?outputClass=rs.company.candidate.Candidate")
            .marshal()
            .bindy(BindyType.Csv, Candidate.class)
            .to("file:src/data?fileName=candidates.csv");
    }
}
