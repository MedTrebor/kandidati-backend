package rs.company.candidate.validator;

import org.apache.camel.Message;

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
}
