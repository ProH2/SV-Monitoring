/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojos;

import java.time.LocalDateTime;

/**
 *
 * @author neume
 */
public class Comment {
    private LocalDateTime entryTime;
    private String author;
    private String comment;

    public Comment(){
  
    }
    
    public Comment(LocalDateTime entryTime, String author, String comment) {
        this.entryTime = entryTime;
        this.author = author;
        this.comment = comment;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
    
}
