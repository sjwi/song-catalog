package com.sjwi.catalog.dao.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sjwi.catalog.dao.OrganizationDao;
import com.sjwi.catalog.model.Organization;

@Repository
public class SqlOrganizationDao implements OrganizationDao {

	@Autowired
	private Map<String,String> queryStore;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Organization> getOrganizations(){
		return jdbcTemplate.query(queryStore.get("getAllOrganizations"), r -> {
			List<Organization> organizations = new ArrayList<Organization>();
			while (r.next()) {
				organizations.add(new Organization(r.getInt("ID"),r.getString("NAME"),r.getString("TITLE")));
			}
			return organizations;
		});
	}
	
	@Override
	public Map<Integer, String> getMeetingServices(){
		return jdbcTemplate.query(queryStore.get("getAllServices"), r -> {
			Map<Integer, String> meetingServices = new HashMap<Integer, String>();
			while (r.next()) {
				meetingServices.put(r.getInt("ID"),r.getString("NAME"));
			}
			return meetingServices;
		});
	}

	@Override
	public String getOrganizationById(int id) {
		return jdbcTemplate.query(queryStore.get("getOrganizationById"), new Object[] {id}, r -> {
			if (r.next()) {
				return r.getString("NAME");
			} else {
				return null;
			}
		});
	}

	@Override
	public String getMeetingServiceById(int id) {
		return jdbcTemplate.query(queryStore.get("getServiceById"), new Object[] {id}, r -> {
			if (r.next()) {
				return r.getString("NAME");
			} else {
				return null;
			}
		});
	}
}