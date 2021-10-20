
package com.api.util.testframework.dto;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.api.util.testframework.RuntimeTestCase;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * The Items Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "description",
    "apiName",
    "apiParam",
    "expectedResult",
    "message",
    "publicKeyFileName",
    "skipTest",
    "passphrase",
    "errorTest"
})
public class TestDatum {
	
    /**
     * The Id Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private String id = "";
    /**
     * The Description Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("description")
    private String description = "";
    /**
     * The Apiname Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiName")
    private String apiName = "";
    /**
     * The Apiparam Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiParam")
    private ApiParam apiParam;
    /**
     * The Expectedresult Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("expectedResult")
    private ExpectedResult expectedResult;
    /**
     * The Message Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("message")
    private String message = "";
    /**
     * The PublicCertFileName Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("publicKeyFileName")
    private String publicKeyFileName = "";
    /**
     * The SkipTest Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("skipTest")
    private List<Object> skipTest = new ArrayList<Object>();
    /**
     * The Passphrase Schema
     * <p>
     * 
     * 
     */
    @JsonProperty("passphrase")
    private String passphrase = "";
    /**
     * The ErrorTest Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errorTest")
    private Boolean errorTest = false;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * The Id Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * The Id Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * The Description Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * The Description Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The Apiname Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiName")
    public String getApiName() {
        return apiName;
    }

    /**
     * The Apiname Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiName")
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * The Apiparam Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiParam")
    public ApiParam getApiParam() {
        return apiParam;
    }

    /**
     * The Apiparam Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("apiParam")
    public void setApiParam(ApiParam apiParam) {
        this.apiParam = apiParam;
    }

    /**
     * The Expectedresult Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("expectedResult")
    public ExpectedResult getExpectedResult() {
        return expectedResult;
    }

    /**
     * The Expectedresult Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("expectedResult")
    public void setExpectedResult(ExpectedResult expectedResult) {
        this.expectedResult = expectedResult;
    }

    /**
     * The Message Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * The Message Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * The PublicKeyFileName Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("publicKeyFileName")
    public String getPublicKeyFileName() {
        return publicKeyFileName;
    }

    /**
     * The PublicCertFileName Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("publicKeyFileName")
    public void setPublicKeyFileName(String publicKeyFileName) {
        this.publicKeyFileName = publicKeyFileName;
    }

    /**
     * The SkipTest Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("skipTest")
    public List<Object> getSkipTest() {
        return skipTest;
    }

    /**
     * The SkipTest Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("skipTest")
    public void setSkipTest(List<Object> skipTest) {
        this.skipTest = skipTest;
    }

    /**
     * The Passphrase Schema
     * <p>
     * 
     * 
     */
    @JsonProperty("passphrase")
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * The Passphrase Schema
     * <p>
     * 
     * 
     */
    @JsonProperty("passphrase")
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    /**
     * The ErrorTest Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errorTest")
    public Boolean getErrorTest() {
        return errorTest;
    }

    /**
     * The ErrorTest Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errorTest")
    public void setErrorTest(Boolean errorTest) {
        this.errorTest = errorTest;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("description", description).append("apiName", apiName).append("apiParam", apiParam).append("expectedResult", expectedResult).append("message", message).append("publicCertFileName", publicKeyFileName).append("skipTest", skipTest).append("passphrase", passphrase).append("errorTest", errorTest).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(skipTest).append(apiName).append(publicKeyFileName).append(expectedResult).append(description).append(passphrase).append(id).append(additionalProperties).append(message).append(apiParam).append(errorTest).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TestDatum) == false) {
            return false;
        }
        TestDatum rhs = ((TestDatum) other);
        return new EqualsBuilder().append(skipTest, rhs.skipTest).append(apiName, rhs.apiName).append(publicKeyFileName, rhs.publicKeyFileName).append(expectedResult, rhs.expectedResult).append(description, rhs.description).append(passphrase, rhs.passphrase).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).append(message, rhs.message).append(apiParam, rhs.apiParam).append(errorTest, rhs.errorTest).isEquals();
    }

}
