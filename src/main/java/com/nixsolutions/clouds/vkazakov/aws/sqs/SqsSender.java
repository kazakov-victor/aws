package com.nixsolutions.clouds.vkazakov.aws.sqs;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nixsolutions.clouds.vkazakov.aws.dto.UserDtoTest;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Base64;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.amazonaws.services.sqs.AmazonSQS;
//import com.amazonaws.services.sqs.model.SendMessageRequest;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/sqs")
//public class SqsSender {
//    private final AmazonSQS sqs;
//    @GetMapping("/send")
//    String getSubs() throws IOException {
//        Path path = Paths.get("", "er2.jpg");
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream(
//            (int) (Files.size(path) * 4 / 3 + 4));
//        try (OutputStream base64Stream = Base64.getEncoder().wrap(bytes)) {
//            Files.copy(path, base64Stream);
//        }
//        String base64 = bytes.toString(StandardCharsets.US_ASCII);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        UserDtoTest userDtoTest =
//            new UserDtoTest("gret", "jol", "opplop", "vmailua@meta.ua",
//                "vuiooer", "2000-09-05", "2", "+380506667744", base64);
//        String json = objectMapper.writeValueAsString(userDtoTest);
//
//
//        SendMessageRequest send_msg_request = new SendMessageRequest()
//            .withQueueUrl("https://sqs.eu-central-1.amazonaws.com/463050938679/MyStandartQueue")
//            .withMessageBody(json);
//        sqs.sendMessage(send_msg_request);
//        return "Ok";
//
//
//
//    }
//}
