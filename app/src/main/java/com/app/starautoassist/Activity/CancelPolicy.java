package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.app.starautoassist.R;

public class CancelPolicy extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Cancellation & Refund Policy");
        setContentView(R.layout.activity_cancel_policy);

        webView = findViewById(R.id.cancel_webview);

        String htmlText = " %s ";
        String myText;
        myText = "<html><body  style=\"text-align:justify;\">";
        myText += "CANCELLATION   AND   REFUND   POLICY\n" +
                "\n" +
                "Please read these Terms and Conditions carefully before using the services offered by Star Auto Assist Sdn Bhd (“Star Auto Assist“) or (“We”, “Us”, or “Our”), a company incorporated under the laws of Malaysia. These terms set forth the legally binding terms and conditions for your use of Our Services as contained in http://www.starautoassist.com (“Website“) and Star Auto Assist platform (“Mobile App”) (collectively with the site, referred to as “Services“).\n" +
                "1. CANCELLATION OF SERVICE\n\n" +
                "Generally, We do not refund your purchase if you cancel it. We will endeavor all reasonable care to ensure that Our Services conform with the standard services in Malaysia. For any disputes please contact our Customer Service hotline number.\n" +
                "2. REFUND OF YOUR PURCHASE\n\n" +
                "Star Auto Assist Sdn Bhd is committed to providing you with excellent services. To arrange for a full refund, please contact our Customer Service hotline number  \n" +
                "As a customer you are responsible for understanding this refund policy upon purchasing any product or services from us. However, we realize that exceptional circumstance can take place with regard to the character of the product or services we provide. \n" +
                "THEREFORE, WE DO HONOR REQUESTS FOR REFUND, WHERE THE FOLLOWING APPLY:\n" +
                "1. Non-delivery of the product or service (non-delivery incident): \n\n" +
                "After the customer submits a request, the mobile-app shows the Estimated Time of Arrival (ETA) of the Ranger to arrive for assistance. Non-delivery incident applies when the Ranger do not arrive after 1 hour of the ETA. For example, if the ETA in the mobile-app is 30 minutes, the customer needs to wait another 1 hour before eligible for a refund (total waiting time 30min + 1hr = 1 hr 30mins) \n" +
                "2. Requests for a refund are accepted within the period of 48 hours of the non-delivery incident\n\n" +
                "3. We encourage all customers to contact us if there is an issue with the product or services received. If a refund has been issued, please allow up to 14 business days for your issuing bank to post the refund back to your account. Issuing banks have different timeframes for posting refunds to your account; therefore you may not see the funds posted to your account despite the fact that we have processed with the refund. \n" +
                "Please note that our Customer Service Team is always ready to provide you with timely and efficient professional assistance at all times.\n" +
                "\n" +
                "4. DELIVERY OF SERVICES\n\n" +
                "We endeavor to adhere to stipulated deadlines as indicated in Our Website and Mobile App. However, delivery deadlines will not be binding unless expressly agreed otherwise.\n" +
                "Furthermore, actual charges may vary based on factors outside of the Company’s control, such as but not limited to weather, traffic conditions and need of special equipment like baby tyre, crane, forklift, additional toll trucks, car park basement towing, etc.\n" +
                "Our contractual obligations are subject to a case to case basis based on your request and additional charges may incur.\n" +
                "When making a booking with us, please ensure that you have ordered the correct and most suitable services.\n" +
                "5. NO WAIVER\n\n" +
                "The failure to exercise or delay in exercising a right or remedy provided hereunder or by law does not constitute a waiver of the right or remedy or waiver of other rights or remedies.\n" +
                "6. GOVERNING LAW\n\n" +
                "These Terms and Conditions shall be governed by and interpreted in accordance with Malaysian law and the parties irrevocably submit to the exclusive jurisdiction of the Malaysian courts.\n" +
                "7. SPECIAL TERMS\n\n" +
                "These Terms and Conditions are not intended to nor shall create any rights, entitlements, claims or benefits enforceable by any person that is not a party to them. No person shall derive any benefit or have any right, entitlement or claim in relation to this Terms and Conditions.\n" +
                "8. CHANGES TO THE TERMS AND CONDITIONS\n\n" +
                "We reserve the right to update these Terms and Conditions at any time and any changes may be made without notice to you. We recommend you to read this Terms and Conditions periodically.\n" +
                "By continuing to access or use Our Services after those revisions or changes become effective, you agree to be bound by the revised terms. If you do not agree to the new Terms and Conditions, please stop using the Website and/or Our Services.";

        myText += "</body></html>";

        myText = myText.replace("\n", "<br>");
        webView.loadData(String.format(htmlText, myText), "text/html", "utf-8");
    }
}
