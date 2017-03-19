/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojos;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author neume / DarkHell2
 */
public class Comment {
    private int commentId;
    private String comment;
    private String commentTo;
    private Date lastChanged;
    private int author;
    

    public Comment() {

    }

    public Comment(int commentId, String comment, String commentTo, int author, LocalDateTime lDt) {
        Date lastC = new Date(lDt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        
        this.commentId = commentId;
        this.comment = comment;
        this.commentTo = commentTo;
        this.author = author;
        this.lastChanged = lastC;
    }
    
    

    public Comment(int commentId, String comment, String commentTo, int author, Date lastChanged) {
        this.commentId = commentId;
        this.comment = comment;
        this.commentTo = commentTo;
        this.author = author;
        this.lastChanged = lastChanged;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentTo() {
        return commentTo;
    }

    public void setCommentTo(String commentTo) {
        this.commentTo = commentTo;
    }

    public Date getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this.lastChanged = lastChanged;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    

    @Override
    public String toString() {
        return "Comment{" + "commentId=" + commentId + ", comment=" + comment + ", commentTo=" + commentTo + ", lastChanged=" + lastChanged + ", author=" + author + '}';
    }
}
