package rs.company.candidate.generator;

import rs.company.candidate.Candidate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.random.RandomGenerator;
import java.util.regex.Pattern;

public class CandidateGenerator {

    private static final RandomGenerator random = RandomGenerator.getDefault();

    private static LocalDateTime generateDob() {
        long epoch = random.nextLong(Static.MAX_DOB);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
    }

    private static String generateEmail(String firstName, String lastName) {
        String email = firstName + "." + lastName + "@" + Static.EMAIL_EXTENSIONS[
                random.nextInt(Static.EMAIL_EXTENSIONS.length)];
        email = email.toLowerCase();

        for (var entry : Static.REPLACEMENTS.entrySet()) {
            email = email.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
        }

        return email;
    }

    private static String generateNumberSequence(int length) {
        StringBuilder sequence = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sequence.append(random.nextInt(10));
        }
        return sequence.toString();
    }

    private static String generatePhone() {
        String first = generateNumberSequence(random.nextInt(3, 5));
        String last = generateNumberSequence(3);
        return Static.PHONE_PROVIDERS[random.nextInt(Static.PHONE_PROVIDERS.length)]
                + "/" + first + "-" + last;
    }

    private static String generateJMBG(LocalDateTime dob) {
        String day = String.valueOf(dob.getDayOfMonth());
        if (day.length() == 1) {
            day = "0" + day;
        }
        String month = String.valueOf(dob.getMonthValue());
        if (month.length() == 1) {
            month = "0" + month;
        }
        String year = String.valueOf(dob.getYear()).substring(1);
        String extension = generateNumberSequence(6);
        return day + month + year + extension;
    }

    public static Candidate candidate() {
        LocalDateTime dob = generateDob();
        String firstName = Static.FIRST_NAMES[random.nextInt(Static.FIRST_NAMES.length)];
        String lastName = Static.LAST_NAMES[random.nextInt(Static.LAST_NAMES.length)];

        Candidate candidate = new Candidate();
        candidate.setJmbg(generateJMBG(dob));
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setBirthYear(dob.getYear());
        candidate.setEmail(generateEmail(firstName, lastName));
        candidate.setPhone(generatePhone());
        candidate.setIsEmployed(random.nextBoolean());

        return candidate;
    }
}
