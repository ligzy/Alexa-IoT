package com.recharge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.recharge.dao.RechargeDao;
import com.recharge.util.Constants;
import com.recharge.util.HttpRechargeServices;

public class RechargeSpeechlet implements Speechlet {

	
	private static final Logger log = LoggerFactory.getLogger(RechargeSpeechlet.class);

	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}",	request.getRequestId(), session.getSessionId());		
	}

	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		String vmsg = Constants.KEY_WELCOME;
		return getAskSpeechletResponse(vmsg, vmsg);
	}

	public SpeechletResponse onIntent(final IntentRequest request,
			final Session session) throws SpeechletException {

		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if (Constants.KEY_HELLOWORLD_INTENT.equals(intentName)) {
			return getAskSpeechletResponse(Constants.KEY_MOBILE_NO_QUERY, Constants.KEY_MOBILE_NO_QUERY);
		} else if (Constants.KEY_ADDNUMBER_INTENT.equals(intent.getName())) {
			String mobileNumber = intent.getSlot("PlayerName").getValue();
			session.setAttribute("MOBILENO", mobileNumber);
			String speech = "Are you sure you want to recharge Rupees 10 for Mobile Number " + mobileNumber.replaceAll("\\B", " ");
			return getAskSpeechletResponse(speech, speech);
		} else if (Constants.KEY_CONFIRMATION_INTENT.equals(intent.getName())) {
			String confirm = intent.getSlot("Confirmation").getValue();
			if (confirm.equalsIgnoreCase("Yes")) {
				return doRecharge((String) session.getAttribute("MOBILENO"));
			} else {
				return exit("Thank You");
			}
		} else {
			throw new SpeechletException("Invalid Intent");
		}
	}

	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		
	}

	private SpeechletResponse getAskSpeechletResponse(String speechText, String repromptText) {

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("HelloWorld");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	
	private SpeechletResponse doRecharge(String mobileNumber) {

		RechargeDao dao = new RechargeDao();
		mobileNumber = mobileNumber.trim();
		String speechText = "Transaction Successful for " + mobileNumber.replaceAll("\\B", " ");

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("HelloWorld");
		// card.setContent(speechText);
		HttpRechargeServices http = new HttpRechargeServices();
		// Do Recharge
		try {
			dao.setNumber(mobileNumber);
			dao.setRechargeAmount(10);
			dao.setProviderId(http.getProviderCode(dao));
			HttpRechargeServices ex = new HttpRechargeServices();
			ex.doRecharge(dao);
		} catch (Exception e) {			
			speechText = "Transaction Failed";
		}
		
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return SpeechletResponse.newTellResponse(speech, card);
	}

	private SpeechletResponse exit(String speechText) {

		SimpleCard card = new SimpleCard();
		card.setTitle("Session");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
		repromptSpeech.setText(speechText);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptSpeech);

		return SpeechletResponse.newTellResponse(speech, card);
	}
}
