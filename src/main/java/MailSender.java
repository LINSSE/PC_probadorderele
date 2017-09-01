import java.io.IOException;

import javax.mail.MessagingException;

public class MailSender implements iObserver {
	
	private GoogleServices gmail;
	
	public MailSender(GoogleServices googleApi) {
		// TODO Auto-generated constructor stub
		this.gmail = googleApi;
	}

	@Override
	public void update(){
		// TODO Auto-generated method stub
		
			try {
				this.gmail.sendMail();
			} catch (MessagingException e)
			{
				
			}
			catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("!!Mail sender");
		System.out.println("!!Mail Enviado");
	}
}
