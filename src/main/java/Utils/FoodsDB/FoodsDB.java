package Utils.FoodsDB;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Utils.HttpSender.HttpSender;


public class FoodsDB {

	private final String credPath="./foods_db_creds.json";
	private final String url;
	private final  JSONParser jParser;
	
	
	private final String ateAsJson ="{\r\n" + 
			"  \"food_name\": \"%s\",\r\n" + 
			"  \"email\": \"%s\",\r\n" + 
			"  \"amount\": %d\r\n" + 
			"}";
	
	
	public FoodsDB() throws FoodsDBException{
		 
		 // reading url from the credential file
		 jParser = new JSONParser();
		 JSONObject creds;
		 try {
			creds = ( JSONObject) jParser.parse(new FileReader(credPath));
			url =  (String) creds.get("url");
		} catch (Exception e) {
			throw new CantReadCredsExc(this.credPath);
		} 
		
	 }
	 
	public  void UserAte(String email, String food, int amount ) throws FoodsDBException {
		 String res= new HttpSender()
					.setMethod2POST()
					.setUrl(url+"/ate")
					.setJsonBody(String.format(ateAsJson, food,email,amount))
					.send();
		 
		 // empty response means error when sending the request
		 if(res=="")
			 throw new CantSendRequestExc();
		 
		 JSONObject response;
		 String result;
		 try {
			 response = ( JSONObject) jParser.parse(res);
				result =  (String) response.get("result");
			} catch (Exception e) {
				throw new CantReadResponseExc();
			}
		 
		  if(!result.equals("OK"))
			  throw new databaseExc();
	 }
	 
	 
	 
	 
	 public JSONObject  NutVal4T4Today(String email) throws FoodsDBException {
		 String res= new HttpSender()
					.setMethod2GET()
					.setUrl(url+"/atetoday?email="+email)
					.send();
		 
	
		 if(res=="")
			 throw new CantSendRequestExc();
		 
		 JSONObject response=null;
		 try {
			 response = ( JSONObject) jParser.parse(res);
			} catch (Exception e) {
				throw new CantReadResponseExc();
			}
		 return response;
	 }
	
}