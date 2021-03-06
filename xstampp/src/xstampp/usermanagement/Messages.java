/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.usermanagement;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "xstampp.usermanagement.messages"; //$NON-NLS-1$
  public static String AbstractUserShell_Create;
  public static String CollaborationSettings_CreateWorkingCopy;
  public static String CollaborationSettings_PullChanges;
  public static String CollaborationSettings_WorkingCopyToolTip;
  public static String CreateAdminShell_CreateAdmin;
  public static String CreateUserShell_CreateReadOnlyUser;
  public static String CreateUserShell_CreateUser;
  public static String CreateUserShell_UsernameExists;
  public static String LoginShell_InvalidCredentials;
  public static String LoginShell_ReadOnlyLogin;
  public static String MenuShell_Logout;
  public static String MenuShell_openUserSettings;
  public static String MenuShell_UserSettingsToolTip;
  public static String ProjectSettingsShell_Collaboration;
  public static String ProjectSettingsShell_Title;
  public static String ProjectSettingsShell_UserManagementTitle;
  public static String UserContribution_LoggedAsAdministrator;
  public static String UserContribution_LoggedAsUser;
  public static String UserContribution_LoginLabel;
  public static String UserContribution_OpenUserSettingsTip;
  public static String UserContribution_ReadOnlyAccsessToolTip;
  public static String UserManagementPage_0;
  public static String UserManagementPage_3;
  public static String UserManagementPage_5;
  public static String UserManagementPage_6;
  public static String SyncShell_0;
  public static String SyncShell_1;
  public static String SyncShell_2;
  public static String SyncShell_3;

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
