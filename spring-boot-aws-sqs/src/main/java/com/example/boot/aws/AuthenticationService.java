package com.example.boot.aws;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ContainerCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

@Component
@Scope("singleton")
public class AuthenticationService {

	private AWSCredentials credentials;
	
    /*
     * The ProfileCredentialsProvider will return your [default]
     * credential profile by reading from the credentials file located at
     * (~/.aws/credentials).
     */
    public AWSCredentials withProfileCredentialsProvider() {

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.",
                    e);
        } 
        
        return credentials;
    }
    
    /*
     * Implementation that provides credentials by looking at 
     * the aws.accessKeyId and aws.secretKey Java system properties.
     */
    public AWSCredentials withSystemPropertiesCredentialsProvider() {

        try {
            credentials = new SystemPropertiesCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from Java system properties. "
                    + "Please make sure that your properties file is at the correct "
                    + "location (application.properties), and is in valid format.",
                    e);
        } 
        
        return credentials;
    }
    
    /*
     * Implementation that provides credentials by looking at the: 
     * AWS_ACCESS_KEY_ID (or AWS_ACCESS_KEY) and AWS_SECRET_KEY (or AWS_SECRET_ACCESS_KEY) environment variables. 
     * If the AWS_SESSION_TOKEN environment variable is also set then temporary credentials will be used.
     */
    public AWSCredentials withEnvironmentVariableCredentialsProvider() {

        try {
            credentials = new EnvironmentVariableCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from enviroment variables. "
                    + "Please make sure that your enviroment variables are correct. "
                    + "AWS_ACCESS_KEY_ID (or AWS_ACCESS_KEY) and AWS_SECRET_KEY (or AWS_SECRET_ACCESS_KEY) environment variables.",
                    e);
        } 
        
        return credentials;
    }
    
    /*
     * Implementation that loads credentials from an Amazon Elastic Container. 
     * By default, the URI path is retrieved from the environment variable "AWS_CONTAINER_CREDENTIALS_RELATIVE_URI" 
     * in the container's environment.
     */
    public AWSCredentials withContainerCredentialsProvider() {

        try {
            credentials = new ContainerCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from Amazon Elastic Container. "
                    + "Please make sure that your Amazon Elastic Container URI Endpoint is setted up correctly. ",
                    e);
        } 
        
        return credentials;
    }
    
    /*
     * Credentials provider implementation that loads credentials from the Amazon EC2 Instance Metadata Service. 
     */
    public AWSCredentials withInstanceProfileCredentialsProvider() {

        try {
            credentials = new InstanceProfileCredentialsProvider(false).getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from Amazon EC2 Instance Metadata Service. ",e);
        } 
        
        return credentials;
    }
    
    /*
     * Credentials hardcoded.
     */
    public AWSCredentials withBasicAWSCredentials(String accessKey, String secretKey) {

        try {
        	credentials = new BasicAWSCredentials( accessKey , secretKey );
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from basics strings. "
                    + "Please make sure that your credentials are at the correct " +
                    e);
        }
        
        return credentials;
        
    }
    
}
