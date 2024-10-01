package rs.company.candidate;

import org.apache.camel.dataformat.bindy.Format;
import org.apache.camel.dataformat.bindy.annotation.BindyConverter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.sql.Date;

@CsvRecord(separator = ",", generateHeaderColumns = true)
public class Candidate {

    public static final class DateConverter implements Format<Date> {
        @Override
        public String format(Date date) throws Exception {
            return date.toString();
        }

        @Override
        public Date parse(String date) throws Exception {
            return Date.valueOf(date);
        }
    }

    @DataField(pos = 1)
    private String jmbg;

    @DataField(pos = 2, columnName = "first_name")
    private String firstName;

    @DataField(pos = 3, columnName = "last_name")
    private String lastName;

    @DataField(pos = 4, columnName = "birth_year")
    private Integer birthYear;

    @DataField(pos = 5)
    private String email;

    @DataField(pos = 6)
    private String phone;

    @DataField(pos = 7, columnName = "is_employeed")
    private Boolean employed;

    @DataField(pos = 8, columnName = "last_updated")
    @BindyConverter(DateConverter.class)
    private Date lastUpdated;

    public Candidate() {
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isEmployed() {
        return employed;
    }

    public void setIsEmployed(Boolean employed) {
        this.employed = employed;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Candidate{" + "jmbg='" + jmbg + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", birthYear=" + birthYear + ", email='" + email + '\'' + ", phone='" + phone + '\'' + ", employed=" + employed + ", lastUpdated=" + lastUpdated + '}';
    }
}
