/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import java.util.List;

/**
 *
 * @author DarkHell2
 */
public interface BaseDao<T>{
    
    public List<T> findAll();
    
    public void insert(T o);
    
    public void delete(Integer id);
    
    public void update(T o);
}
