
import java.util.ArrayList;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


public class SmsSender implements iObserver{
	// Find your Account Sid and Token at twilio.com/user/account
	  public static final String ACCOUNT_SID = System.getenv("TWILIO_ACOUNT_SID");
	  public static final String AUTH_TOKEN = System.getenv("TWILIO_KEY");
	@Override
	public void update() {
		
		 Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		 ArrayList<String> to = new ArrayList<>();
		 to.add("+543794688483");//Ivan
		 //to.add("+543795013243");//Azul
		 //to.add("+5493794735197");//Emanuel
		 //to.add("+541158443749");//Ariel
		// to.add("+542916434357");//Adrian
		  String body = "Probador de Reles: Anomalia MAX detectada en CIAA Con1=45";
		  
		  for(String number: to)
		  {
		  Message message = Message.creator(  new PhoneNumber(number) , new PhoneNumber(System.getenv("TWILIO_NUMBER")),body).create();
			    System.out.println(message.getSid());  
		  }
		    
		  
		
		// TODO Auto-generated method stub
		System.out.println("!!Sms Sender");
		//System.out.println("!!SMS ENVIADO A "+to);
	}
}
