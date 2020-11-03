package com.sjwi.catalog.mail;

public class MailConstants {

	public static String DEFAULT_FROM_ADDRESS;
	public static String ADMIN_DISTRIBUTION_LIST;
	public static final String LOG_NOTIFICATION_SUBJECT = "Song Catalog LOG Notification: ";
	public static final String ERROR_SUBJECT = "Song Catalog ERROR: ";
	public static final String INVITATION_SUBJECT = "{{USERNAME}} has invited you to join the CF Song Catalog Website";
	public static final String PASSWORD_RESET_SUBJECT = "Password Reset Requested - Song Catalog";
	
	public static void initializeMailConstants(String defaultFromAddress, String adminDistributionList) {
		DEFAULT_FROM_ADDRESS = defaultFromAddress;
		ADMIN_DISTRIBUTION_LIST = adminDistributionList;
	}
}
