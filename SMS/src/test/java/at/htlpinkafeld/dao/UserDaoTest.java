package at.htlpinkafeld.dao;

import at.htlpinkafeld.sms.pojo.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;



public class UserDaoTest {

    private EmbeddedDatabase db;

    UserDao userDao;
    
    @Before
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
    	db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).addScript("db/sql/create-db.sql").addScript("db/sql/insert-data.sql").build();
    }

    @Test
    public void testFindByname() {
    	NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
    	UserDaoImpl userDao = new UserDaoImpl();
    	userDao.setNamedParameterJdbcTemplate(template);
    	
    	User user = userDao.findByName("jens");
  
    	Assert.assertNotNull(user);
    	Assert.assertEquals(1, user.getUserNr().intValue());
    	Assert.assertEquals("jens", user.getName());
    	Assert.assertEquals("jens@gmail.com", user.getEmail());
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

}