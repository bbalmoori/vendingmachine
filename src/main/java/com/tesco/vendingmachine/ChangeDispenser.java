package com.tesco.vendingmachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tesco.vendingmachine.exception.NotEnoughChangeException;
import com.tesco.vendingmachine.model.Coin;

public class ChangeDispenser {

	public List<Coin> dispenseChange(int amount, Map<Coin, Integer> coinStore) throws Exception {
		if (amount == 0) {
			return Collections.emptyList();
		}

		Map<Coin, Integer> coinStoreCopy = new HashMap<>();
		
		coinStoreCopy.forEach(coinStore::put);
		
		List<Coin> change = new ArrayList<>();
		while (amount > 0) {
			if (isCoinStoreContainsDenomination(amount, Coin.ONE_POUND, coinStore)) {
				accumulateChange(Coin.ONE_POUND, change, coinStore);
				amount = amount - Coin.ONE_POUND.getDenomination();
			} else if (isCoinStoreContainsDenomination(amount, Coin.FIFTY_PENCE, coinStore)) {
				accumulateChange(Coin.FIFTY_PENCE, change, coinStore);
				amount = amount - Coin.FIFTY_PENCE.getDenomination();
			} else if (isCoinStoreContainsDenomination(amount, Coin.TWENTY_PENCE, coinStore)) {
				accumulateChange(Coin.TWENTY_PENCE, change, coinStore);
				amount = amount - Coin.TWENTY_PENCE.getDenomination();
			} else if (isCoinStoreContainsDenomination(amount, Coin.TEN_PENCE, coinStore)) {
				accumulateChange(Coin.TEN_PENCE, change, coinStore);
				amount = amount - Coin.TEN_PENCE.getDenomination();
			} else {
				coinStore.clear();
				coinStoreCopy.forEach(coinStore::put);
				throw new NotEnoughChangeException("Not enough change to dispense");
			}
		}
		return change;
	}

	private boolean isCoinStoreContainsDenomination(int amount, Coin coin, Map<Coin, Integer> coinStore) {
		return coinStore.containsKey(coin) && coinStore.get(coin) > 0 && amount >= coin.getDenomination();
	}

	private void accumulateChange(Coin coin, List<Coin> change, Map<Coin, Integer> coinStore) {
		coinStore.compute(coin, (p, q) -> Integer.valueOf(q - 1));
		change.add(coin);
	}

	
}
