package com.tesco.vendingmachine.model;

public enum Item {
	SIXTY_PENCE_ITEM(60), ONE_POUND_ITEM(100), ONE_POUND_SEVENTY_PENCE_ITEM(170);
	private int priceInPence;

	Item(int priceInPence) {
		this.priceInPence = priceInPence;
	}

	public int getPriceInPence() {
		return this.priceInPence;
	}
}
