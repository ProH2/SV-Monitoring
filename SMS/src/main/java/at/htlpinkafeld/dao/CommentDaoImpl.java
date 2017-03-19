/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.config.db.HsqlDataSource;
import at.htlpinkafeld.sms.pojos.Comment;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author DarkHell2
 */
public class CommentDaoImpl implements CommentDao {
    HsqlDataSource db = new HsqlDataSource();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());

    @Override
    public void insert(Comment comment) {    
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "INSERT INTO comment(commentid, comment, commentto, author, lastchanged) VALUES (:commentid, :comment, :commentto, :author, :lastchanged)";
        
        params.put("commentid", comment.getCommentId());
        params.put("comment", comment.getComment());
        params.put("commentto", comment.getCommentTo());
        params.put("author", comment.getAuthor());
        params.put("lastchanged", comment.getLastChanged());

        template.update(sql, params);
        System.out.println("Inserted Comment");
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DutyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Integer commentid) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM comment WHERE commentid = :commentid";
        
        params.put("commentid", commentid);
        template.update(sql, params);
        
        try {
            db.dataSource().getConnection().commit();
        } catch (SQLException ex) {
            Logger.getLogger(DutyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Comment> getCommentByRange(LocalDateTime time1, LocalDateTime time2) {
        Date startt=new Date(time1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Date endt=new Date(time2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        String sql = "SELECT * FROM comment WHERE lastchanged BETWEEN :time1 AND :time2";
        params.put("time1", startt);
        params.put("time2", endt);
        
        List<Comment> result = template.query(sql, params, new CommentMapper());
        return result;
    }

    @Override
    public Comment findByCommentId(int commentid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("commentid", commentid);

        String sql = "SELECT * FROM comment WHERE commentid=:commentid";

        Comment result = template.queryForObject(sql, params, new CommentMapper());
        
        return result;
    }

    @Override
    public List<Comment> findByAuthor(int userid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("author", userid);

        String sql = "SELECT * FROM comment WHERE author=:author";

        List<Comment> result = template.query(sql, params, new CommentDaoImpl.CommentMapper());
        
        return result;
    }

    @Override
    public List<Comment> findAll() {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * FROM comment";

        List<Comment> result = template.query(sql, params, new CommentDaoImpl.CommentMapper());

        return result;
    }

    @Override
    public void update(Comment o) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql= "UPDATE comment SET comment=:comment, commentto=:commentto, author=:author, lastchanged=:lastchanged WHERE commentid=:commentid";
        
        params.put("commentid", o.getCommentId());
        params.put("comment", o.getComment());
        params.put("commentto", o.getCommentTo());
        params.put("author", o.getAuthor());
        params.put("lastchanged", o.getLastChanged());

        template.update(sql, params);
    }
    
    private static final class CommentMapper implements RowMapper<Comment> {

        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment comment = new Comment();
            comment.setCommentId(rs.getInt("commentid"));
            comment.setComment(rs.getString("comment"));
            comment.setCommentTo(rs.getString("commentto"));
            comment.setAuthor(rs.getInt("author"));
            comment.setLastChanged(rs.getDate("lastChanged"));

            return comment;
        }
    }
    
}
