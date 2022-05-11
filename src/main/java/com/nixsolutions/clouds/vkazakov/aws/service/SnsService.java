package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.util.SnsConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import software.amazon.awssdk.services.sns.model.ListSubscriptionsByTopicRequest;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
@RequiredArgsConstructor
public class SnsService {
    private final SnsConstants snsConstants;
    private final SnsClient snsClient;

    public String pubTopic(String subject, String text) {
        try {
            PublishRequest request = PublishRequest.builder()
                .subject(subject)
                .message(text)
                .topicArn(snsConstants.getTopicArn())
                .build();
            PublishResponse result = snsClient.publish(request);
            return "Message sent." + " Status was " + result.sdkHttpResponse().statusCode();
        } catch (SnsException exception) {
            log.error(exception.awsErrorDetails().errorMessage());
        }
        return "Error - msg not sent";
    }

    public void unsubscriptionWithEmail(String emailEndpoint) {
        try {
            String subscriptionArn = getTopicArnValue(emailEndpoint);
            UnsubscribeRequest request = UnsubscribeRequest.builder()
                .subscriptionArn(subscriptionArn)
                .build();
            snsClient.unsubscribe(request);
        } catch (SnsException exception) {
            log.error(exception.awsErrorDetails().errorMessage());
        }
    }

    private String getTopicArnValue(String endpoint){
        try {
            List<Subscription> allSubscriptions
                = snsClient.listSubscriptionsByTopic(ListSubscriptionsByTopicRequest.builder()
                .topicArn(snsConstants.getTopicArn()).build()).subscriptions();

            for (Subscription sub: allSubscriptions) {
                if (sub.endpoint().compareTo(endpoint)==0) {
                    return sub.subscriptionArn();
                }
            }
        } catch (SnsException exception) {
            log.error(exception.awsErrorDetails().errorMessage());
        }
        return "";
    }

    public String subscriptionWithEmail(String email) {
        try {
            SubscribeRequest request = SubscribeRequest.builder()
                .protocol("email")
                .endpoint(email)
                .returnSubscriptionArn(true)
                .topicArn(snsConstants.getTopicArn())
                .build();
            return snsClient.subscribe(request).subscriptionArn() ;
        } catch (SnsException exception) {
            log.error(exception.awsErrorDetails().errorMessage());
        }
        return "";
    }

    public String getAllSubscriptions() {
        List<String> subList = new ArrayList<>() ;
        try {
            List<Subscription> allSubs  = snsClient.listSubscriptionsByTopic(
                ListSubscriptionsByTopicRequest.builder()
                .topicArn(snsConstants.getTopicArn())
                .build()).subscriptions();
            for (Subscription sub: allSubs) {
                subList.add(sub.endpoint());
            }
        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
        return convertToString(convertToXml(subList));
    }

    private Document convertToXml(List<String> subsList) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("Subs");
            doc.appendChild(root);

            for (String sub : subsList) {
                Element item = doc.createElement("Sub");
                root.appendChild(item);
                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(sub));
                item.appendChild(email);
            }
            return doc;
        } catch(ParserConfigurationException exception){
            log.error(exception.getMessage());
        }
        return null;
    }

    private String convertToString(Document xml) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(xml);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch(TransformerException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }
}
