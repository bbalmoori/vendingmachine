package com.tesco.vendingmachine;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.tesco.vendingmachine.exception.NotEnoughChangeException;
import com.tesco.vendingmachine.model.Coin;

public class ChangeDispenserTest {

	ChangeDispenser changeDispenser = new ChangeDispenser();

	@Test
	public void testOnePoundChange() throws Exception {
		List<Coin> coins = changeDispenser.dispenseChange(100, getCoinStore());
		assertThat("Number of coins returned are incorrect", coins.size(), equalTo(1));
		assertThat("Change returned is incorrect", coins.get(0), equalTo(Coin.ONE_POUND));
	}
	
	@Test
	public void testEightyPenceChange() throws Exception {
		List<Coin> coins = changeDispenser.dispenseChange(80, getCoinStore());
		assertThat("Number of coins returned are incorrect", coins.size(), equalTo(3));
		assertTrue("Fifty pence coin is not returned", coins.contains(Coin.FIFTY_PENCE));
		assertTrue("Twenty pence coin is not returned", coins.contains(Coin.TWENTY_PENCE));
		assertTrue("Ten pence coin is not returned", coins.contains(Coin.TEN_PENCE));
	}
	
	@Test
	public void testSixtyPenceChange() throws Exception {
		List<Coin> coins = changeDispenser.dispenseChange(60, getCoinStore());
		assertThat("Number of coins returned are incorrect", coins.size(), equalTo(2));
		assertTrue("Fify pence coin is not returned", coins.contains(Coin.FIFTY_PENCE));
		assertTrue("Ten pence coin is not returned", coins.contains(Coin.TEN_PENCE));
	}
	
	@Test
	public void testFourtyPenceChange() throws Exception {
		List<Coin> coins = changeDispenser.dispenseChange(40, getCoinStore());
		assertThat("Number of coins returned are incorrect", coins.size(), equalTo(2));
		assertThat("Fify pence coin is not returned", coins.get(0), equalTo(Coin.TWENTY_PENCE));
		assertThat("Fify pence coin is not returned", coins.get(0), equalTo(Coin.TWENTY_PENCE));
	}
	
	@Test(expected = NotEnoughChangeException.class)
	public void testNotEnoughChangeException() throws Exception {
		changeDispenser.dispenseChange(65, getCoinStore());
	}
	
	@Test(expected = NotEnoughChangeException.class)
	public void testCoinStoreRanOutOfMoney() throws Exception {
		changeDispenser.dispenseChange(3000, getCoinStore());
	}
	
	private Map<Coin, Integer> getCoinStore() {
		Map<Coin, Integer> coinStore = new HashMap<>();
		Arrays.stream(Coin.values()).forEach(coin -> coinStore.put(coin, 10));
		return coinStore;
	}

}
