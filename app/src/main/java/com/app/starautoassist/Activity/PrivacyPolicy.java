package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.app.starautoassist.R;

public class PrivacyPolicy extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Privacy Policy");
        setContentView(R.layout.activity_privacy_policy);

        webView = findViewById(R.id.privacy_webview);

        String htmlText = " %s ";
        String myText;
        myText = "<html><body  style=\"text-align:justify;\">";
        myText += "PRIVACY   POLICY\n\n" +
                "PLEASE READ THIS PRIVACY POLICY CAREFULLY. BY ACCESSING OR USING OUR WEBSITE AND MOBILE APP, YOU ACKNOWLEDGE THAT YOU HAVE READ, UNDERSTAND, AND AGREE TO BE BOUND TO ALL THE TERMS OF THIS PRIVACY POLICY AND OUR WEBSITE TERMS OF USE. IF YOU DO NOT AGREE TO THESE TERMS, EXIT THIS PAGE AND DO NOT ACCESS OR USE THE WEBSITE.\n" +
                "\n" +
                "1. STATEMENT\n\n" +
                "This Privacy Policy discloses the privacy practices for www.starautoassist.com (“Website” or “Mobile App”). Star Auto Assist Sdn Bhd, (“Us” or “We” or “Our”), the provider of the Website is committed to protecting your privacy online. Please read the following to learn what information We collect from you (“User”) and how We use that information. \n" +
                "\n" +
                "2. CHANGES TO PRIVACY POLICY\n\n" +
                "If We decide to change our Privacy Policy, We will post those changes on the website, mobile app and other places We deem appropriate so that you are aware of what information We collect, how We use it, and under what circumstances, if any, We disclose such information. We reserve the right to modify this Privacy Policy at any time, so please review it frequently. If we make substantial changes to this policy, we will notify you here, either by email, or by means of notice on our home page.\n" +
                "\n" +
                "3. PRIVACY POLICIES OF THIRD PARTY WEBSITES\n\n" +
                "This Privacy Policy only addresses the use and disclosure of information We collect from you on www.starautoassist.com Website and Mobile App. Other websites that may be accessible through this Website have their own privacy policies and data collection, use and disclosure practices. If you link to any such website, We urge you review the website’s privacy policy. We are not responsible for the policies or practices of third parties.\n" +
                "\n" +
                "4. MISCELLANEOUS PRIVACY ISSUES\n\n" +
                "Minors under the age of 18 may not use the Website and Mobile App. We do not knowingly collect personal information from anyone under the age of 18, and no part of the Website and Mobile App is designed to attract anyone under the age of 18. We do not sell Our services to and for children or minors.\n" +
                "\n" +
                "5. SECURITY OF INFORMATION\n\n" +
                "We take security seriously and take numerous precautions to protect the security of your information. Rest assured your information resides on a secure server that only Our selected personnel and contractors have access to. We encrypt certain sensitive information (such as credit card information) using Secure Socket Layer (SSL) technology to ensure that your Personally Identifiable Information is safe as it is transmitted to us.\n" +
                "\n" +
                "Unfortunately, no data transmission over the Internet or any wireless network can be guaranteed to be 100% secure. As a result, while We employ commercially reasonable security measures to protect data and seek to partner with companies which do the same, We cannot guarantee the security of any information transmitted to or from the Website, and are not responsible for the actions of any third parties that may receive any such information.\n" +
                "\n" +
                "6. TYPES OF INFORMATION COLLECTED AND USE OF COLLECTED INFORMATION\n\n" +
                "We collect two types of information about our Website Users: Personally Identifiable Information and Non-Personally Identifiable Information.\n" +
                "\n" +
                "7. PERSONALLY IDENTIFIABLE INFORMATION\n\n" +
                "Personally Identifiable Information is information that pertains to a specific User. When you engage in certain activities on the Website and Mobile App, such as ordering a service from Us or Our partners, submitting content and/or entering a contest or sweepstakes sponsored by Us or Our partners, filling out a survey, posting a review, sending Us or Our partners feedback, requesting information about Our services, submitting an affiliate agreement, posting a video or pictures or signing up for special offers from third parties through the Website and Mobile App (collectively, “Identification Activities”), We may ask you to provide certain information about yourself. It is optional for you to engage in an Identification Activity. If you elect to engage in an Identification Activity, however, We may ask you to provide Us with certain personal information about yourself, such as your first and last name, mailing address (including postcode), email address, telephone number and others. When you order Our services, We may also ask you to provide Us with your credit card number, expiration date and authentication codes or related information. Depending on the activity, some of the information We ask you to provide is identified as mandatory and some is identified as voluntary. If you do not provide the mandatory information for a particular activity that requires it, you will not be permitted to engage in that activity.\n" +
                "\n" +
                "8. NON-PERSONALLY IDENTIFIABLE INFORMATION\n\n" +
                "Non-Personally Identifiable Information is information that does not identify a specific End User. This type of information may include things like the Uniform Resource Locator (“URL”) of the website you visited before coming to our Website and Mobile App, the URL of the website you visit after leaving our Website and Mobile App, the type of browser you are using and your Internet Protocol (“IP”) address. We, and/or our authorized Third Party Service Providers and Advertisers, may automatically collect this information when you visit our Website through the use of electronic tools like Cookies and Web beacons or Pixel tags, as described in this Privacy Policy.\n" +
                "\n" +
                "We use Non-Personally Identifiable Information to troubleshoot, administer the Website and Mobile App, analyze trends, gather demographic information, comply with applicable law, and cooperate with law enforcement activities. We may also share this information with our authorized Third Party Service Providers and Advertisers to measure the overall effectiveness of our online advertising, content, and programming.\n" +
                "\n" +
                "9. RELEASE OF PERSONALLY IDENTIFIABLE INFORMATION\n\n" +
                "We will not share, sell, rent, or trade your Personally Identifiable Information with other parties except as provided below:\n" +
                "\n" +
                "We may share your information with Authorized Third Party Service Providers. These “Third Party Service Providers” perform functions on Our behalf, like sending out and distributing our administrative and promotional emails. We may share your Personally Identifiable Information with such Service Providers to fulfill orders, deliver packages, send postal or email, administer contests or sweepstakes, remove repetitive information on customer lists, analyze data, provide marketing assistance, provide search results and links, process credit card payments, operate the Website and Mobile App, troubleshoot, and provide customer service. We may also collect personal information from individuals and companies (“Affiliates”) with whom we have business relationships and may share your information with Service Providers to accomplish our administrative tasks. For example, when you order a service, We release your credit card information to the card-issuing bank to confirm payment for the service and, if applicable, release your address to the delivery service to deliver the service. Likewise, We may release an Affiliate’s information to our bank to send out a payment. We encourage Our Third Party Service Providers to adopt and post privacy policies. However, the use of your Personally Identifiable Information by such parties is governed by the privacy policies of such parties and is not subject to our control.\n" +
                "\n" +
                "We may remarket your information. Remarketing is a way for Us to connect with users, based upon your past interactions with our Website and Mobile App. Third-party marketing vendors may be hired by Us to perform remarketing services.\n" +
                "\n" +
                "We may share your information in a Business Transfer. As with any other business, We could merge with, or be acquired by another company. If this occurs, the successor company would acquire the information we maintain, including Personally Identifiable Information. However, Personally Identifiable Information would remain subject to this Privacy Policy or any changes there to.\n" +
                "\n" +
                "We reserve the right to disclose your personally identifiable information as required by law and when we believe that disclosure is necessary to protect our rights and/or comply with a judicial proceeding, court order, or legal process served on our Website and Mobile App; enforce or apply this Privacy Policy, our Website And Mobile App Terms of Use or other agreements; or protect the rights, property or safety of the Website and Mobile App, its Users or others.\n" +
                "\n" +
                "10. RELEASE OF NON-PERSONALLY IDENTIFIABLE INFORMATION\n\n" +
                "We may disclose or share Non-Personally Identifiable Information with Partners, Affiliates and Advertisers. For example, we may share aggregated demographic information (which does not include any Personally Identifiable Information) with “Third Party Advertisers” or “Third Party Advertising Companies” and other parties as provided below:\n" +
                "\n" +
                "We also use Third Party Service Providers to track and analyze Non-Personally Identifiable usage and volume statistical information from our Users to administer our Website and Mobile App and constantly improve its quality. We may also publish this information for promotional purposes or as a representative audience for Advertisers. Please note that this is not Personally Identifiable Information, only general summaries of the activities of our Users. Such data is collected on our behalf, and is owned and used by us.\n" +
                "\n" +
                "11. USER CHOICES ON COLLECTION AND USE OF INFORMATION\n\n" +
                "As discussed above, you can always choose not to provide information, although it may be required to engage in a certain activity on the Website and Mobile App.\n" +
                "\n" +
                "\n" +
                "Data Tracking\n" +
                "\n" +
                "In order to facilitate and customize your experience with the Website and Mobile App, we store cookies on your computer. A cookie is a small text file that is stored on a User’s computer for record-keeping purposes which contains information about that User. We use cookies to save you time while using the Website and Mobile App, remind Us who you are, and track and target User interests in order to provide a customized experience. Cookies also allow us to collect Non-Personally Identifiable Information from you, like which pages you visited and what links you clicked on. Use of this information helps Us to create a more user-friendly experience for all visitors. In addition, we may use Third Party Advertising Companies to display advertisements on Our Website and Mobile App. As part of their service, they may place separate cookies on your computer. We have no access to or control over these cookies. This Privacy Policy covers the use of cookies by our Website and Mobile App only and does not cover the use of cookies by any Advertiser. Most browsers automatically accept cookies, but you may be able to modify your browser settings to decline cookies. Please note that if you decline or delete these cookies, some parts of the Website and Mobile App may not work properly.\n" +
                "\n" +
                "Other Tracking Devices\n" +
                "\n" +
                "We may use other industry standard technologies like pixel tags and web beacons to track your use of Our Website and Mobile App pages and promotions, or We may allow Our Third Party Service Providers to use these devices on Our behalf. Pixel tags and web beacons are tiny graphic images placed on certain pages on Our Website, or in Our emails that allow Us to determine whether you have performed a specific action. When you access these pages or open or click an email, pixel tags and web beacons generate a Non-Personally Identifiable notice of that action. Pixel tags allow Us to measure and improve our understanding of visitor traffic and behavior on Our Website, as well as give Us a way to measure Our promotions and performance. We may also utilize pixel tags and web beacons provided by Our Affiliates and/or Marketing Partners for the same purposes.\n" +
                "\n" +
                "12. PRIVACY AND PROTECTION OF PERSONAL INFORMATION.\n\n" +
                "See the Privacy Policy disclosures relating to the collection and use of your informations.";
        myText += "</body></html>";

        myText = myText.replace("\n", "<br>");
        webView.loadData(String.format(htmlText, myText), "text/html", "utf-8");
    }
}
