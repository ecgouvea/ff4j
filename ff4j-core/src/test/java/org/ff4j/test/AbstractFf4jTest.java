package org.ff4j.test;

/*
 * #%L
 * ff4j-core
 * %%
 * Copyright (C) 2013 Ff4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;

import org.ff4j.FF4j;
import org.ff4j.FF4jContext;
import org.ff4j.feature.FeatureStore;
import org.ff4j.feature.ToggleStrategy;
import org.ff4j.security.FF4JSecurityManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract Internal Class to Test FF4J.
 * 
 * @author Cedrick Lunven (@clunven)
 */
public abstract class AbstractFf4jTest implements TestConstantsFF4j {

    /** Mock Authorizaton Manager. */
    protected static FF4JSecurityManager mockAuthManager = null;

    /** Mock Flip Strategy. */
    protected static ToggleStrategy mockFlipStrategy = null;

    /** FF4J Instance for testings. */
    protected FF4j ff4j = null;

    /** Test Values */
    protected AssertFF4j assertFf4j = null;

    /** {@inheritDoc} */
    @Before
    public void setUp() throws Exception {
        // Create MOCK
        mockAuthManager = mock(FF4JSecurityManager.class);
        when(mockAuthManager.getCurrentUserPermissions()).thenReturn(new HashSet<String>(Arrays.asList(new String[] {"ROLEA"})));
        when(mockAuthManager.listAllPermissions()).thenReturn(new HashSet<String>(Arrays.asList(new String[] {"ROLEA","ROLEB"})));
        when(mockAuthManager.toJson()).thenReturn("{ value : 1 }");
        
        // Create MOCK
        mockFlipStrategy = mock(ToggleStrategy.class);
        when(mockFlipStrategy.evaluate(any(String.class), any(FeatureStore.class), any(FF4jContext.class)))
                .thenReturn(true);
        when(mockFlipStrategy.getInitParams()).thenReturn(null);

        // Init of FF4J
        this.ff4j = initFF4j();

        // Tester
        this.assertFf4j = new AssertFF4j(ff4j);
    }

    /**
     * Mocks have been correctly setup.
     * 
     * @throws IOException
     *             error during initializations.
     */
    @Test
    public void testMock() throws IOException {
        Assert.assertTrue(mockFlipStrategy.evaluate("", null, null));
        Assert.assertEquals(mockAuthManager.getCurrentUserPermissions(),
                new HashSet<String>(Arrays.asList(new String[] {"ROLEA"})));
    }

    /**
     * Each Test will create its instance of FF4J.
     * 
     * @return instance of FF4J for testing purposes.
     */
    public abstract FF4j initFF4j();
}
