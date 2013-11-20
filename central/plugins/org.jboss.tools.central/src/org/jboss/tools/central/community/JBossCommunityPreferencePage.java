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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.central.JBossCentralActivator;
import org.jboss.tools.central.Messages;

/**
 * @author mistria
 */
public class JBossCommunityPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public static final String JBOSS_ORG_USERID = JBossCommunityPreferencePage.class.getPackage().getName() + ".userId"; //$NON-NLS-1$
	public static final String JBOSS_ORG_PASSWORD = JBossCommunityPreferencePage.class.getPackage().getName() + ".password"; //$NON-NLS-1$
	private Text usernameText;
	private Text passwordText;
			
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite content = new Composite(parent,SWT.NONE);
		content.setLayout(new GridLayout(2, false));
		Link jbossOrgDescription = new Link(content, SWT.WRAP);
		jbossOrgDescription.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 2, 1));
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
		
		Label passwordLabel = new Label(content, SWT.NONE);
		passwordLabel.setText(Messages.CommunityPreferences_jbossOrgPassword);
		this.passwordText = new Text(content, SWT.BORDER);
		this.passwordText.setEchoChar('*');
		this.passwordText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		
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
