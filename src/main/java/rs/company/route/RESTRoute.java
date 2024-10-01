package rs.company.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;

public class RESTRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("jetty")
                .host("0.0.0.0").port(8080)
                .enableCORS(true)
                .bindingMode(RestBindingMode.json)
                .clientRequestValidation(true)
                .inlineRoutes(true);

        rest()
                .get()
                .param().name("offset").type(RestParamType.query).defaultValue("0").endParam()
                .to("direct:getCandidates");
    }
}
