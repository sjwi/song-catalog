package com.sjwi.catalog.mail;

public class MailConstants {

	public static String DEFAULT_FROM_ADDRESS;
	public static String ADMIN_DISTRIBUTION_LIST;
	public static String INVITATION_SUBJECT;
	public static String PASSWORD_RESET_SUBJECT;
	public static String LOG_NOTIFICATION_SUBJECT = "Log Notification";
	public static String ERROR_SUBJECT = "ERROR";
	public static String EDIT_ACTION = "Song edited";
	public static String VERSION_CREATED_ACTION = "New version created";
	
	public static void initializeMailConstants(String defaultFromAddress, String adminDistributionList, String properName) {
		DEFAULT_FROM_ADDRESS = defaultFromAddress;
		ADMIN_DISTRIBUTION_LIST = adminDistributionList;
		INVITATION_SUBJECT = "{{USERNAME}} has invited you to join the " + properName + " website.";
		PASSWORD_RESET_SUBJECT = "Password Reset Requested - " + properName;
	}
}
