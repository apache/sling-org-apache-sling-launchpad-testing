/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.launchpad.webapp.integrationtest.userManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

/**
 * Tests for the PrivilegesInfo Script Helper
 */
public class PrivilegesInfoTest extends AbstractUserManagerTest {
	
	String testUserId = null;
	String testUserId2 = null;
	String testGroupId = null;
	String testFolderUrl = null;
    Set<String> toDelete = new HashSet<String>();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

        // Script for server-side PrivilegeInfo calculations
        String scriptPath = "/apps/sling/servlet/default";
        testClient.mkdirs(WEBDAV_BASE_URL, scriptPath);
        toDelete.add(uploadTestScript(scriptPath,
        				"usermanager/privileges-info.json.esp",
        				"privileges-info.json.esp"));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		Credentials creds = new UsernamePasswordCredentials("admin", "admin");

		if (testFolderUrl != null) {
			//remove the test user if it exists.
			String postUrl = testFolderUrl;
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new NameValuePair(":operation", "delete"));
			assertAuthenticatedPostStatus(creds, postUrl, HttpServletResponse.SC_OK, postParams, null);
		}
		if (testGroupId != null) {
			//remove the test user if it exists.
			String postUrl = HTTP_BASE_URL + "/system/userManager/group/" + testGroupId + ".delete.html";
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			assertAuthenticatedPostStatus(creds, postUrl, HttpServletResponse.SC_OK, postParams, null);
		}
		if (testUserId != null) {
			//remove the test user if it exists.
			String postUrl = HTTP_BASE_URL + "/system/userManager/user/" + testUserId + ".delete.html";
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			assertAuthenticatedPostStatus(creds, postUrl, HttpServletResponse.SC_OK, postParams, null);
		}
		if (testUserId2 != null) {
			//remove the test user if it exists.
			String postUrl = HTTP_BASE_URL + "/system/userManager/user/" + testUserId2 + ".delete.html";
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			assertAuthenticatedPostStatus(creds, postUrl, HttpServletResponse.SC_OK, postParams, null);
		}
		
        for(String script : toDelete) {
            testClient.delete(script);
        }
	}
	
	
	/**
	 * Checks whether the current user has been granted privileges
	 * to add a new user.
	 */
	public void testCanAddUser() throws JSONException, IOException {
		testUserId = createTestUser();

		String getUrl = HTTP_BASE_URL + "/system/userManager/user/" + testUserId + ".privileges-info.json";

		//fetch the JSON for the test page to verify the settings.
		Credentials testUserCreds = new UsernamePasswordCredentials(testUserId, "testPwd");

		String json = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json);
		JSONObject jsonObj = new JSONObject(json);
		
		assertEquals(false, jsonObj.getBoolean("canAddUser"));
		
		//now add the user to the 'User Admin' group.
		addUserToUserAdminGroup(testUserId);
		
		//fetch the JSON again
		String json2 = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json2);
		JSONObject jsonObj2 = new JSONObject(json2);
		
		assertEquals(true, jsonObj2.getBoolean("canAddUser"));
	}

	/**
	 * Checks whether the current user has been granted privileges
	 * to add a new group.
	 */
	public void testCanAddGroup() throws IOException, JSONException {
		testUserId = createTestUser();

		String getUrl = HTTP_BASE_URL + "/system/userManager/user/" + testUserId + ".privileges-info.json";

		//fetch the JSON for the test page to verify the settings.
		Credentials testUserCreds = new UsernamePasswordCredentials(testUserId, "testPwd");

		String json = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json);
		JSONObject jsonObj = new JSONObject(json);
		
		assertEquals(false, jsonObj.getBoolean("canAddGroup"));
		
		//now add the user to the 'Group Admin' group.
		addUserToGroupAdminGroup(testUserId);
		
		//fetch the JSON again
		String json2 = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json2);
		JSONObject jsonObj2 = new JSONObject(json2);
		
		assertEquals(true, jsonObj2.getBoolean("canAddGroup"));
	}
	
	/**
	 * Checks whether the current user has been granted privileges
	 * to update the properties of the specified user.
	 */
	public void testCanUpdateUserProperties() throws IOException, JSONException {
		testUserId = createTestUser();

		//1. verify user can update thier own properties
		String getUrl = HTTP_BASE_URL + "/system/userManager/user/" + testUserId + ".privileges-info.json";

		//fetch the JSON for the test page to verify the settings.
		Credentials testUserCreds = new UsernamePasswordCredentials(testUserId, "testPwd");

		String json = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json);
		JSONObject jsonObj = new JSONObject(json);
		
		//user can update their own properties
		assertEquals(true, jsonObj.getBoolean("canUpdateProperties"));
		
		
		//2. now try another user 
		testUserId2 = createTestUser();

		//fetch the JSON for the test page to verify the settings.
		Credentials testUser2Creds = new UsernamePasswordCredentials(testUserId2, "testPwd");

		String json2 = getAuthenticatedContent(testUser2Creds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json2);
		JSONObject jsonObj2 = new JSONObject(json2);
		
		//user can not update other users properties
		assertEquals(false, jsonObj2.getBoolean("canUpdateProperties"));
		
		
		//3. now add the user to the 'User Admin' group.
		addUserToUserAdminGroup(testUserId2);
		
		//fetch the JSON again
		String json3 = getAuthenticatedContent(testUser2Creds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json3);
		JSONObject jsonObj3 = new JSONObject(json3);
		
		//user in 'User Admin' group can update the properties of other users
		assertEquals(true, jsonObj3.getBoolean("canUpdateProperties"));
	}

	/**
	 * Checks whether the current user has been granted privileges
	 * to update the properties of the specified group.
	 */
	public void testCanUpdateGroupProperties() throws IOException, JSONException {
		testGroupId = createTestGroup();
		testUserId = createTestUser();

		//1. Verify non admin user can not update group properties
		String getUrl = HTTP_BASE_URL + "/system/userManager/group/" + testGroupId + ".privileges-info.json";

		//fetch the JSON for the test page to verify the settings.
		Credentials testUserCreds = new UsernamePasswordCredentials(testUserId, "testPwd");

		String json = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json);
		JSONObject jsonObj = new JSONObject(json);
		
		//normal user can not update group properties
		assertEquals(false, jsonObj.getBoolean("canUpdateProperties"));
		

		//2. now add the user to the 'Group Admin' group.
		addUserToGroupAdminGroup(testUserId);
		
		//fetch the JSON again
		String json2 = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json2);
		JSONObject jsonObj2 = new JSONObject(json2);
		
		//user in 'Group Admin' group can update the properties of groups
		assertEquals(true, jsonObj2.getBoolean("canUpdateProperties"));
	}
	
	/**
	 * Checks whether the current user has been granted privileges
	 * to remove the specified user.
	 */
	public void testCanRemoveUser() throws IOException, JSONException {
		testUserId = createTestUser();

		//1. verify user can not remove themselves
		String getUrl = HTTP_BASE_URL + "/system/userManager/user/" + testUserId + ".privileges-info.json";

		//fetch the JSON for the test page to verify the settings.
		Credentials testUserCreds = new UsernamePasswordCredentials(testUserId, "testPwd");

		String json = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json);
		JSONObject jsonObj = new JSONObject(json);
		
		//user can not remove themselves
		assertEquals(false, jsonObj.getBoolean("canRemove"));
		
		
		//2. now try another user 
		testUserId2 = createTestUser();

		//fetch the JSON for the test page to verify the settings.
		Credentials testUser2Creds = new UsernamePasswordCredentials(testUserId2, "testPwd");

		String json2 = getAuthenticatedContent(testUser2Creds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json2);
		JSONObject jsonObj2 = new JSONObject(json2);
		
		//user can not delete other users
		assertEquals(false, jsonObj2.getBoolean("canRemove"));
		
		
		//3. now add the user to the 'User Admin' group.
		addUserToUserAdminGroup(testUserId2);
		
		//fetch the JSON again
		String json3 = getAuthenticatedContent(testUser2Creds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json3);
		JSONObject jsonObj3 = new JSONObject(json3);
		
		//user in 'User Admin' group can remove other users
		assertEquals(true, jsonObj3.getBoolean("canRemove"));
	}

	/**
	 * Checks whether the current user has been granted privileges
	 * to remove the specified group.
	 */
	public void testCanRemoveGroup() throws IOException, JSONException {
		testGroupId = createTestGroup();
		testUserId = createTestUser();

		//1. Verify non admin user can not remove group
		String getUrl = HTTP_BASE_URL + "/system/userManager/group/" + testGroupId + ".privileges-info.json";

		//fetch the JSON for the test page to verify the settings.
		Credentials testUserCreds = new UsernamePasswordCredentials(testUserId, "testPwd");

		String json = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json);
		JSONObject jsonObj = new JSONObject(json);
		
		//normal user can not remove group
		assertEquals(false, jsonObj.getBoolean("canRemove"));
		

		//2. now add the user to the 'Group Admin' group.
		addUserToGroupAdminGroup(testUserId);
		
		//fetch the JSON again
		String json2 = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json2);
		JSONObject jsonObj2 = new JSONObject(json2);
		
		//user in 'Group Admin' group can update the properties of groups
		assertEquals(true, jsonObj2.getBoolean("canRemove"));
	}
	
	/**
	 * Checks whether the current user has been granted privileges
	 * to update the membership of the specified group.
	 */
	public void testCanUpdateGroupMembers() throws IOException, JSONException {
		testGroupId = createTestGroup();
		testUserId = createTestUser();

		//1. Verify non admin user can not update group membership
		String getUrl = HTTP_BASE_URL + "/system/userManager/group/" + testGroupId + ".privileges-info.json";

		//fetch the JSON for the test page to verify the settings.
		Credentials testUserCreds = new UsernamePasswordCredentials(testUserId, "testPwd");

		String json = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json);
		JSONObject jsonObj = new JSONObject(json);
		
		//normal user can not remove group
		assertEquals(false, jsonObj.getBoolean("canUpdateGroupMembers"));
		

		//2. now add the user to the 'Group Admin' group.
		addUserToGroupAdminGroup(testUserId);
		
		//fetch the JSON again
		String json2 = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json2);
		JSONObject jsonObj2 = new JSONObject(json2);
		
		//user in 'Group Admin' group can update the membership of groups
		assertEquals(true, jsonObj2.getBoolean("canUpdateGroupMembers"));
		
		
		//3. remove user from the 'Group Admin' group
		removeUserFromGroup(testUserId, "GroupAdmin");
	
		//fetch the JSON again
		String json3 = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json3);
		JSONObject jsonObj3 = new JSONObject(json3);
		
		//user not in 'Group Admin' group can not update the membership of groups
		assertEquals(false, jsonObj3.getBoolean("canUpdateGroupMembers"));
		
		
		//4. add user to the 'User Admin' group
		addUserToUserAdminGroup(testUserId);

		//fetch the JSON again
		String json4 = getAuthenticatedContent(testUserCreds, getUrl, CONTENT_TYPE_JSON, null, HttpServletResponse.SC_OK);
		assertNotNull(json4);
		JSONObject jsonObj4 = new JSONObject(json4);
		
		//user in 'User Admin' group can update the membership of groups
		assertEquals(true, jsonObj4.getBoolean("canUpdateGroupMembers"));
	}
}
