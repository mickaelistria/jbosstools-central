/*************************************************************************************
 * Copyright (c) 2008-2013 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.central;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.central.messages"; //$NON-NLS-1$
	public static String DiscoveryViewer_Certification_Label0;
	
	public static String ReportBugDialog_issueTitle;
	public static String ReportBugDialog_stepToReproduce;
	public static String ReportBugDialog_stepsToReproduceDefault;
	public static String ReportBugDialog_expectedUnexpectedLabel;
	public static String ReportBugDialog_expectedUnexpectedDefault;
	public static String ReportBugDialog_attachLogs;
	public static String ReportBugDialog_viewLogFile;
	public static String ReportBugDialog_jbossAccount;
	public static String ReportBugDialog_configureJBossAccount;
	public static String ReportBugDialog_ReportProblemTitle;
	public static String ReportBugDialog_ReportProblemDescription;
	public static String ReportBugDialog_SubmitReport;
	public static String ReportBugDialog_couldNotOpenLog;
	
	public static String CommunityPreferences_jbossOrgDescription;
	public static String CommunityPreferences_jbossOrgUsername;
	public static String CommunityPreferences_jbossOrgPassword;
	public static String CommunityPreferences_errorStoringCredentials;
	public static String CommunityPreferences_testCredentials;
	public static String CommunityPreferences_couldNotAccessJira;
	public static String CommunityPreferences_CredentialsValid;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
