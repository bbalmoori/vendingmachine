package com.tesco.vendingmachine.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemAndChange {
	private Item item;
	private List<Coin> change;
}
