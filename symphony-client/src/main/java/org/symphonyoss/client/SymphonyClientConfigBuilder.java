/*
 *
 * Copyright 2018 The Symphony Software Foundation
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.symphonyoss.client;

import java.util.List;
import java.util.ArrayList;


/**
 * This class provides a simpler and more foolproof way of correctly constructing SymphonyClientConfig objects.  It
 * users the "Step Builder" pattern, which means that callers of this class will be required to set the mandatory
 * configuration properties in a fixed order (not doing so will be a compile-time error), while also having the option
 * of setting optional properties when desired.
 *
 * To use this class, start with <code>SymphonyClientConfigBuilder.newBuilder</code>, then continue calling the
 * <code>withXXX</code> methods until such time as the <code>build</code> method becomes visible.
 */
public class SymphonyClientConfigBuilder
{
    private SymphonyClientConfigBuilder()
    {
        throw new RuntimeException("This class should not be instantiated.  Use the newBuilder method to get started.");
    }

    public static SessionAuthUrlStep newBuilder()
    {
        return new Steps();
    }

    public static interface SessionAuthUrlStep
    {
        KeyAuthUrlStep withSessionAuthUrl(String sessionAuthUrl);
    }

    public static interface KeyAuthUrlStep
    {
        PodUrlStep withKeyAuthUrl(String keyAuthUrl);
    }

    public static interface PodUrlStep
    {
        AgentUrlStep withPodUrl(String podUrl);
    }

    public static interface AgentUrlStep
    {
        TrustStoreStep withAgentUrl(String agentUrl);
    }

    public static interface TrustStoreStep
    {
        UserCredsStep withTrustStore(String trustStoreFilename, char[] trustStorePassword);
    }

    public static interface UserCredsStep
    {
        BuildStep withUserCreds(String userEmail, String userCertFilename, char[] userCertPassword);
    }

    public static interface BuildStep
    {
        BuildStep withReceiverEmail(String receiverEmail);
        BuildStep withServices(boolean enabled);
        BuildStep withJMXHealthcheck(boolean enabled);
        SymphonyClientConfig build();
    }

    private static class Steps
        implements SessionAuthUrlStep,
                   KeyAuthUrlStep,
                   PodUrlStep,
                   AgentUrlStep,
                   TrustStoreStep,
                   UserCredsStep,
                   BuildStep
    {
        private String  sessionAuthUrl        = null;
        private String  keyAuthUrl            = null;
        private String  podUrl                = null;
        private String  agentUrl              = null;
        private String  trustStoreFilename    = null;
        private char[]  trustStorePassword    = null;
        private String  userEmail             = null;
        private String  userCertFilename      = null;
        private char[]  userCertPassword      = null;
        private String  receiverEmail         = null;
        private boolean servicesEnabled       = true;
        private boolean jmxHealthCheckEnabled = true;

        @Override
        public KeyAuthUrlStep withSessionAuthUrl(String sessionAuthUrl)
        {
            this.sessionAuthUrl = sessionAuthUrl;
            return (this);
        }

        @Override
        public PodUrlStep withKeyAuthUrl(String keyAuthUrl)
        {
            this.keyAuthUrl = keyAuthUrl;
            return (this);
        }

        @Override
        public AgentUrlStep withPodUrl(String podUrl)
        {
            this.podUrl = podUrl;
            return (this);
        }

        @Override
        public TrustStoreStep withAgentUrl(String agentUrl)
        {
            this.agentUrl = agentUrl;
            return (this);
        }

        @Override
        public UserCredsStep withTrustStore(String trustStoreFilename, char[] trustStorePassword)
        {
            this.trustStoreFilename = trustStoreFilename;
            this.trustStorePassword = trustStorePassword;
            return (this);
        }

        @Override
        public BuildStep withUserCreds(String userEmail, String userCertFilename, char[] userCertPassword)
        {
            this.userEmail = userEmail;
            this.userCertFilename = userCertFilename;
            this.userCertPassword = userCertPassword;
            return (this);
        }

        @Override
        public BuildStep withReceiverEmail(String receiverEmail)
        {
            this.receiverEmail = receiverEmail;
            return (this);
        }

        @Override
        public BuildStep withServices(boolean enabled)
        {
            this.servicesEnabled = enabled;
            return (this);
        }

        @Override
        public BuildStep withJMXHealthcheck(boolean enabled)
        {
            this.jmxHealthCheckEnabled = enabled;
            return (this);
        }

        @Override
        public SymphonyClientConfig build()
        {
            SymphonyClientConfig result = new SymphonyClientConfig();

            result.set(SymphonyClientConfigID.SESSIONAUTH_URL, this.sessionAuthUrl);
            result.set(SymphonyClientConfigID.KEYAUTH_URL, this.keyAuthUrl);
            result.set(SymphonyClientConfigID.POD_URL, this.podUrl);
            result.set(SymphonyClientConfigID.AGENT_URL, this.agentUrl);
            result.set(SymphonyClientConfigID.TRUSTSTORE_FILE, this.trustStoreFilename);
            result.set(SymphonyClientConfigID.TRUSTSTORE_PASSWORD, new String(this.trustStorePassword));  // SECURITY HOLE DUE TO STRING INTERNING
            result.set(SymphonyClientConfigID.USER_EMAIL, this.userEmail);
            result.set(SymphonyClientConfigID.USER_CERT_FILE, this.userCertFilename);
            result.set(SymphonyClientConfigID.USER_CERT_PASSWORD, new String(this.userCertPassword));    // SECURITY HOLE DUE TO STRING INTERNING
            result.set(SymphonyClientConfigID.RECEIVER_EMAIL, this.receiverEmail);
            result.set(SymphonyClientConfigID.DISABLE_SERVICES, String.valueOf(!this.servicesEnabled));
            result.set(SymphonyClientConfigID.HEALTHCHECK_JMX_ENABLED, String.valueOf(this.jmxHealthCheckEnabled));

            return (result);
        }
    }
}
