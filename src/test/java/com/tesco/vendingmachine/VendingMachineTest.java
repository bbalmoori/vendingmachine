package com.tesco.vendingmachine;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tesco.vendingmachine.VendingMachine;
import com.tesco.vendingmachine.exception.AlreadyOnException;
import com.tesco.vendingmachine.exception.ItemNotAvailableException;
import com.tesco.vendingmachine.exception.ItemNotSelectedException;
import com.tesco.vendingmachine.exception.MachineOffException;
import com.tesco.vendingmachine.exception.UnknownCoinException;
import com.tesco.vendingmachine.model.Coin;
import com.tesco.vendingmachine.model.Item;
import com.tesco.vendingmachine.model.ItemAndChange;

/**
 * Unit tests for {@link VendingMachine}
 */
public class VendingMachineTest {

	VendingMachine vendingMachine;

	@Before
	public void setup() throws AlreadyOnException {
		vendingMachine = new VendingMachine();
	}

	@Test
	public void defaultStateIsOff() {
		assertFalse(vendingMachine.isOn());
	}

	@Test
	public void turnsOn() throws AlreadyOnException {
		vendingMachine.setOn();
		assertTrue(vendingMachine.isOn());
	}

	@Test(expected = AlreadyOnException.class)
	public void turnsOnTwice() throws AlreadyOnException {
		vendingMachine.setOn();
		vendingMachine.setOn();
	}

	@Test
	public void testDispenseChangeOnePound() throws Exception {
		vendingMachine.setOn();
		vendingMachine.insertMoney(100);
		List<Coin> coinsReturned = vendingMachine.coinReturn();
		assertNotNull(coinsReturned);
		assertThat("Number of coints are incorrect", coinsReturned.size(), equalTo(1));
		assertThat("One pound coin is not returned", coinsReturned.get(0), equalTo(Coin.ONE_POUND));
	}

	@Test
	public void testDispenseChangeSixtyPence() throws Exception {
		vendingMachine.setOn();
		vendingMachine.insertMoney(50);
		vendingMachine.insertMoney(10);
		List<Coin> coinsReturned = vendingMachine.coinReturn();
		assertNotNull(coinsReturned);
		assertThat("Number of coints are incorrect", coinsReturned.size(), equalTo(2));
		assertThat("Fifty pence coin is not returned", coinsReturned.get(0), equalTo(Coin.FIFTY_PENCE));
		assertThat("Ten pence coin is not returned", coinsReturned.get(1), equalTo(Coin.TEN_PENCE));
	}

	@Test(expected = ItemNotSelectedException.class)
	public void testNullSelectItem() throws Exception {
		vendingMachine.setOn();
		vendingMachine.selectItem(null);
	}

	@Test(expected = MachineOffException.class)
	public void testSelectItemWithMachineOff() throws Exception {
		vendingMachine.selectItem(Item.ONE_POUND_ITEM);
	}

	@Test(expected = UnknownCoinException.class)
	public void testInvalidCoin() throws Exception {
		vendingMachine.setOn();
		vendingMachine.insertMoney(123);
	}

	@Test
	public void testOnePoundItemWithInsufficientMoneyInserted() throws Exception {
		vendingMachine.setOn();
		vendingMachine.selectItem(Item.ONE_POUND_ITEM);
		vendingMachine.insertMoney(10);
		ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
		assertNotNull(itemAndChange);
		assertNull(itemAndChange.getItem());
		assertThat("Number of coins returned is incorrect", itemAndChange.getChange().size(), equalTo(1));
		assertThat("Change returned is incorrect", itemAndChange.getChange().get(0), equalTo(Coin.TEN_PENCE));
	}

	@Test
	public void testOnePoundItemWithZeroChange() throws Exception {
		vendingMachine.setOn();
		vendingMachine.selectItem(Item.ONE_POUND_ITEM);
		vendingMachine.insertMoney(100);
		ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
		assertNotNull(itemAndChange);
		assertThat("Item returned is incorrect", itemAndChange.getItem(), equalTo(Item.ONE_POUND_ITEM));
		assertThat("Change returned is incorrect", itemAndChange.getChange(), equalTo(Collections.emptyList()));
		assertThat("Itemstore does not contain 9 one pound items",
				vendingMachine.getItemStore().get(Item.ONE_POUND_ITEM), equalTo(9));
		assertThat("Coinstore does not contain 11 one pound coins", vendingMachine.getCoinStore().get(Coin.ONE_POUND),
				equalTo(11));
	}

	@Test
	public void testOnePoundItemWithTenPenceChange() throws Exception {
		vendingMachine.setOn();
		vendingMachine.selectItem(Item.ONE_POUND_ITEM);
		vendingMachine.insertMoney(100);
		vendingMachine.insertMoney(10);
		ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
		assertNotNull(itemAndChange);
		assertThat("Item returned is incorrect", itemAndChange.getItem(), equalTo(Item.ONE_POUND_ITEM));
		assertThat("Change returned is incorrect", itemAndChange.getChange().size(), equalTo(1));
		assertThat("Change returned is ten pence", itemAndChange.getChange().get(0), equalTo(Coin.TEN_PENCE));
		assertThat("Coinstore does not contain 11 one pound coins", vendingMachine.getCoinStore().get(Coin.ONE_POUND),
				equalTo(11));
		assertThat("Coinstore does not contain 10 TEN pence coins", vendingMachine.getCoinStore().get(Coin.TEN_PENCE),
				equalTo(10));
		assertThat("Itemstore does not contain 9 one pound items",
				vendingMachine.getItemStore().get(Item.ONE_POUND_ITEM), equalTo(9));
	}

