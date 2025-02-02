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

package org.apache.ignite.internal.processors.security.thread;

import org.apache.ignite.internal.processors.security.IgniteSecurity;
import org.apache.ignite.internal.processors.security.OperationSecurityContext;
import org.apache.ignite.internal.processors.security.SecurityContext;

/**
 * Represents a {@link Runnable} wrapper that executes the original {@link Runnable} with the security context
 * current at the time the wrapper was created.
 */
public class SecurityAwareRunnable implements Runnable {
    /** */
    private final Runnable delegate;

    /** */
    private final IgniteSecurity security;

    /** */
    private final SecurityContext secCtx;

    /** */
    public SecurityAwareRunnable(IgniteSecurity security, Runnable delegate) {
        assert security.enabled();
        assert delegate != null;

        this.delegate = delegate;
        this.security = security;
        secCtx = security.securityContext();
    }

    /** {@inheritDoc} */
    @Override public void run() {
        try (OperationSecurityContext ignored = security.withContext(secCtx)) {
            delegate.run();
        }
    }
}
