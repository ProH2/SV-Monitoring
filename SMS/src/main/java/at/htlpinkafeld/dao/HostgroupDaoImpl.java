/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.dao;

import at.htlpinkafeld.config.db.HsqlDataSource;
import at.htlpinkafeld.sms.pojos.Hostgroup;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author DarkHell2
 */
public class HostgroupDaoImpl implements HostgroupDao {

    HsqlDataSource db = HsqlDataSource.getInstance();
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db.dataSource());

    @Override
    public void insert(Hostgroup hostgroup) {
        if (hostgroup != null) {
            Map<String, Object> params = new HashMap<String, Object>();

            Integer hostgroupnr = hostgroup.getId();
            String name = hostgroup.getName();
            List<String> hostlist = hostgroup.getHostlist();

            String help = "";

            for (int i = 0; i < hostlist.size(); i++) {
                help = help + hostlist.get(i) + ";";
            }

            String sql = "INSERT INTO hostgroup(hostgroupnr, name, hostlist) VALUES (:hostgroupnr, :name, :hostlist)";
            params.put("hostgroupnr", hostgroupnr);
            params.put("name", name);
            params.put("hostlist", help);

            KeyHolder keyHolder = new GeneratedKeyHolder();

            template.update(sql, new AbstractSqlParameterSource() {
                @Override
                public boolean hasValue(String paramName) {
                    return params.containsKey(paramName);
                }

                @Override
                public Object getValue(String paramName) throws IllegalArgumentException {
                    return params.get(paramName);
                }

            }, keyHolder);
            hostgroup.setId(keyHolder.getKey().intValue());

            System.out.println("Inserted Hostgroup");
        }
    }

    @Override
    public void delete(Integer hostgroupnr) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "DELETE FROM hostgroup WHERE hostgroupnr = :hostgroupnr";

        params.put("hostgroupnr", hostgroupnr);
        template.update(sql, params);
    }

    @Override
    public Hostgroup findByHostgroupNr(int hostgroupnr) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("hostgroupnr", hostgroupnr);

        String sql = "SELECT * FROM hostgroup WHERE hostgroupnr=:hostgroupnr";

        Hostgroup result = template.queryForObject(sql, params, new HostgroupMapper());

        return result;
    }

    @Override
    public List<Hostgroup> findAll() {
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT * FROM hostgroup";

        List<Hostgroup> result = template.query(sql, params, new HostgroupDaoImpl.HostgroupMapper());

        if (result != null || !result.isEmpty()) {
            String[] parts;
            Hostgroup help;
            List<String> shelp;

            for (int i = 0; i < result.size(); i++) {
                help = result.get(i);
                shelp = help.getHostlist();
                shelp = new ArrayList<>();
                parts = help.getHelplist().split(";");

                for (int j = 0; j < parts.length; j++) {
                    shelp.add(parts[j]);
                }
            }
        }

        return result;
    }

    @Override
    public void update(Hostgroup o) {
        if (o != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            String sql = "UPDATE hostgroup SET name=:name, hostlist=:hostlist WHERE hostgroupnr=:hostgroupnr";

            Integer hostgroupnr = o.getId();
            String name = o.getName();
            List<String> hostlist = o.getHostlist();

            String help = "";

            for (int i = 0; i < hostlist.size(); i++) {
                help = help + hostlist.get(i) + ";";
            }

            params.put("hostgroupnr", hostgroupnr);
            params.put("name", name);
            params.put("hostlist", help);

            template.update(sql, params);
        }
    }

    private static final class HostgroupMapper implements RowMapper<Hostgroup> {

        public Hostgroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hostgroup hostgroup = new Hostgroup();
            hostgroup.setId(rs.getInt("hostgroupnr"));
            hostgroup.setName(rs.getString("name"));
            hostgroup.setHelplist(rs.getString("hostlist"));
            hostgroup.setHostlist(new LinkedList<>(Arrays.asList(hostgroup.getHelplist().split(";"))));
            return hostgroup;
        }
    }

}
