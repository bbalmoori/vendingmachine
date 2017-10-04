package com.tesco.vendingmachine.model;

import com.tesco.vendingmachine.exception.UnknownCoinException;
import com.tesco.vendingmachine.model.Coin;

public enum Coin {
	TEN_PENCE(10), TWENTY_PENCE(20), FIFTY_PENCE(50), ONE_POUND(100);
	private int pence;

	Coin(int pence) {
		this.pence = pence;
	}

	public int getDenomination() {
		return pence;
	}

	public static Coin fromPence(int pence) throws UnknownCoinException {
		for (Coin c : Coin.values()) {
			if (c.pence == pence) {
				return c;
			}
		}
		throw new UnknownCoinException("Invalid Coin inserted");
	}
}