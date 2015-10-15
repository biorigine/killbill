/*
 * Copyright 2014-2015 Groupon, Inc
 * Copyright 2014-2015 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.util.tag.api;

import org.killbill.billing.callcontext.DefaultCallContext;
import org.killbill.billing.callcontext.InternalCallContext;
import org.killbill.billing.util.UtilTestSuiteNoDB;
import org.killbill.billing.util.api.TagDefinitionApiException;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.InternalCallContextFactory;
import org.killbill.billing.util.tag.TagDefinition;
import org.killbill.billing.util.tag.dao.TagDao;
import org.killbill.billing.util.tag.dao.TagDefinitionDao;
import org.killbill.billing.util.tag.dao.TagDefinitionModelDao;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by maguero on 14/10/15.
 */
public class DefaultTagUserApiTest extends UtilTestSuiteNoDB {

    private DefaultTagUserApi tagUserApi;
    private TagDefinitionDao tagDefinitionDao;
    private CallContext context;

    @BeforeMethod
    public void setUp() throws Exception {
        final TagDao tagDao = Mockito.mock(TagDao.class);
        final InternalCallContextFactory internalCallContextFactory = Mockito.mock(InternalCallContextFactory.class);
        tagDefinitionDao = Mockito.mock(TagDefinitionDao.class);
        tagUserApi = new DefaultTagUserApi(internalCallContextFactory, tagDefinitionDao, tagDao);
        context = Mockito.mock(DefaultCallContext.class);
    }

    @Test(expectedExceptions = TagDefinitionApiException.class, expectedExceptionsMessageRegExp = "The tag definition name must be in lowercase .*")
    public void testCreateTagDefinitionWithUpperCase() throws Exception {
        tagUserApi.createTagDefinition("someUpperCaseLetters", "description", context);
    }

    @Test
    public void testCreateTagDefinitionWithLowerCase() throws Exception {
        final String tagDefinitionName = "lowercase";
        final TagDefinitionModelDao tagDefinitionModelDao = new TagDefinitionModelDao();
        tagDefinitionModelDao.setName(tagDefinitionName);
        Mockito.when(tagDefinitionDao.create(Mockito.anyString(), Mockito.anyString(), Mockito.any(InternalCallContext.class))).thenReturn(tagDefinitionModelDao);
        final TagDefinition tagDefinition = tagUserApi.createTagDefinition(tagDefinitionName, "description", context);
        assertEquals(tagDefinitionName, tagDefinition.getName());
    }
}