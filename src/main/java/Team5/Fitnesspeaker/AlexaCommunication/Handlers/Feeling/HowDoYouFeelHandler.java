package Team5.Fitnesspeaker.AlexaCommunication.Handlers.Feeling;
import static com.amazon.ask.request.Predicates.intentName;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Utils.Portion.Portion;
import Utils.Portion.Portion.Type;
import Utils.Strings;
import Utils.DB.DBUtils;
import Utils.DB.DBUtils.DBException;
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;
import Utils.Strings.SlotString;

public class HowDoYouFeelHandler implements RequestHandler {
	
	public static String getDate() {
		final String[] splited = Calendar.getInstance().getTime().toString().split("\\s+");
		return splited[2] + "-" + splited[1] + "-" + splited[5];
	}


	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.FEELING_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		final Slot feelingSlot = ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.FEELING_SLOT);
		String speechText = "You didn't tell me how you feel";

		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		List<Portion> FoodList = new LinkedList<>();
		List<String> KeyList = new LinkedList<>();

		// retrieving the information
		try {
			FoodList = db.DBGetTodayFoodList().stream().map(p -> p.getValue()).filter(p -> (p.getType() == Type.FOOD || p.getType() == Type.MEAL))
					.collect(Collectors.toList());
			KeyList = db.DBGetTodayFoodList().stream().map(p -> p.getName()).collect(Collectors.toList());
			
		} catch (final DBException e) {
			// no need to do anything
		}

		if (!FoodList.isEmpty()) {
		//	Iterator<Portion> it = FoodList.iterator();
		//	Portion p = FoodList.get(0);
		//	while (it.hasNext()) {
		//		p = it.next();
		//	}
			
			Portion p = FoodList.get(FoodList.size() - 1);
			String name = KeyList.get(KeyList.size() - 1);
		    DatabaseReference databaseReferenceC = FirebaseDatabase.getInstance().getReference().child(i.getServiceClientFactory().getUpsService().getProfileEmail()).child("Dates").child(getDate())
					.child("Food").child(name);
		   // databaseReferenceC.removeValueAsync();
			p.setFeeling(feelingSlot.getValue());
			try {
				
				db.DBPushFood(p);
			    databaseReferenceC.removeValue(null);

				// TODO: Delete the previous data from the database!
				
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			speechText = String.format("You logged that you feel " + feelingSlot.getValue());
		}

		return i.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.FALSE).build();
	}

}