	@Test
	public void testOnePoundItemWithSixtyPenceChange() throws Exception {
		vendingMachine.setOn();
		vendingMachine.selectItem(Item.ONE_POUND_ITEM);
		vendingMachine.insertMoney(100);
		vendingMachine.insertMoney(50);
		vendingMachine.insertMoney(10);
		ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
		assertNotNull(itemAndChange);
		assertThat("Item returned is incorrect", itemAndChange.getItem(), equalTo(Item.ONE_POUND_ITEM));
		assertThat("Change returned is incorrect", itemAndChange.getChange().size(), equalTo(2));
		assertThat("Change returned is not fifty pence", itemAndChange.getChange().get(0), equalTo(Coin.FIFTY_PENCE));
		assertThat("Change returned is not ten pence", itemAndChange.getChange().get(1), equalTo(Coin.TEN_PENCE));
		assertThat("CoinStore does not contain 10 ten pence coins", vendingMachine.getCoinStore().get(Coin.TEN_PENCE),
				equalTo(10));
		assertThat("CoinStore does not contain  20 twnety pence coins",
				vendingMachine.getCoinStore().get(Coin.TWENTY_PENCE), equalTo(10));
		assertThat("CoinStore does not contain 10 one pound coins", vendingMachine.getCoinStore().get(Coin.ONE_POUND),
				equalTo(11));
		assertThat("Itemstore does not contain 9 one pound items",
				vendingMachine.getItemStore().get(Item.ONE_POUND_ITEM), equalTo(9));
	}

	@Test
	public void testTenOnePoundItems() throws Exception {
		vendingMachine.setOn();
		for (int i = 0; i < 10; i++) {
			vendingMachine.selectItem(Item.ONE_POUND_ITEM);
			vendingMachine.insertMoney(100);
			ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
			assertNotNull(itemAndChange);
			assertThat("Item returned is incorrect", itemAndChange.getItem(), equalTo(Item.ONE_POUND_ITEM));
			assertThat("Change returned is incorrect", itemAndChange.getChange(), equalTo(Collections.EMPTY_LIST));
		}
		assertThat("All One pound items are not empty", vendingMachine.getItemStore().get(Item.ONE_POUND_ITEM),
				equalTo(0));
		assertThat("All One pound coins are incorrect", vendingMachine.getCoinStore().get(Coin.ONE_POUND), equalTo(20));
	}

	@Test(expected = ItemNotAvailableException.class)
	public void testElevenOnePoundItems() throws Exception {
		vendingMachine.setOn();
		for (int i = 0; i < 11; i++) {
			vendingMachine.selectItem(Item.ONE_POUND_ITEM);
			vendingMachine.insertMoney(100);
			vendingMachine.getItemAndChange();
		}
	}

	@Test
	public void testAllItems() throws Exception {
		vendingMachine.setOn();
		for (int i = 0; i < 10; i++) {
			vendingMachine.selectItem(Item.ONE_POUND_ITEM);
			vendingMachine.insertMoney(100);
			ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
			assertNotNull(itemAndChange);
			assertThat("Item returned is incorrect", itemAndChange.getItem(), equalTo(Item.ONE_POUND_ITEM));
			assertThat("Change returned is incorrect", itemAndChange.getChange(), equalTo(Collections.EMPTY_LIST));
		}
		for (int i = 0; i < 10; i++) {
			vendingMachine.selectItem(Item.SIXTY_PENCE_ITEM);
			vendingMachine.insertMoney(50);
			vendingMachine.insertMoney(10);
			ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
			assertNotNull(itemAndChange);
			assertThat("Item returned is incorrect", itemAndChange.getItem(), equalTo(Item.SIXTY_PENCE_ITEM));
			assertThat("Change returned is incorrect", itemAndChange.getChange(), equalTo(Collections.EMPTY_LIST));
		}
		for (int i = 0; i < 10; i++) {
			vendingMachine.selectItem(Item.ONE_POUND_SEVENTY_PENCE_ITEM);
			vendingMachine.insertMoney(100);
			vendingMachine.insertMoney(50);
			vendingMachine.insertMoney(20);
			ItemAndChange itemAndChange = vendingMachine.getItemAndChange();
			assertNotNull(itemAndChange);
			assertThat("Item returned is incorrect", itemAndChange.getItem(),
					equalTo(Item.ONE_POUND_SEVENTY_PENCE_ITEM));
			assertThat("Change returned is incorrect", itemAndChange.getChange(), equalTo(Collections.EMPTY_LIST));
		}
		assertThat("All One pound items are not empty", vendingMachine.getItemStore().get(Item.ONE_POUND_ITEM),
				equalTo(0));
		assertThat("All ONE_POUND_SEVENTY_PENCE_ITEM items are not empty",
				vendingMachine.getItemStore().get(Item.ONE_POUND_SEVENTY_PENCE_ITEM), equalTo(0));
		assertThat("All SIXTY_PENCE_ITEM items are not empty", vendingMachine.getItemStore().get(Item.SIXTY_PENCE_ITEM),
				equalTo(0));
		assertThat("One pound coins in coinStore are incorrect", vendingMachine.getCoinStore().get(Coin.ONE_POUND),
				equalTo(30));
		assertThat("Fifty pence coins in coinStore are incorrect", vendingMachine.getCoinStore().get(Coin.ONE_POUND),
				equalTo(30));
		assertThat("Twenty pence coins in coinStore are incorrect",
				vendingMachine.getCoinStore().get(Coin.TWENTY_PENCE), equalTo(20));
		assertThat("Ten pence coins in coinStore are incorrect", vendingMachine.getCoinStore().get(Coin.TEN_PENCE),
				equalTo(20));
	}

}
