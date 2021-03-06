package com.salesmanapp.helper;



import java.util.regex.Pattern;

public class AllKeys {
	public static  final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


    public static final boolean checkEmail(String email) {
		System.out.println("Email Validation:==>" + email);
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}



	public static final String KEYWORD_CLIENT = "ANDCLIENT";
	public static final String KEYWORD_FOLLOWUP = "ANDFOLLOWUP";
	public static final String KEYWORD_ORDER = "ANDORDER";

	public static final String WEBSITE = "http://crm.tech9teen.com/Service.asmx/";
	//public static final String WEBSITE = "http://arham.dnsitexperts.com/yelona/index.php/welcome/";//http://demo1.dnsitexperts.com/
	//public static final String WEBSITE = "http://19designs.org/yelona/index.php/welcome/";//http://demo1.dnsitexperts.com/


	public static final String RESOURSES="https://www.shahagenciesindia.com/";
	public static final Integer MY_SOCKET_TIMEOUT_MS=30000;


	//  //UploadDocument Realted KEYS
	public static final String TAG_UPLOAD_IMAGES_ARRAY = "Data";
	public static final String TAG_UPLOAD_IMAGE_NAME = "IMAGE_NAME";
	public static final String TAG_UPLOAD_IMAGEURL = "visitingcardfronturl";




	//Common Keys
	public static final String TAG_MESSAGE = "MESSAGE";
	public static final String TAG_ERROR_ORIGINAL = "ORIGINAL_ERROR";
	public static final String TAG_ERROR_STATUS = "ERROR_STATUS";
	public static final String TAG_IS_RECORDS = "RECORDS";

	//GetJSONForCategoryData Related Keys
	public static final String ARRAY_CATEGORY="categorydata";
	//public static final String TAG_CATEGORYID="id";
	public static final String TAG_CATEGORY_NAME="category";
	public static final String TAG_CATEGORY_IMAGE="img";
	//public static final String TAG_PARENTID="p_id";

	public static final String TAG_TOTAL_SUB_CATEGORIES="TotalSubCategory";



	//LoginData Related keys
	public static final String ARRAY_LOGINDATA="Data";
	public static final String TAG_EMPID="empid";
	public static final String TAG_EMP_NAME="empname";
	public static final String TAG_MOBILENO="empmobile";
	public static final String TAG_EMP_EMAIL = "empemail";
	public static final String TAG_EMP_TYPE = "type";
	public static final String TAG_EMP_UNIQUE_CODE = "uniquecode";

	//ViewClientMst Related keys
	public static final String TAG_CLIENTID = "clientid";
	public static final String TAG_DEVICETYPE = "devicetype";
	public static final String TAG_COMPANYNAME = "companyname";
	public static final String TAG_MOBILE1 = "mobile1";
	public static final String TAG_MOBILE2 = "mobile2";
	public static final String TAG_LANDLINE = "landline";
	public static final String TAG_EMAIL = "email";
	public static final String TAG_CLIENT_WEBSITE="website";
	public static final String TAG_BUSINESS = "business";
	public static final String TAG_ADDRESS = "address";
	public static final String TAG_CONTACT_PERSON_NAME = "contactpersonname";
	public static final String TAG_VISITING_CARD_FRONT = "visitingcardfront";
	public static final String TAG_VISITING_CARD_BACK = "visitingcardback";
	//public static final String TAG_EMPLOYEEID_ = "empid";
	public static final String TAG_CREATED_DATE = "createddate";
	public static final String TAG_LATTITUDE = "latitude";
	public static final String TAG_LONGTIUDE = "longitude";
	public static final String TAG_CLIENT_TYPE = "clienttype";
	public static final String TAG_NOTE = "note";

	public static final String TAG_DATE = "date";
	public static final String TAG_TIME = "time";
	public static final String TAG_DESCRIPTION="description";
	public static final String TAG_FOLLOWUPID="followupid";
	public static final String TAG_FOLLOWUP_STATUS="status";
	public static final String TAG_FOLLOWUP_REASON="reason";





	public static final String TAG_SERVICEID="serviceid";
	public static final String TAG_SERVICENAME="servicename";

	//ViewServiceMaster Related  Keys
	public static final String TAG_ORDERID="orderid";
	//public static final String TAG_SERVICEID="serviceid";
	public static final String TAG_QUIANTITY="qty";
	public static final String TAG_RATE="rate";
	public static final String TAG_DISCOUNT_AMT="discountamt";
	public static final String TAG_NET_AMT="netamt";
	public static final String TAG_CLIENT_ID="clientid";
	//public static final String TAG_EMPID="empid";
	//public static final String TAG_DEVICETYPE="devicetype";
	public static final String TAG_ORDER_DESCR="orderdescr";


	public static final int DEAFULT= 0;
	public static final int YES = 1;
	public static final int CANCEL = 2;
	public static final int SCHEDULE =3;
























































}
