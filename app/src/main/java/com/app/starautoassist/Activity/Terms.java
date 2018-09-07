package com.app.starautoassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.app.starautoassist.R;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Terms extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Terms & Conditions");
        setContentView(R.layout.activity_terms);

        webView = findViewById(R.id.webview);

        String htmlText = " %s ";

        String myText;
        myText = "<html><body  style=\"text-align:justify;\">";
        myText += "GENERAL   TERMS   AND   CONDITIONS\n\n" +
                "Please read these Terms and Conditions carefully before using the services offered by Star Auto Assist Sdn Bhd (“Star Auto Assist“) or (“We”, “Us”, or “Our”), a company incorporated under the laws of Malaysia. These terms set forth the legally binding terms and conditions for your use of Our Services as contained in http://starautoassist.com (“Website, Mobile App and Rangers“) (collectively with the site, referred to as “Services“). “Rangers” is defined as technician including tow truck operators who is assisting customers on site. “Membership Cardholder” is defined as customers who signed up for special membership packages offered by Star Auto Assist Sdn Bhd.\n" +
                "1. GENERAL TERMS\n\n" +
                "Any such purchasing terms and conditions shall apply only if we expressly confirm the same in writing. Acceptance of your delivered services shall be deemed acknowledgement of our terms and conditions. Please also see our Cancellation and Refund Terms and Conditions.\n" +
                "\n" +
                "2. USE OF INTERNET AS MEDIUM OF COMMUNICATION\n\n" +
                "You acknowledge that the internet is not a secure medium and information submitted for the Services hosted on Our Website and Mobile App may be accessed by third parties. Star Auto Assist accepts no liability whatsoever in this circumstance.\n" +
                "\n" +
                "3. DOCUMENTATION OF OUR SERVICES\n\n" +
                "Any form of documentation provided by Us must not be copied or made available to any third party or use for any other purpose other than the intended and agreed purpose as indicated in Our Website and Mobile App.\n" +
                "\n" +
                "4. EXECUTION OF SERVICE\n\n" +
                "We endeavour to adhere to stipulated service deadlines as indicated in Our Website and Mobile Apps. However, due to the hazards and peculiar features of Our Services, service execution deadlines will not be binding unless expressly agreed otherwise. Our contractual obligations are subject to a case to case basis based on your request and additional charges may incur.\n" +
                "\n" +
                "5. LIMITATION\n\n" +
                "5.1 Impractical Conditions\n\n" +
                "Nothing in this terms and conditions shall require the Us to service at any location from or to which it is impracticable, through no fault or neglect of the Us to operate vehicles because of:\n" +
                "The condition of roads, streets, driveways or alleys or Riots, Acts of God, the public enemy, the authority of law, strikes or labor unrest the existence of violence, or such possible disturbances as to create reasonable apprehension of danger to person or property.\n" +
                "5.2 Tow Truck Transport and Goods Limitation\n\n" +
                "There are a number of prohibitions for the use of Star Auto Assist services. The following cases are specifically prohibited: \n" +
                "a.ABSOLUTELY NO CARRIAGE OF ANY PERSON in the vehicle carried by tow truck for any distance or reason ever.\n" +
                "b.ABSOLUTELY NO CARRIAGE OF ANY ANIMAL in the vehicle carried by tow truck for any distance or reason ever.\n" +
                "c.Items of extraordinary value or items that are irreplaceable should not be left the vehicle carried by tow truck\n" +
                "d.NO Hazardous Materials to be carried the vehicle carried by tow truck, but is not limited to: explosives, gases, flammable liquids, flammable solids, poisonous or infectious substances, radioactive material, corrosives.\n" +
                "e.NO firearms, ammunition or other explosive materials the vehicle carried by tow truck.\n" +
                "f.NO illegal goods the vehicle carried by tow truck.\n" +
                "5.3 Limitations Of Ranger's Liability For Premise Clearance\n" +
                "Ranger assumes no responsibility for ensuring or otherwise providing for clearance through inspection by premise’s management. Rangers is not responsible for the application for entry permit into premise if the management rule and regulation require entry pass or permit to be applied beforehand. Ranger or party in possession shall not be liable for loss, damage, deterioration of the vehicle or delay in delivery due to the process for management clearance or inspection.\n" +
                "\n" +
                "6. APPLICATION OF ACCESSORIAL CHARGES\n\n" +
                "In addition to the base rate and additional services requested for any booking and unless otherwise agreed in writing, the following accessorial charges shall apply and shall be reflected on the amended booking details and receipt shall the customer requested for one.\n" +
                "a.Baby tyre, crane, forklift, additional toll trucks, etc. \n" +
                "b.Special equipment for customized and modified car\n" +
                "c.Car park towing and basement towing\n" +
                "d.Highland areas such as Penang, Langkawi, Genting Highlands, Cameron Highlands and Fraser Hill and any other highlands. \n" +
                "e.Ferry charges \n" +
                "f.Natural disasters such as flood, landslides, earthquakes, sinkholes, storms etc. \n" +
                "\n" +
                "7. CANCELLATION OF SERVICE\n\n" +
                "If a service is booked and then cancelled, generally, We do not refund your purchase. We will endeavor all reasonable care to ensure that Our Services conform to the standard services in Malaysia. Please refer to Cancellation and Refund Policy for further information\n" +
                "For any disputes please contact our Customer Service hotline number.\n" +
                "\n" +
                "8. RECONSIGNMENT OR DIVERSION\n\n" +
                "A request for diversion of transport will be subject to the following definitions, conditions and charges:\n" +
                "a.Request for diversion must be informed to Star Auto Assist Customer Service department. \n" +
                "b.Diversion en route shall be determined on the basis of the distance from origin to final destination via diversion point.\n" +
                "\n" +
                "9. PRICE AND PAYMENTS\n\n" +
                "9.1 Price\n\n" +
                "All prices listed on the Platform are in Malaysian Ringgit. The price of each Service as listed on the Platform is merely an indicative price and is subject to change due to other additional factors such as the distance and time it takes for Star Auto Assist Rangers to reach your exact geographical location or some other extraneous or unexpected situations that may increase the cost of delivery of the Services or the product itself. Star Auto Assist Rangers will inform you of the actual price and obtain your agreement before providing the Services to you.\n" +
                "9.2 Response Time\n\n" +
                "While we can show an estimated time of arrival of Rangers to your exact geographical location, it is merely an estimated time and shall not be taken as a binding commitment from Star Auto Assist or Rangers as the response time would depend on a myriad of factors such as traffic situation, road accident, distance from Star Auto Assist or Ranger’s office to your exact geographical location and other conditions on the road. In any event, Rangers will strive to reach your exact geographical location in the shortest possible time but will not be liable in the event Rangers reaches your location beyond the estimated time of arrival.\n" +
                "9.3 Charges Per Bill\n\n" +
                "Customers will pay for Services according to towing bill and not kilometre (KM) in areas such as Penang, Langkawi, Genting Highlands, Cameron Highlands and Fraser Hill and any other highlands. \n" +
                "9.4 Payment methods: \n\n" +
                "You may pay for the Services using bank direct transfer or credit card upon completion of each Service. \n" +
                "9.5 Taxes \n\n" +
                "If the Service is liable for taxes, you shall be responsible to pay for the taxes, in addition to the price for the Service.\n" +
                "9.6 No Refunds\n\n" +
                "Purchases are not refundable and we will not refund or credit for any Service rendered unless you provide credible evidence to us to prove that you have been wrongly billed or such other circumstances on a “case to case” basis as we may decide in our sole and absolute discretion.\n" +
                "9.7 Minimum Service Charge:\n\n" +
                "In the event Star Auto Assist’s Rangers, having been called to your location but is unable to provide Star Auto Assist’s Service, due to reasons beyond his control (for example, the vehicle battery is dead and there is no way to jump start it, or the spare tyre is flat and there is no way to pump it), you shall be responsible to pay a minimum service charge of RM50.\n" +
                "9.8 Vouchers \n\n" +
                "We may from time to time provide vouchers for our customers as part of our promotional campaigns. The vouchers are not exchangeable, convertible or redeemable for cash.\n" +
                "9.9 Promotional Offers or Discounts \n\n" +
                "We may make promotional offers or discounts with different features and different rates to our customers from time to time in our sole and absolute discretion and you agree that such promotional offers or discounts, unless also made available to you, shall have no bearing on your use of the Services.\n" +
                "9.10 Additional Terms \n\n" +
                "When using the Platform and/or Services, you will be subject to any additional guidelines or rules applicable to specific products, services or features which may be posted from time to time \n" +
                "\n" +
                "10. MEMBERSHIP CARDHOLDER OBLIGATIONS AND PROCEDURES\n\n" +
                "10.1 Requests For Assistance\n\n" +
                "For towing assistance, and prior to taking personal action, the Membership Cardholder or his/her representative shall call Star Auto Assist  twenty-four (24) hours Customer Service Hotline 1-800-88-0202 and furnish details such as:\n" +
                "a) His/her name, the vehicle registration number and NRIC number.\n" +
                "b) The name of the place and if possible the telephone number where Star Auto Assist can reach the Member or his/her representative.\n" +
                "c) A brief description of the emergency and the nature of the assistance required.\n" +
                "10.2 Co-Operation With Star Auto Assist\n\n" +
                "The Membership Cardholder shall cooperate with Star Auto Assist to get all original documents and receipts from relevant sources.\n" +
                "10.3 Limitation On Assistance Cases\n\n" +
                "Membership Cardholder is entitled for one (1) free towing service in Peninsula Malaysia. The benefit expires after the free towing service is redeemed and membership can only be renewed in the following year. \n" +
                "The following also apply\n" +
                "a.Cheating or Fraud: Star Auto Assist shall not be liable for the act of cheating or fraud by the Membership Cardholder in requesting vehicle assistance. If found guilty, the membership will be forfeited and penalty will be charged\n" +
                "b.Cancellation of Booking : If the Membership Cardholder cancel the service requested with no apparent reason, membership will be forfeited immediately with no refund\n" +
                "10.4 Special Request\n\n" +
                "Membership Cardholders who request for baby tyre, crane, forklift, additional toll trucks and any other kind of special requests from the norm will incur additional charges accordingly.\n" +
                "\n" +
                "11. GENERAL EXCLUSIONS\n\n" +
                "11.1 Customers’s Present\n\n" +
                "Customer must be at the site of incident for verification. Star Auto Assist reserves the right to deny the service if the Customer is not available prior to towing.\n" +
                "11.2 Star Auto Assist shall not be held responsible for the provision of the assistance services in respect of:\n\n" +
                "a) Any person who drives the said vehicle who does not hold a valid license issued by the relevant authority.\n" +
                "b) Vehicle modified for racing, trials or rallying or participating in such activities.\n" +
                "c) Any call for assistance made by the Membership Cardholder on behalf of his/her relative(s) or friends(s) which their vehicles are not listed as Star Auto Assist Membership Cardholder\n" +
                "d) Vehicle number that does not tally with the number registered with Star Auto Assist, meaning the service is only applicable to the registered vehicle\n" +
                "e) Any expenses directly paid by the Membership Cardholder to any third party without prior authorization from Star Auto Assist.\n" +
                "f) Any vehicle carrying more passengers or towing greater weight than for which it was designed as stated in the manufacturer’s specification or arising directly or indirectly from or out of the unreasonable driving of the vehicle on unsuitable terrain.\n" +
                "g) Any Membership Cardholder who have failed to take all reasonable precautions to maintain his/her vehicle in an efficient roadworthy condition.\n" +
                "h) Any vehicle left unattended for a period of a day (24 hours) or more which later culminate the problem of either the vehicle cannot be started due to weak battery, spoilt alternator, flat tyre or any other related cause.\n" +
                "i) Mechanical breakdown due to lack of engine oil, petrol, water and/or frost damage. Upon request from the Membership Cardholder, Star Auto Assist may provide assistance for such incidences subject to payment by Membership Cardholder of all actual costs involved.\n" +
                "j) Ignition key accidentally left and was locked from inside the said vehicle.\n" +
                "k) Change of flat tyre will be entertained only when Membership Cardholder keeps a good spare tyre that is roadworthy.\n" +
                "l) The Company shall not be liable for any vehicle assistance that is caused by or attributed to the act of cheating by any person within the meaning of the definition of the offence of cheating set out in the Penal Code. \n" +
                "o) Loss or damage directly or indirectly occasioned as a consequence of war, invasion, act of God, natural disasters,  act of foreign enemies, hostilities whether war be declared or not, civil war, rebellion, insurrection, terrorism, military and usurped power, riot or commotion.\n" +
                "p) Any Membership Cardholder found intoxicated with alcohol, drugs or any other substances during service.\n" +
                "\n" +
                "12. PASSING OF RISK\n\n" +
                "During Service is executed, any risk (including accidental loss, destruction or deterioration) (“Risk“) shall pass to you as soon as We have completed the Service. As part of Our value-added Services, We will assist in ensuring the highest level of care is administered but we cannot accept any liability arising therefrom.\n" +
                "\n" +
                "13. EXCLUSION OF LIABILITY\n\n" +
                "13.1 Except for any legal responsibility that we cannot exclude in law (such as for death or personal injury), we are not legally responsible for any losses that were: \n\n" +
                "a. not foreseeable to you and Star Auto Assist when this contract was formed; \n" +
                "b. not caused by any breach on Star Auto Assist’s part, and \n" +
                "c.  indirect, exemplary, incidental or consequential loss (including loss of profit and loss of data), arising out of or in connection with the Platform and/or Services.\n" +
                "13.2 To the extent our liability cannot be excluded but can be limited, our liability to you under or in connection with these Terms, or in connection with the Platform and/or Services, or your access and use of (or inability to access or use) the Platform and/or Services, shall not exceed RM 300.\n\n" +
                "13.3 We have no liability to you for any breach of these terms caused by any event or circumstance beyond our reasonable control including, without limitation, strikes, lock-outs or other industrial disputes; breakdown of systems or network access; or flood, fire, explosion or accident.\n\n" +
                "\n" +
                "14. INTELLECTUAL PROPERTY\n\n" +
                "14.1 We are the owner (or the licensee, where applicable) of all proprietary and intellectual property rights on the Platform (including all information, data, texts, graphics, visual interfaces, artworks, photographs, logos, icons, sound recordings, videos, look and feel, software programmes, computer code, downloadable files, software applications, interactive features, tools, services) or other information or content made available on or through the Platform.\n\n" +
                "14.2 We grant you, subject to these Terms, a non-exclusive, non-transferable, non-assignable, personal, limited license to access and use the Platform for your own personal and non-commercial use. All rights not expressly granted to you are reserved by us.\n\n" +
                "\n" +
                "15. DISCLAIMERS\n\n" +
                "15.1 The information and materials on the Platform are provided to you for information purposes only and on an “as is” and “as available” basis without representations, warranties or guarantees of any kind either express or implied.\n\n" +
                "15.2 Whilst we endeavor to make the Platform (not the Services) available 24 hours a day, we shall not be liable if for any reason the Platform is unavailable for any time or for any period. We make no representation, warranty or guarantee that your access to the Platform will be uninterrupted, timely or error-free. Due to the nature of the internet, this cannot be guaranteed. In addition, we may occasionally need to carry out repairs, maintenance or introduce new facilities and functions.\n\n" +
                "15.3 We warrant that the Services will be performed consistent with generally accepted industry standards. No specific result from provision of the Services is assured or guaranteed. Other than those expressly set forth herein, to the extent permitted by law, we hereby disclaim all warranties, express or implied, statutory or otherwise, including but not limited to implied warranties and the warranties of merchantability, fitness for a particular purpose, in respect of the Platform and/or Services.\n\n" +
                "15.4 Product warranty from the vendor or manufacturer: We will transfer to you any product warranties and indemnities authorized by the vendor or manufacturer. We make no warranties of any kind concerning the product or any related documentation or services provided by the vendor, manufacturer or otherwise.\n\n" +
                "15.5 Alternative product: In the event that the product advertised on the Platform is out of stock, we may recommend to you an alternative product as a replacement, the quality of which will be verified by our technician and informed to you before installation.\n\n" +
                "15.6 Not responsible for Tow Truck Operator: We are not responsible nor liable for any direct, indirect, economic, exemplary, incidental or consequential loss (including loss of profit and loss of data), damage, cost or expense that you may suffer or incur as a result of or in connection with the acts, omissions and/or negligence of any Tow Truck Operator who provides the Outsourced Services to you. We make no warranties of the quality, suitability, safety or ability of the Outsourced Services.\n\n" +
                "15.7 Not a contracting party between yourself Tow Truck Operator: and We are not a party to any agreement, dealing or transaction entered into between you and the Tow Truck Operator, whether as a result, directly or indirectly, from using the Outsourced Services and we disclaim any and all responsibilities and/or liabilities arising from such agreement between you and the Tow Truck Operator.\n\n" +
                "\n" +
                "16. LINKS TO OTHER WEBSITES\n\n" +
                "Links included within the Platform may let you leave the Platform and enter into other website(s) (\"Linked Site(s)\"). The Linked Sites are not under the control of Star Auto Assist and Star Auto Assist is not responsible nor shall it be liable for the contents of any Linked Sites or any links contained in a Linked Site or any changes or updates to such sites. You agree that your access to and/or use of such Linked Site is entirely at your own risk and subject to the terms and conditions of access and/or use contained therein.\n" +
                "\n" +
                "17. THIRD PARTY ADVERTISING\n\n" +
                "We may allow third party advertisers to place advertisements on the Platform or any part thereof. Such advertisements will be clearly identified as originating from third parties. By using the Platform and/or Services, you agree to receive such advertising and marketing materials. If you do not want to receive such advertising and marketing materials you should notify us in writing. We do not endorse, and will not be responsible for, the contents of such advertisements or for your access, use, reliance, sale, purchase, or other action on your part with respect to the contents or subject matter of such advertisements.\n" +
                "\n" +
                "18. GENERAL\n\n" +
                "18.1 If we need to contact you, we may do so by email or by posting a notice on the Platform. Notice will be deemed given 24 hours after email is sent to your designated email address or notice is posted on the Platform. You agree that this satisfies all legal requirements in relation to written communications.\n\n" +
                "18.2 These Terms and our Privacy Policy constitute the entire agreement between you and Star Auto Assist and supersedes any prior agreements, arrangements, statements and understandings between Star Auto Assist and you.\n\n" +
                "18.3 A waiver by Star Auto Assist of any breach by you of any of the provisions of these Terms or the acquiescence of Star Auto Assist to any act (whether of commission or omission) which but for such acquiescence would be a breach as aforesaid shall not constitute a general waiver of such provision or of any subsequent act contrary thereto.\n\n" +
                "18.4 These Terms shall be governed by the laws of Malaysia and each party agrees to submit to the non-exclusive jurisdiction of the Courts of Malaysia.\n\n" +
                "18.5 For us to waive a right under these Terms, the waiver must be in writing.\n\n" +
                "18.6 If any part or provision of these Terms is or becomes illegal, unenforceable, or invalid under any enactment or rule of law or by any court in any jurisdiction, that part or provision is deemed to be modified to the extent required to remedy the illegality, unenforceability or invalidity. If a modification is not possible, the part or provision must be treated for all purposes as severed from these Terms. The remainder of these Terms will remain in full force and effect and continue to be binding and enforceable on you.\n\n" +
                "\n" +
                "19. INDEMNITY AND LIMITATION OF LIABILITY\n\n" +
                "You undertake the Services at your own risk and agrees to indemnify Star Auto Assist and its employees against all costs, losses, damages, expenses and liabilities (including for loss of reputation and goodwill and professional advisors fees) and any claim arising from your own actions in any way in connection with the Services, or a breach of your obligations as set forth in these Terms and Conditions.\n" +
                "Star Auto Assist is not liable in any way for any costs, expenses, damages, liability or injury arising out of or in any way connected with the Services. This limitation does not exclude any liability for negligence by Star Auto Assist or death or personal injury arising out of such negligence.\n" +
                "\n" +
                "20. SEVERABILITY\n\n" +
                "The invalidity, illegality, or unenforceability of the whole or part of these Terms and Conditions do not affect or impair the continuation in force of the remainder of the Terms and Conditions.\n" +
                "\n" +
                "21. ENTIRE AGREEMENT\n\n" +
                "These Terms and Conditions represent the entire agreement between the parties relating to the Services (unless otherwise expressly confirmed in writing by Star Auto Assist) and supersede all prior representations, agreements, negotiations or understandings (whether oral or in writing). Except as specifically set out herein, all conditions, warranties and representations expressed or implied by law are excluded. For the avoidance of doubt, no information of any nature about the Services or any of these Terms and Conditions should be relied upon unless confirmed in writing by Star Auto Assist\n" +
                "\n" +
                "22. NO WAIVER\n\n" +
                "The failure to exercise or delay in exercising a right or remedy provided hereunder or by law does not constitute a waiver of the right or remedy or waiver of other rights or remedies.\n" +
                "\n" +
                "23. GOVERNING LAW\n\n" +
                "These Terms and Conditions shall be governed by and interpreted in accordance with Malaysian law and the parties irrevocably submit to the exclusive jurisdiction of the Malaysian courts.\n" +
                "\n" +
                "24. SPECIAL TERMS\n\n" +
                "These Terms and Conditions are not intended to nor shall create any rights, entitlements, claims or benefits enforceable by any person that is not a party to them. No person shall derive any benefit or have any right, entitlement or claim in relation to this Terms and Conditions.\n" +
                "\n" +
                "25. CHANGES TO THE TERMS AND CONDITIONS\n\n" +
                "We reserve the right to update these Terms and Conditions at any time and any changes may be made without notice to you. We recommend you to read this Terms and Conditions periodically.\n" +
                "By continuing to access or use Our Services after those revisions or changes become effective, you agree to be bound by the revised terms. If you do not agree to the new Terms and Conditions, please stop using the Website and/or Our Services.\n" +
                "All of our rights are reserved. \n";

        myText += "</body></html>";

        myText = myText.replace("\n", "<br>");
        webView.loadData(String.format(htmlText, myText), "text/html", "utf-8");


    }
}
