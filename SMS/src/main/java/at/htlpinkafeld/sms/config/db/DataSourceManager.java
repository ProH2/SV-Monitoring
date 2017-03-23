/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.config.db;

import at.htlpinkafeld.sms.service.PropertyHolder;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 *
 * @author DarkHell2
 */
public class DataSourceManager {
    private static DataSourceManager instance = null;
    private static EmbeddedDatabase db;
    private static PropertyHolder prop = PropertyHolder.getInstance();
    private static MysqlDataSource mysqlds = new MysqlDataSource();
    private static String hsql;
    private static String mysql;

    private DataSourceManager() {}
    
    
    public static DataSourceManager getInstance(){
        hsql = prop.getProperty("HSQLDB");
        mysql = prop.getProperty("MYSQLDB");
        
        if(instance == null){
            instance = new DataSourceManager();
            
            if(hsql.equals("true")){
                EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
                db = builder.setType(EmbeddedDatabaseType.HSQL).addScript("db/sql/drop-tables.sql").addScript("db/sql/create-db.sql").addScript("db/sql/insert-data.sql").build();
            }
            
            if(mysql.equals("true")){
                mysqlds.setURL("jdbc:mysql://localhost:3306/ZES_SVISS?serverTimezone=UTC");
                mysqlds.setUser("root");
                mysqlds.setPassword("Burgenland2016#");
            }
            
        }
        return instance;
    }
   
    public DataSource dataSource(){
        if(hsql.equals("true")){
            return db;
        }
        if(mysql.equals("true")){
            return mysqlds;
        }
        return null;
    }
}
