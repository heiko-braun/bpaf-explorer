/*
 * Copyright 2009 JBoss, a divison Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.bpm.monitor.model;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Sep 23, 2010
 */
public class DataSourceFactory {

    public static final String ENTITY_MANAGER_FACTORY = "bpel/EntityManagerFactory";

    public static BPAFDataSource createDataSource()
    {
        BPAFDataSource ds = null;
        try {
            InitialContext ctx = new InitialContext();
            EntityManagerFactory emf = (EntityManagerFactory)ctx.lookup(ENTITY_MANAGER_FACTORY);
            if(null==emf)
                throw new IllegalStateException("EntityManagerFactory not bound: "+ENTITY_MANAGER_FACTORY);

            ds = new DefaultBPAFDataSource(emf);

        } catch (Exception e) {
            System.out.println("Failed to create service" + e.getMessage());
        }

        return ds;
    }
}
