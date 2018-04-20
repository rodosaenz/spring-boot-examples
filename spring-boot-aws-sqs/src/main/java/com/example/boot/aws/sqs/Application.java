package com.example.boot.aws.sqs;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.services.sqs.model.Message;

@SpringBootApplication
public class Application {
	
	@Autowired
	private QueueService queueService;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @PostConstruct
    private void init() {

    	System.out.println("1 - QueueService start");
    	
    	//If is fiifo use true
    	String queueUrl = queueService.createQueue("test-queue",true);
    	System.out.println("2 - Queue created: " + queueUrl);
    	
    	String text = "Mensaje de prueba";
    	queueService.send(queueUrl, text);
    	System.out.println("3 - Message created: " + text);
    	
    	int quantity = queueService.count(queueUrl);
    	System.out.println("4 - Messages quantity in queue: " + quantity);
    	
    	List<Message> messages = queueService.receive(queueUrl, 10);
    	System.out.println("5 - Messages in queue: " );
    	for (Message message : messages) {
    		
    		System.out.println("-> \t"+ message.getMessageId() + " : " + message.getBody() );
    		queueService.delete(queueUrl, message);
    		System.out.println("-> \tMessage deleted" );
		}
    	
    	queueService.deleteQueue(queueUrl);
    	System.out.println("6 - Queue deleted" );
    	
    	System.out.println("7 - QueueService end");
    }
}
