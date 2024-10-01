package rs.company.candidate.validator;

import org.apache.camel.Message;
import rs.company.candidate.Candidate;

import java.time.LocalDate;

public class CandidateValidator {

    public static void validateLimitAndOffsetQueryParams(Message msg) throws CandidateValidationException {
        if (msg.getHeader("limit") == null || msg.getHeader("offset") == null) {
            throw new CandidateValidationException("'limit' and 'offset' query parameters are required");
        }

        try {
            int limit = Integer.parseUnsignedInt((String) msg.getHeader("limit"));
            if (limit == 0) throw new NumberFormatException();
        } catch (NumberFormatException ignored) {
            throw new CandidateValidationException(
                    "'limit' query parameter must be a positive integer larger than 0");
        }

        try {
            int ignored = Integer.parseUnsignedInt((String) msg.getHeader("offset"));
        } catch (NumberFormatException ignored) {
            throw new CandidateValidationException(
                    "'offset' query parameter must be positive integer");
        }
    }

    public static void validateJmbg(String jmbg) throws CandidateValidationException {
        if (jmbg == null || !jmbg.matches("^\\d{13}$")) {
            throw new CandidateValidationException("Invalid JMBG provided");
        }
    }

    public void validateInsert(Candidate candidate) throws CandidateValidationException {

        if (candidate.getJmbg() == null) {
            throw new CandidateValidationException("'jmbg' field missing");
        }
        validateJmbg(candidate.getJmbg());
        if (candidate.getFirstName() == null || candidate.getFirstName().isEmpty()) {
            throw new CandidateValidationException("'firstName' field missing");
        }
        if (candidate.getLastName() == null || candidate.getLastName().isEmpty()) {
            throw new CandidateValidationException("'lastName' field missing");
        }
        if (candidate.getBirthYear() == null) {
            throw new CandidateValidationException("'birthYear' field is missing");
        }
        if (candidate.getBirthYear() >= LocalDate.now().getYear() || candidate.getBirthYear() < 1900) {
            throw new CandidateValidationException("'birthYear' field is incorrect");
        }
        if (candidate.getEmail() == null || candidate.getEmail().isEmpty()) {
            throw new CandidateValidationException("'email' field is missing");
        }
        if (candidate.getPhone() == null || candidate.getPhone().isEmpty()) {
            throw new CandidateValidationException("'phone' field is missing");
        }
        if (candidate.isEmployed() == null) {
            throw new CandidateValidationException("'employed' field is missing");
        }
        if (candidate.getLastUpdated() != null) {
            throw new CandidateValidationException("'lastUpdated' should not be provided");
        }
    }
}
