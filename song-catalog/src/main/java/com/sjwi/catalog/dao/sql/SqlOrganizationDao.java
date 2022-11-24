/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao.sql;

import com.sjwi.catalog.dao.OrganizationDao;
import com.sjwi.catalog.model.Organization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlOrganizationDao implements OrganizationDao {

  @Autowired private Map<String, String> queryStore;

  @Autowired JdbcTemplate jdbcTemplate;

  @Override
  public List<Organization> getOrganizations() {
    return jdbcTemplate.query(
        queryStore.get("getAllOrganizations"),
        r -> {
          List<Organization> organizations = new ArrayList<Organization>();
          while (r.next()) {
            organizations.add(
                new Organization(
                    r.getInt("ID"),
                    r.getString("NAME"),
                    r.getString("TITLE"),
                    r.getString("LINK")));
          }
          return organizations;
        });
  }

  @Override
  public Map<Integer, String> getMeetingServices() {
    return jdbcTemplate.query(
        queryStore.get("getAllServices"),
        r -> {
          Map<Integer, String> meetingServices = new HashMap<Integer, String>();
          while (r.next()) {
            meetingServices.put(r.getInt("ID"), r.getString("NAME"));
          }
          return meetingServices;
        });
  }

  @Override
  public Organization getOrganizationById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getOrganizationById"),
        new Object[] {id},
        r -> {
          if (r.next()) {
            return new Organization(
                r.getInt("ID"), r.getString("NAME"), r.getString("TITLE"), r.getString("LINK"));
          } else {
            return null;
          }
        });
  }

  @Override
  public String getMeetingServiceById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getServiceById"),
        new Object[] {id},
        r -> {
          if (r.next()) {
            return r.getString("NAME");
          } else {
            return null;
          }
        });
  }

  @Override
  public Map<Integer, String> getGroups() {
    return jdbcTemplate.query(
        queryStore.get("getAllGroups"),
        r -> {
          Map<Integer, String> groups = new LinkedHashMap<Integer, String>();
          while (r.next()) {
            groups.put(r.getInt("ID"), r.getString("NAME"));
          }
          return groups;
        });
  }

  @Override
  public void addGroup(String groupName) {
    jdbcTemplate.update(queryStore.get("addGroup"), new Object[] {groupName});
  }

  @Override
  public String getGroupById(int groupId) {
    return jdbcTemplate.query(
        queryStore.get("getGroupById"),
        new Object[] {groupId},
        r -> {
          if (r.next()) return r.getString("NAME");
          else return null;
        });
  }
}
