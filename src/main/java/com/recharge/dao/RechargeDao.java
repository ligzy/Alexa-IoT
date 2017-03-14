package com.recharge.dao;

public class RechargeDao {

	private String accessToken = "MRSzCOuAKlk0dorvHOjevIOlCb1yI94C4fv4ZwxbgMzbipvB0sNehIe0LAQh";
	private int clientId = 5540;
	private String number;
	private int providerId;
	private int rechargeAmount;

	public String getAccessToken() {
		return accessToken;
	}

	public int getClientId() {
		return clientId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public int getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(int rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
}
