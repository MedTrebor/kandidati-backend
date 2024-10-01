package rs.company.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.ArrayList;

public class CandidateDeleteProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getMessage();

        @SuppressWarnings("unchecked")
        ArrayList<String> candidates = msg.getBody(ArrayList.class);

        StringBuilder sql = new StringBuilder("delete from candidates where jmbg in (");
        for (int i = 0; i < candidates.size(); i++) {
            String jmbg = candidates.get(i);
            sql.append('\'').append(jmbg).append('\'');
            if (i < candidates.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(") returning *");

        msg.setBody(sql.toString());
    }
}
