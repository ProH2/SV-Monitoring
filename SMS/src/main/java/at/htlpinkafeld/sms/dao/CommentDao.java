/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.dao;

import at.htlpinkafeld.sms.pojo.Comment;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author DarkHell2
 */
public interface CommentDao extends BaseDao<Comment>{
    
    List<Comment> getCommentByRange(LocalDateTime time1, LocalDateTime time2);

    Comment findByCommentId(int commentId);

    List<Comment> findByAuthor(int userId);
    
    List<Comment> findByCommentTo(String commentTo);
}
