package com.sjwi.catalog.service;

import java.util.List;
import java.util.Map;

import com.sjwi.catalog.dao.OrganizationDao;
import com.sjwi.catalog.model.Organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationService {
	
	@Autowired
	OrganizationDao organizationDao;

	public Organization getOrganizationById(int org) {
		return org == 0? new Organization(0,"Other","Other",null): organizationDao.getOrganizationById(org);
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
