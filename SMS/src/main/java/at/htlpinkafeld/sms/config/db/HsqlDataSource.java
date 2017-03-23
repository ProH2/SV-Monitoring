package at.htlpinkafeld.sms.config.db;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/*@Profile("hsql")
@Configuration*/
public class HsqlDataSource {

    private static HsqlDataSource instance = null;
    private static EmbeddedDatabase db;
    //jdbc:hsqldb:mem:testdb

    private HsqlDataSource() {}
    
    
    public static HsqlDataSource getInstance(){
        
        if(instance == null){
            instance = new HsqlDataSource();
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            db = builder.setType(EmbeddedDatabaseType.HSQL).addScript("db/sql/drop-tables.sql").addScript("db/sql/create-db.sql").addScript("db/sql/insert-data.sql").build();
        }
        return instance;
    }
    //@Bean
    public DataSource dataSource(){
        return db;
    }

}
