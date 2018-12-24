package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoRequest {

	public static String GetUserMail(final String accessToken) {
		final String url = "https://api.amazon.com/user/profile?access_token=" + accessToken;
		JSONObject json;
		try {
			json = readJsonFromUrl(url);
		} catch (IOException | JSONException e) {
			return null;
		}
		return json.getString("email");
	}

	public static String GetUserName(final String accessToken) {
		final String url = "https://api.amazon.com/user/profile?access_token=" + accessToken;
		JSONObject json;
		try {
			json = readJsonFromUrl(url);
		} catch (IOException | JSONException e) {
			return null;
		}
		return json.getString("name");
	}

	private static String readAll(final Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1)
			sb.append((char) cp);
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(final String url) throws IOException, JSONException {
		final InputStream is = new URL(url).openStream();
		try {
			@SuppressWarnings("resource")
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			final String jsonText = readAll(rd);
			final JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

}