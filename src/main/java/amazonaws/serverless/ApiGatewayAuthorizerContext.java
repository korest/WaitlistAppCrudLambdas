package amazonaws.serverless;

/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * Context object used for custom authorizers and Cognito User Pool authorizers.
 * <p>
 * Custom authorizers  populate the <code>principalId</code> field. All other custom values
 * returned by the authorizer are accessible via the <code>getContextValue</code> method.
 * <p>
 * Cognito User Pool authorizers populate the <code>claims</code> object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGatewayAuthorizerContext {

    //-------------------------------------------------------------
    // Variables - Private
    //-------------------------------------------------------------

    private Map<String, String> contextProperties = new HashMap<>();
    private String principalId;

    //-------------------------------------------------------------
    // Methods - Public
    //-------------------------------------------------------------

    @JsonAnyGetter
    public String getContextValue(String key) {
        return contextProperties.get(key);
    }


    @JsonAnySetter
    public void setContextValue(String key, String value) {
        contextProperties.put(key, value);
    }


    //-------------------------------------------------------------
    // Methods - Getter/Setter
    //-------------------------------------------------------------

    public String getPrincipalId() {
        return principalId;
    }


    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }
}