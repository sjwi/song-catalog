package com.sjwi.catalog.dao;

import java.util.List;
import java.util.Map;

import com.sjwi.catalog.model.Organization;

public interface OrganizationDao {

	public List<Organization> getOrganizations();
	
	public Map<Integer, String> getMeetingServices();

	public Organization getOrganizationById(int id);

	public String getMeetingServiceById(int id);

  public Map<Integer, String> getGroups();

	public void addGroup(String groupName);

  public String getGroupById(int groupId);

}