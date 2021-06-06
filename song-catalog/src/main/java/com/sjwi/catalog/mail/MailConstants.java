package com.sjwi.catalog.mail;

public class MailConstants {

	public static String DEFAULT_FROM_ADDRESS;
	public static String ADMIN_DISTRIBUTION_LIST;
	public static String LOG_NOTIFICATION_SUBJECT;
	public static String ERROR_SUBJECT;
	public static String INVITATION_SUBJECT;
	public static String PASSWORD_RESET_SUBJECT;
	
	public static void initializeMailConstants(String defaultFromAddress, String adminDistributionList, String properName) {
		DEFAULT_FROM_ADDRESS = defaultFromAddress;
		ADMIN_DISTRIBUTION_LIST = adminDistributionList;
		LOG_NOTIFICATION_SUBJECT = properName + " Log Notification";
		ERROR_SUBJECT = properName + " ERROR";
		INVITATION_SUBJECT = "{{USERNAME}} has invited you to join the " + properName + " Website";
		PASSWORD_RESET_SUBJECT = "Password Reset Requested - " + properName;
	}
}
