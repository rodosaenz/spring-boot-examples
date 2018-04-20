package com.example.boot.aws.sqs;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class QueueService  {

    private AmazonSQS amazonSQS = null;

    @PostConstruct
    public void createSQSClient() {
    	amazonSQS = AmazonSQSClientBuilder
            		.standard()
            		.withRegion(Regions.US_EAST_1)
            		.build();
    }


    public String createQueue(String name, boolean isFifo) {

        try {
        	final Map<String, String> attributes = new HashMap<String, String>();
        	if(isFifo) {
        		name = name + ".fifo";
        		// A FIFO queue must have the FifoQueue attribute set to True
        		attributes.put("FifoQueue", "true");
        		// If the user doesn't provide a MessageDeduplicationId, generate a MessageDeduplicationId based on the content.
        		attributes.put("ContentBasedDeduplication", "true");
        	}
        	
        	
			CreateQueueRequest createQueueRequest 	= new CreateQueueRequest(name).withAttributes(attributes);
			CreateQueueResult createQueueResult 	= amazonSQS.createQueue(createQueueRequest);
			String queueUrl = createQueueResult.getQueueUrl();
			return queueUrl;
			
		} catch (AmazonServiceException e) {
			System.out.println(" ERROR " + e.getMessage());
			e.printStackTrace();
		} catch (AmazonClientException e) {
			System.out.println(" ERROR " + e.getMessage());
			e.printStackTrace();
		}

        return null;
    }

    public boolean send(String queueUrl, String message) {
        try {
        	
        	SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, message);
        	if(queueUrl.endsWith(".fifo")) {
        		sendMessageRequest.withMessageDeduplicationId( UUID.randomUUID().toString() );
        		sendMessageRequest.withMessageGroupId("SpringBootExamples");
        	}
        	
        	amazonSQS.sendMessage(sendMessageRequest);
            return true;
            
        } catch (AmazonServiceException ase) {
        	System.out.println(ase.getErrorMessage());
            return false;
        } catch (AmazonClientException ace) {
        	System.out.println(ace.getMessage());
            return false;
        }
    }

    public List<Message> receive(String queueUrl, int max)  {
        
        List<Message> messages = null;
        try {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                    .withMaxNumberOfMessages(max);
            messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();

        } catch (AmazonServiceException ase) {
        	System.out.println(ase.getErrorMessage());
        } catch (AmazonClientException ace) {
        	System.out.println(ace.getMessage());
        }

        return messages;
    }

    public void delete(String queueUrl, Message message) {

        String messageReceiptHandle = message.getReceiptHandle();
        amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
    }

    public void deleteQueue(String queueUrlName) {
    	amazonSQS.deleteQueue(new DeleteQueueRequest(queueUrlName));
    }

    public int count(String queueUrl) {

        int count;
        GetQueueAttributesRequest request = new GetQueueAttributesRequest(queueUrl)
                .withAttributeNames("All");

        Map<String, String> attributes = amazonSQS.getQueueAttributes(request).getAttributes(); 
        String approximateNumberOfMessages = attributes.get("ApproximateNumberOfMessages");
        String approximateNumberOfMessagesNotVisible = attributes.get("ApproximateNumberOfMessagesNotVisible");
        count = Integer.parseInt(approximateNumberOfMessages) + Integer.parseInt(approximateNumberOfMessagesNotVisible);

        return count;
    }
}
