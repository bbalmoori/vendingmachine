package com.tesco.vendingmachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.tesco.vendingmachine.exception.AlreadyOffException;
import com.tesco.vendingmachine.exception.AlreadyOnException;
import com.tesco.vendingmachine.exception.ItemNotAvailableException;
import com.tesco.vendingmachine.exception.ItemNotSelectedException;
import com.tesco.vendingmachine.exception.MachineOffException;
import com.tesco.vendingmachine.exception.NotInsertedCoinsException;
import com.tesco.vendingmachine.exception.UnknownCoinException;
import com.tesco.vendingmachine.model.Coin;
import com.tesco.vendingmachine.model.Item;
import com.tesco.vendingmachine.model.ItemAndChange;
import com.tesco.vendingmachine.model.ItemAndChange.ItemAndChangeBuilder;

/**
 * Encapsulates the state of a vending machine and the operations that can be
 * performed on it
 */
public class VendingMachine {

	public volatile boolean isOn;

	private Map<Coin, Integer> coinStore = new HashMap<>();

	private Map<Item, Integer> itemStore = new HashMap<>();

	private ChangeDispenser changeDispenser = new ChangeDispenser();

	private int insertedMoney = 0;
	private int cashInventory = 0;
	private Item selectedItem = null;

	public boolean isOn() {
		return isOn;
	}

	public void setOn() throws AlreadyOnException {
		if (!isOn) {
			initMachine();
			isOn = true;
		} else {
			throw new AlreadyOnException("The machine is off already. It can not be turned on twice");
		}
	}

	public void setOff() throws AlreadyOffException {
		if (!isOn) {
			isOn = false;
		} else {
			throw new AlreadyOffException("The machine is off already");
		}
	}

	public void insertMoney(int pence) throws UnknownCoinException, MachineOffException {
		checkMachineIsOn();
		Coin insertedCoin = Coin.fromPence(pence);
		insertedMoney += insertedCoin.getDenomination();
		cashInventory += insertedCoin.getDenomination();
		coinStore.compute(insertedCoin, (p, q) -> q == null ? 1 : Integer.valueOf(q + 1));
	}

	public void selectItem(Item item) throws Exception {
		checkMachineIsOn();
		this.selectedItem = item;
		checkSelctedItem();
	}
	
	public List<Coin> coinReturn() throws Exception {
		checkMachineIsOn();
		if (insertedMoney == 0) {
			throw new NotInsertedCoinsException("Nothing to return!!");
		}
		List<Coin> change = changeDispenser.dispenseChange(insertedMoney, coinStore);
		cashInventory -= change.stream().mapToInt(coin -> coin.getDenomination()).sum();
		return change;
	}

	public ItemAndChange getItemAndChange() throws Exception {
		checkMachineIsOn();
		ItemAndChangeBuilder builder = ItemAndChange.builder();
		if (insertedMoney >= selectedItem.getPriceInPence()) {
			builder.item(selectedItem);
		}
		return builder.change(checkInsertedCoinsAndGetItem()).build();

	}

	public Map<Coin, Integer> getCoinStore() {
		return Collections.unmodifiableMap(coinStore);
	}

	public Map<Item, Integer> getItemStore() {
		return Collections.unmodifiableMap(itemStore);
	}

	private void checkMachineIsOn() throws MachineOffException {
		if (!isOn) {
			throw new MachineOffException("Machine is under maintenance, please comeback later!!");
		}
	}

	private void initMachine() {
		Arrays.stream(Coin.values()).forEach(coin -> {
			coinStore.put(coin, 10);
			cashInventory += coin.getDenomination();
		});
		Arrays.stream(Item.values()).forEach(x -> itemStore.put(x, 10));
	}

	private List<Coin> checkInsertedCoinsAndGetItem() throws Exception {
		if (insertedMoney >= selectedItem.getPriceInPence()) {
			List<Coin> change = changeDispenser.dispenseChange(insertedMoney - selectedItem.getPriceInPence(),
					coinStore);
			insertedMoney = 0;
			itemStore.compute(selectedItem, (p, q) -> Integer.valueOf(q - 1));
			return change;
		} else if (insertedMoney > 0) {
			return changeDispenser.dispenseChange(insertedMoney, coinStore);
		}
		return Collections.emptyList();
	}

	private void checkSelctedItem() throws Exception {
		if (Objects.isNull(selectedItem)) {
			throw new ItemNotSelectedException("Please select an Item first");
		}

		if (itemStore.get(selectedItem) == 0) {
			selectedItem = null;
			throw new ItemNotAvailableException("Selected item has run out, please change the selection");
		}
	}
}
