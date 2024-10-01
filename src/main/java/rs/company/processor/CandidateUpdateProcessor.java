package rs.company.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import rs.company.candidate.Candidate;
import rs.company.candidate.validator.CandidateValidationException;
import rs.company.candidate.validator.CandidateValidator;

public class CandidateUpdateProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getMessage();
        CandidateValidator.validateJmbgQueryParam(msg);
        Candidate candidate = msg.getBody(Candidate.class);

        StringBuilder updateStr = new StringBuilder("update candidates set ");
        boolean isFirst = true;
        if (candidate.getJmbg() != null && candidate.getJmbg() != msg.getHeader("jmbg")) {
            updateStr.append("jmbg = :?newJmbg");
            msg.setHeader("newJmbg", candidate.getJmbg());
            isFirst = false;
        }
        if (candidate.getFirstName() != null && !candidate.getFirstName().isEmpty()) {
            if (!isFirst) updateStr.append(", ");
            updateStr.append("first_name = :?firstName");
            msg.setHeader("firstName", candidate.getFirstName());
            isFirst = false;
        }
        if (candidate.getLastName() != null && !candidate.getLastName().isEmpty()) {
            if (!isFirst) updateStr.append(", ");
            updateStr.append("last_name = :?lastName");
            msg.setHeader("lastName", candidate.getLastName());
            isFirst = false;
        }
        if (candidate.getBirthYear() != null) {
            if (!isFirst) updateStr.append(", ");
            updateStr.append("birth_year = :?birthYear");
            msg.setHeader("birthYear", candidate.getBirthYear());
            isFirst = false;
        }
        if (candidate.getEmail() != null && !candidate.getEmail().isEmpty()) {
            if (!isFirst) updateStr.append(", ");
            updateStr.append("email = :?email");
            msg.setHeader("email", candidate.getEmail());
            isFirst = false;
        }
        if (candidate.getPhone() != null && !candidate.getPhone().isEmpty()) {
            if (!isFirst) updateStr.append(", ");
            updateStr.append("phone = :?phone");
            msg.setHeader("phone", candidate.getPhone());
            isFirst = false;
        }
        if (candidate.isEmployed() != null) {
            if (!isFirst) updateStr.append(", ");
            updateStr.append("is_employed = :?isEmployed");
            msg.setHeader("isEmployed", candidate.isEmployed());
            isFirst = false;
        }
        if (isFirst) {
            throw new CandidateValidationException("No fields to update");
        }

        updateStr.append(" where jmbg = :?jmbg returning *");

        msg.setBody(updateStr.toString());
    }
}
