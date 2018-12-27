package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.BloodPressure;

public class GetBloodPressureIntentHandler implements RequestHandler{

	
	public static String getDate() {
		String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}
	
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName("GetBloodPressureIntent"));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		
		
		String speechText = "";
		
		final String UserMail=i.getServiceClientFactory().getUpsService().getProfileEmail().replace(".", "_dot_");
		DatabaseReference dbRef = null;
		try {
			FileInputStream serviceAccount;
			FirebaseOptions options = null;
			try {
				serviceAccount = new FileInputStream("db_credentials.json");
				options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://fitnesspeaker-6eee9.firebaseio.com/").build();
				FirebaseApp.initializeApp(options);
			} catch (final Exception e1) {
				speechText += e1.getMessage() + " ";// its ok
			}
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			if (database != null)
				dbRef = database.getReference().child(UserMail).child("Dates").child(getDate()).child("BloodPressure");
		} catch (final Exception e) {
			speechText += e.getMessage() + " ";// its ok
		}


		final List<BloodPressure> logListDB = new LinkedList<>();
		final CountDownLatch done = new CountDownLatch(1);
		dbRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot s) {
				for (final DataSnapshot bloodPressureSnapshot : s.getChildren())
					logListDB.add(bloodPressureSnapshot.getValue(BloodPressure.class));
				done.countDown();
			}

			@Override
			public void onCancelled(final DatabaseError e) {
				System.out.println("The read failed: " + e.getCode());
			}
		});
		try {
			done.await();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
		}
		
		speechText="";
		if(logListDB.isEmpty()) speechText="you did not log any blood pressure measure today";
		else
			for (BloodPressure log : logListDB)
				speechText += log.toString() + ", ";
		
		
		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withReprompt(speechText).withShouldEndSession(Boolean.FALSE).build();
	}

}
