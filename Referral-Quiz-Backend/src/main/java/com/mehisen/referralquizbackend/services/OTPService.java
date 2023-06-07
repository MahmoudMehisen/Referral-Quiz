package com.mehisen.referralquizbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;

import java.util.Collections;

@Service
@Slf4j
public class OTPService {
    @Value("${infobip.base-url}")
    private String baseUrl;
    @Value("${infobip.api-key}")
    private String apiKey;
    @Value("${infobip.sender}")
    private String sender;


    public void sendOtpUsingSms(String phone, String otp) {
        ApiClient client = initApiClient();

        SendSmsApi sendSmsApi = new SendSmsApi(client);

        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .from(sender)
                .addDestinationsItem(new SmsDestination().to("2" + phone))
                .text("The OTP message is " + otp);

        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(Collections.singletonList(smsMessage));

        try {
            sendSmsApi.sendSmsMessage(smsMessageRequest);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ApiClient initApiClient() {
        ApiClient client = new ApiClient();

        client.setApiKeyPrefix("App");
        client.setApiKey(apiKey);
        client.setBasePath(baseUrl);

        return client;
    }
}
