/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.sling.launchpad.webapp.integrationtest;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.commons.testing.integration.HttpTestBase;

/**
 * Test of Davex
 */
public class DavExIntegrationTest extends HttpTestBase {

    private Repository repository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        repository = JcrUtils.getRepository(HTTP_BASE_URL + "/server/");
    }

    public void testDescriptor() throws Exception {
        assertEquals("2.0", repository.getDescriptor(Repository.SPEC_VERSION_DESC));
        assertEquals("Jackrabbit", repository.getDescriptor(Repository.REP_NAME_DESC));
    }

    public void testReadNode() throws Exception {
        final String path = "/DavExNodeTest_1_" + System.currentTimeMillis();
        final String url = HTTP_BASE_URL + path;

        // add some properties to the node
        final Map<String, String> props = new HashMap<String, String>();
        props.put("name1", "value1");
        props.put("name2", "value2");

        testClient.createNode(url, props);

        Session session = repository.login("default");

        Node node = session.getNode(path);

        assertNotNull(node);
        assertEquals("value1", node.getProperty("name1").getString());
        assertEquals("value2", node.getProperty("name2").getString());

        session.logout();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        repository = null;
    }
}
