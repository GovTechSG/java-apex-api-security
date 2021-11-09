
package com.api.util.testframework.dto;

import java.util.HashMap;
import java.util.Map;
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
 * The Apiparam Schema
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "realm",
    "authPrefix",
    "appId",
    "invokeUrl",
    "signatureUrl",
    "httpMethod",
    "queryString",
    "formData",
    "privateKeyFileName",
    "alias",
    "passphrase",
    "nonce",
    "timestamp",
    "secret",
    "signature"
})
public class ApiParam {

    /**
     * The Realm Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("realm")
    private String realm = "";
    /**
     * The Authprefix Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("authPrefix")
    private String authPrefix = "";
    /**
     * The Appid Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("appId")
    private String appId = "";
    /**
     * The Invokeurl Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("invokeUrl")
    private String invokeUrl = "";
    /**
     * The Signatureurl Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("signatureUrl")
    private String signatureUrl = "";
    /**
     * The Httpmethod Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("httpMethod")
    private String httpMethod = "";
    /**
     * The Querystring Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("queryString")
    private QueryString queryString;
    /**
     * The Formdata Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("formData")
    private FormData formData;
    /**
     * The Privatekeyfilename Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("privateKeyFileName")
    private String privateKeyFileName = "";
    /**
     * The Alias Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("alias")
    private String alias = "";
    /**
     * The Passphrase Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("passphrase")
    private String passPhrase = "";
    /**
     * The Nonce Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("nonce")
    private String nonce = "";
    /**
     * The Timestamp Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    private String timestamp = "";
    /**
     * The Secret Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("secret")
    private String secret = "";
    /**
     * The Signature Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("signature")
    private String signature = "";
    // /**
    //  * The Passphrase Schema
    //  * <p>
    //  * 
    //  * (Required)
    //  * 
    //  */
    // @JsonProperty("passphrase")
    // private String passphrase = "";
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * The Realm Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("realm")
    public String getRealm() {
        return realm;
    }

    /**
     * The Realm Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("realm")
    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * The Authprefix Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("authPrefix")
    public String getAuthPrefix() {
        return authPrefix;
    }

    /**
     * The Authprefix Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("authPrefix")
    public void setAuthPrefix(String authPrefix) {
        this.authPrefix = authPrefix;
    }

    /**
     * The Appid Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("appId")
    public String getAppId() {
        return appId;
    }

    /**
     * The Appid Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("appId")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * The Invokeurl Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("invokeUrl")
    public String getInvokeUrl() {
        return invokeUrl;
    }

    /**
     * The Invokeurl Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("invokeUrl")
    public void setInvokeUrl(String invokeUrl) {
        this.invokeUrl = invokeUrl;
    }

    /**
     * The Signatureurl Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("signatureUrl")
    public String getSignatureUrl() {
        return signatureUrl;
    }

    /**
     * The Signatureurl Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("signatureUrl")
    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    /**
     * The Httpmethod Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("httpMethod")
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * The Httpmethod Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("httpMethod")
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * The Querystring Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("queryString")
    public QueryString getQueryString() {
        return queryString;
    }

    /**
     * The Querystring Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("queryString")
    public void setQueryString(QueryString queryString) {
        this.queryString = queryString;
    }

    /**
     * The Formdata Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("formData")
    public FormData getFormData() {
        return formData;
    }

    /**
     * The Formdata Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("formData")
    public void setFormData(FormData formData) {
        this.formData = formData;
    }

    // /**
    //  * The Privatecertfilename Schema
    //  * <p>
    //  * 
    //  * (Required)
    //  * 
    //  */
    // @JsonProperty("privateCertFileName")
    // public String getPrivateCertFileName() {
    //     return privateCertFileName;
    // }

    // /**
    //  * The Privatecertfilename Schema
    //  * <p>
    //  * 
    //  * (Required)
    //  * 
    //  */
    // @JsonProperty("privateCertFileName")
    // public void setPrivateCertFileName(String privateCertFileName) {
    //     this.privateCertFileName = privateCertFileName;
    // }

    /**
     * The Privatekeyfilename Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("privateKeyFileName")
    public String getPrivateKeyFileName() {
        return privateKeyFileName;
    }

    /**
     * The Privatekeyfilename Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("privateKeyFileName")
    public void setPrivateKeyFileName(String privateKeyFileName) {
        this.privateKeyFileName = privateKeyFileName;
    }
    
    /**
     * The Alias Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("alias")
    public String getAlias() {
        return alias;
    }

    /**
     * The Alias Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    /**
     * The Passphrase Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("passPhrase")
    public String getPassPhrase() {
        return passPhrase;
    }

    /**
     * The Passphrase Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("passPhrase")
    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }

    /**
     * The Nonce Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("nonce")
    public String getNonce() {
        return nonce;
    }

    /**
     * The Nonce Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("nonce")
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    /**
     * The Timestamp Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * The Timestamp Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * The Secret Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("secret")
    public String getSecret() {
        return secret;
    }

    /**
     * The Secret Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("secret")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * The Signature Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("signature")
    public String getSignature() {
        return signature;
    }

    /**
     * The Signature Schema
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("signature")
    public void setSignature(String signature) {
        this.signature = signature;
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
        return new ToStringBuilder(this).append("realm", realm).append("authPrefix", authPrefix).append("appId", appId).append("invokeUrl", invokeUrl).append("signatureUrl", signatureUrl).append("httpMethod", httpMethod).append("queryString", queryString).append("formData", formData).append("privateKeyFileName", privateKeyFileName).append("nonce", nonce).append("timestamp", timestamp).append("secret", secret).append("signature", signature).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(privateKeyFileName).append(signature).append(signatureUrl).append(secret).append(httpMethod).append(queryString).append(nonce).append(appId).append(invokeUrl).append(realm).append(formData).append(additionalProperties).append(authPrefix).append(timestamp).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ApiParam) == false) {
            return false;
        }
        ApiParam rhs = ((ApiParam) other);
        return new EqualsBuilder().append(privateKeyFileName, rhs.privateKeyFileName).append(signature, rhs.signature).append(signatureUrl, rhs.signatureUrl).append(secret, rhs.secret).append(httpMethod, rhs.httpMethod).append(queryString, rhs.queryString).append(nonce, rhs.nonce).append(appId, rhs.appId).append(invokeUrl, rhs.invokeUrl).append(realm, rhs.realm).append(formData, rhs.formData).append(additionalProperties, rhs.additionalProperties).append(authPrefix, rhs.authPrefix).append(timestamp, rhs.timestamp).isEquals();
    }

}
