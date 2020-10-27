package com.sjwi.cfsongs.mail;

public class MailConstants {

	public static String DEFAULT_FROM_ADDRESS;
	public static String ADMIN_DISTRIBUTION_LIST;
	public static final String LOG_NOTIFICATION_SUBJECT = "CF Songs LOG Notification: ";
	public static final String ERROR_SUBJECT = "CF Songs ERROR: ";
	public static final String INVITATION_SUBJECT = "{{USERNAME}} has invited you to join the CF Worship Song Website";
	public static final String PASSWORD_RESET_SUBJECT = "Password Reset Requested - CF Worship Songs";
	
	public static void initializeMailConstants(String defaultFromAddress, String adminDistributionList) {
		DEFAULT_FROM_ADDRESS = defaultFromAddress;
		ADMIN_DISTRIBUTION_LIST = adminDistributionList;
	}
}
