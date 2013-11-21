/*************************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.central.community;

import java.net.URI;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.central.JBossCentralActivator;
import org.jboss.tools.central.Messages;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * @author mistria
 */
public class JBossCommunityPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public static final String JBOSS_ORG_USERID = JBossCommunityPreferencePage.class.getPackage().getName() + ".userId"; //$NON-NLS-1$
	public static final String JBOSS_ORG_PASSWORD = JBossCommunityPreferencePage.class.getPackage().getName() + ".password"; //$NON-NLS-1$
	
	public static final String JBOSS_ORG_JIRA_URL = "https://issues.jboss.org"; //$NON-NLS-1$
	
	private Text usernameText;
	private Text passwordText;
	private IEclipsePreferences preferences;

	@Override
	public void init(IWorkbench workbench) {
		this.preferences = JBossCentralActivator.getDefault().getPreferences();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite content = new Composite(parent,SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		final Image jbossCommunityLogo = JBossCentralActivator.getDefault().getImage("wizban/jboss_community.png"); //$NON-NLS-1$
		Canvas canvas = new Canvas(content,SWT.NO_REDRAW_RESIZE);
		GridData imageGridData = new GridData(SWT.NONE, SWT.NONE, false, false, 2, 1);
		canvas.setLayoutData(imageGridData);
		imageGridData.widthHint = 350;
		imageGridData.heightHint = 119;
	    canvas.addPaintListener(new PaintListener() {
	    	@Override
	    	public void paintControl(PaintEvent e) {
	    		e.gc.drawImage(jbossCommunityLogo, 0, 0);
	    	}
	    });
	    canvas.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				jbossCommunityLogo.dispose();
			}
		});
		
		
		Link jbossOrgDescription = new Link(content, SWT.WRAP);
		GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1);
		gridData.widthHint = 500;
		jbossOrgDescription.setLayoutData(gridData);
		jbossOrgDescription.setText(Messages.CommunityPreferences_jbossOrgDescription);
		jbossOrgDescription.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					PlatformUI.getWorkbench().getBrowserSupport().createBrowser(getClass().getName()).openURL(new URL(e.text));
				} catch (Exception ex) {
					ErrorDialog.openError(getShell(), ex.getMessage(), ex.getMessage(),
							new Status(IStatus.ERROR, JBossCentralActivator.PLUGIN_ID, ex.getMessage(), ex));
				}
			}
		});
		
		Label usernameLabel = new Label(content, SWT.NONE);
		usernameLabel.setText(Messages.CommunityPreferences_jbossOrgUsername);
		this.usernameText = new Text(content, SWT.BORDER);
		this.usernameText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		this.preferences.get(JBOSS_ORG_USERID, this.usernameText.getText());
		
		Label passwordLabel = new Label(content, SWT.NONE);
		passwordLabel.setText(Messages.CommunityPreferences_jbossOrgPassword);
		this.passwordText = new Text(content, SWT.BORDER);
		this.passwordText.setEchoChar('*');
		this.passwordText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		
		Button testButton = new Button(content, SWT.PUSH);
		testButton.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 2, 1));
		testButton.setText(Messages.CommunityPreferences_testCredentials);
		testButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
			        JiraRestClientFactory factory = new JerseyJiraRestClientFactory();
			        URI uri = new URI(JBOSS_ORG_JIRA_URL);
			        JiraRestClient client = factory.createWithBasicHttpAuthentication(uri, usernameText.getText(), passwordText.getText());
			        User promise = client.getUserClient().getUser(usernameText.getText(), new NullProgressMonitor());
			        MessageDialog.open(MessageDialog.INFORMATION, getShell(), Messages.CommunityPreferences_CredentialsValid, Messages.CommunityPreferences_CredentialsValid, MessageDialog.INFORMATION);
				} catch (UniformInterfaceException ex) {
					/*if (ex.getResponse().getStatus() == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
						ErrorDialog.openError(getShell(), Messages.CommunityPreferences_wrongCredentials, Messages.CommunityPreferences_couldNotAccessJira,
								new Status(IStatus.ERROR, JBossCentralActivator.PLUGIN_ID, Messages.CommunityPreferences_couldNotAccessJira, ex));
					}else if (ex.getResponse().getStatus() == javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode()) {
						 suggest fix internet connection or configure proxy
					}  else {*/
						ErrorDialog.openError(getShell(), Messages.CommunityPreferences_couldNotAccessJira, Messages.CommunityPreferences_couldNotAccessJira,
								new Status(IStatus.ERROR, JBossCentralActivator.PLUGIN_ID, Messages.CommunityPreferences_couldNotAccessJira, ex));
				//	}
				} catch (Exception ex) {
					ErrorDialog.openError(getShell(), Messages.CommunityPreferences_couldNotAccessJira, Messages.CommunityPreferences_couldNotAccessJira,
							new Status(IStatus.ERROR, JBossCentralActivator.PLUGIN_ID, Messages.CommunityPreferences_couldNotAccessJira, ex)); 
				}
			}
		});
		
		return content;
	}
	
	@Override
	protected void performApply() {
		IEclipsePreferences preferences = JBossCentralActivator.getDefault().getPreferences();
		preferences.put(JBOSS_ORG_USERID, this.usernameText.getText());
		JBossCentralActivator.getDefault().savePreferences();
		ISecurePreferences root = SecurePreferencesFactory.getDefault();
		ISecurePreferences node = root.node(JBOSS_ORG_PASSWORD);
		try {
			node.put(JBOSS_ORG_PASSWORD, this.passwordText.getText(), true);
		} catch (Exception ex) {
			ErrorDialog.openError(getShell(), Messages.CommunityPreferences_errorStoringCredentials, Messages.CommunityPreferences_errorStoringCredentials,
					new Status(IStatus.ERROR, JBossCentralActivator.PLUGIN_ID, Messages.CommunityPreferences_errorStoringCredentials, ex));
		}

	}
	
}
