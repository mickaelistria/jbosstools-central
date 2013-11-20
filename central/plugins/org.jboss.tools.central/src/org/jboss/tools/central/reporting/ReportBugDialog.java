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
package org.jboss.tools.central.reporting;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.central.JBossCentralActivator;
import org.jboss.tools.central.Messages;

/**
 * @author mistria
 */
public class ReportBugDialog extends TitleAreaDialog {

	private Image wizban;

	public ReportBugDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(parentShell.getStyle() | SWT.RESIZE);
	}
	
	@Override
	public Composite createDialogArea(Composite parent) {
		setTitle(Messages.ReportBugDialog_ReportProblemTitle);
		setMessage(Messages.ReportBugDialog_ReportProblemDescription);
		this.wizban = JBossCentralActivator.getDefault().getImage("wizban/ReportProblemWizBan.png"); //$NON-NLS-1$
		setTitleImage(this.wizban);
		
		Composite content = new Composite(parent, SWT.NONE);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		content.setLayout(new GridLayout(2, false));
		Label titleLabel = new Label(content, SWT.NONE);
		titleLabel.setText(Messages.ReportBugDialog_issueTitle);
		Text titleText = new Text(content, SWT.BORDER);
		titleText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		Label stepsToReproduceLabel = new Label(content, SWT.NONE);
		stepsToReproduceLabel.setText(Messages.ReportBugDialog_stepToReproduce);
		stepsToReproduceLabel.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 2, 1));
		Text stepsToReproduceText = new Text(content, SWT.BORDER | SWT.MULTI);
		GridData stepsToReproduceGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		stepsToReproduceGridData.heightHint = 100;
		stepsToReproduceText.setLayoutData(stepsToReproduceGridData);
		stepsToReproduceText.setText(Messages.ReportBugDialog_stepsToReproduceDefault);
		Label expectedUnexpectedLabel = new Label(content, SWT.NONE);
		expectedUnexpectedLabel.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 2, 1));
		expectedUnexpectedLabel.setText(Messages.ReportBugDialog_expectedUnexpectedLabel);
		Text expectedUnexpectedText = new Text(content, SWT.BORDER | SWT.MULTI);
		GridData expectedUnexpectedGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		expectedUnexpectedGridData.heightHint = 100;
		expectedUnexpectedText.setLayoutData(expectedUnexpectedGridData);
		expectedUnexpectedText.setText(Messages.ReportBugDialog_expectedUnexpectedDefault);
		Button attachLogs = new Button(content, SWT.CHECK);
		attachLogs.setSelection(true);
		attachLogs.setText(Messages.ReportBugDialog_attachLogs);
		Link logLink = new Link(content, SWT.NONE);
		logLink.setText("<a>" + Messages.ReportBugDialog_viewLogFile + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		logLink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(Platform.getLogFileLocation().toFile().toURI());
			    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			    try {
			        IDE.openEditorOnFileStore( page, fileStore );
			    } catch ( PartInitException ex ) {
			        ErrorDialog.openError(getShell(), Messages.ReportBugDialog_couldNotOpenLog, Messages.ReportBugDialog_couldNotOpenLog,
			        		new Status(IStatus.ERROR, JBossCentralActivator.PLUGIN_ID, Messages.ReportBugDialog_couldNotOpenLog, ex));
			    }
			}
		});
		
		Composite spacer = new Composite(content, SWT.NONE);
		GridData spacerLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1);
		spacerLayoutData.heightHint = 30;
		spacer.setLayoutData(spacerLayoutData);
		
		Label jbossAccount = new Label(content, SWT.NONE);
		jbossAccount.setText(Messages.ReportBugDialog_jbossAccount);
		Link configureJbossAccountLink = new Link(content, SWT.NONE);
		configureJbossAccountLink.setText("<a>" + Messages.ReportBugDialog_configureJBossAccount + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		
		return content;
	}
	
	@Override
	public Control createButtonBar(Composite parent) {
		Control res = super.createButtonBar(parent);
		getButton(Window.OK).setText(Messages.ReportBugDialog_SubmitReport);
		return res;
	}
	
	@Override
	public boolean close() {
		this.wizban.dispose();
		return super.close();
	}

}
