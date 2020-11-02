package com.sjwi.cfsongs.dao;

import java.util.List;
import java.util.Map;

import com.sjwi.cfsongs.model.Organization;

public interface OrganizationDao {

	public List<Organization> getOrganizations();
	
	public Map<Integer, String> getMeetingServices();

	public String getOrganizationById(int id);

	public String getMeetingServiceById(int id);

}