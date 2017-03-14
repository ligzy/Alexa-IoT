package com.recharge.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.recharge.dao.RechargeDao;

public class HttpRechargeServices {

	private static final Logger log = LoggerFactory
			.getLogger(HttpRechargeServices.class);

	// Recharge - HTTP GET request
	public void doRecharge(RechargeDao dao) throws Exception {

		String url = Constants.KEY_BASE_URL + "paynow?api_token=" + dao.getAccessToken() + "&number=" + dao.getNumber()
				+ "&provider_id=" + dao.getProviderId() + "&amount=" + dao.getRechargeAmount() + "&client_id=" + dao.getClientId();

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();		
	}

	public int getProviderCode(RechargeDao dao) throws Exception {

		String url = Constants.KEY_BASE_URL + "get-number?api_token=" + dao.getAccessToken() + "&number=" + dao.getNumber();

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONObject jo = (JSONObject) new JSONTokener((response.toString())).nextValue();
		return Integer.parseInt(jo.getString("provider_id"));
	}
}