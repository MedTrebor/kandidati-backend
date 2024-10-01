package rs.company.datasource;

import org.postgresql.ds.PGSimpleDataSource;

public class DbDataSource {
    public static PGSimpleDataSource get() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"127.0.0.1"});
        dataSource.setPortNumbers(new int[]{5434});
        dataSource.setDatabaseName("kandidati");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }
}
