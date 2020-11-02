package com.sjwi.cfsongs.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.cfsongs.dao.OrganizationDao;
import com.sjwi.cfsongs.model.Organization;

@Component
public class OrganizationService {
	
	@Autowired
	OrganizationDao organizationDao;

	public String getOrganizationById(int org) {
		return organizationDao.getOrganizationById(org);
	}

	public String getMeetingServiceById(int service) {
		return organizationDao.getMeetingServiceById(service);
	}

	public List<Organization> getOrganizations() {
		return organizationDao.getOrganizations();
	}

	public Map<Integer, String> getMeetingServices() {
		return organizationDao.getMeetingServices();
	}

}
