/**add here document
 * @author Fname Sname
 * @since year-month-day*/
package Utils;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Utils.Portion.Type;

/**
 * @author Shaked Sapir
 *
 */
@SuppressWarnings({"static-method","unlikely-arg-type"}) public class PortionTest {

	double DELTA = 0.01;
	@Test
	public void testConstr() {
		Portion bacon = new Portion(Type.FOOD,"bacon",100,700,13,302,30.3);
		assertEquals(Type.FOOD, bacon.getType());
		assertEquals("bacon",bacon.getName());
		assertEquals(100,bacon.getAmount(),DELTA);
		assertEquals(700,bacon.getCalories_per_100_grams(),DELTA);
		assertEquals(13,bacon.getProteins_per_100_grams(),DELTA);
		assertEquals(302,bacon.getCarbs_per_100_grams(),DELTA);
		assertEquals(30.3,bacon.getFats_per_100_grams(),DELTA);
	}
	
	@Test
	public void testSetters() {
		Portion bacon = new Portion(Type.FOOD,"bacon",100,700,13,302,30.3);
		bacon.setAmount(200);
		bacon.setCalories_per_100_grams(200);
		bacon.setCarbs_per_100_grams(200);
		bacon.setFats_per_100_grams(200);
		bacon.setProteins_per_100_grams(200);
		
		assertEquals(Type.FOOD, bacon.getType());
		assertEquals("bacon",bacon.getName());
		assertEquals(200,bacon.getAmount(),DELTA);
		assertEquals(200,bacon.getCalories_per_100_grams(),DELTA);
		assertEquals(200,bacon.getProteins_per_100_grams(),DELTA);
		assertEquals(200,bacon.getCarbs_per_100_grams(),DELTA);
		assertEquals(200,bacon.getFats_per_100_grams(),DELTA);
	}
	
	@Test
	public void testToString() {
		assertEquals(
				"Portion name: bacon , 100.0 grams\nPortion type: FOOD\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 700.0\nProteins: 13.0\n"
						+ "Carbohydrates: 302.0\nFats: 30.3",
				(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3)).toString());
		
	}
	
	@Test
	public void testToString2() {
		assertEquals(
				"Portion name: fuzetea , 100.0 ml\nPortion type: DRINK\n----------------------------------\n"
						+ "Nutritional Values per 100 grams:\nCalories: 700.0\nProteins: 13.0\n"
						+ "Carbohydrates: 302.0\nFats: 30.3",
				(new Portion(Type.DRINK, "fuzetea", 100, 700, 13, 302, 30.3)).toString());
		
	}
	
	@Test
	public void testEquals() {
		assert (new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3)));
		assert !(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.FOOD, "bacon", 100, 700, 13, 301, 30.3)));
		assert !(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.1)));
		assert !(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.FOOD, "bacon", 100, 700, 14, 302, 30.3)));
		assert !(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.FOOD, "bacon", 100, 701, 13, 302, 30.3)));
		assert !(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.FOOD, "bacon", 101, 700, 13, 302, 30.3)));
		assert !(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.FOOD, "baconn", 100, 700, 13, 302, 30.3)));
		assert !(new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3).equals(new Portion(Type.DRINK, "bacon", 100, 700, 13, 302, 30.3)));
		Portion a = new Portion(Type.FOOD, "bacon", 100, 700, 13, 302, 30.3);
		assert (a.equals(a));
		assert !(a.equals(null));
		assert !(a.equals(String.valueOf("b")));
	}
	
}