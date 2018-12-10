package com.google.gwt.Jostle.client;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Test;

class BudgetCategoryTest {
	private double ZERO = 0.00;
	private double goodAmount = 990;
	private double goodSmallAmount = 9;
	private double negAmount = -1.00;
	private double overAmount = 20000000.00;
	private String goodString = "School Textbooks";
	private String badString = "Lopadotemachoselachogaleokranio"
			+ "leipsanodrimhypotrimmatosilphioparaomelito"
			+ "katakechymenokichlepikossyphophattoperister"
			+ "alektryonoptekephalliokigklopeleiola";
	
	private BudgetCategory goodObj = new BudgetCategory(goodString, goodAmount);
	
	@Test
	void testBudgetCategory() {
		BudgetCategory c1 = new BudgetCategory(goodString, goodAmount);
		
		assertEquals(c1.getBudgetAmount(), goodAmount);
		assertEquals(c1.getbudgetRemaining(), 0.00);
		assertEquals(c1.getbudgetSpent(), 0.00);
		
		try {
			BudgetCategory c2 = new BudgetCategory(badString, goodAmount);
			fail("Should be invalid string");
		} catch (ValidationException e) {}
		try {
			BudgetCategory c3 = new BudgetCategory(goodString, negAmount);
			fail("Should be invalid negative amount");
		} catch (ValidationException e) {}
		try {
			BudgetCategory c4 = new BudgetCategory(goodString, overAmount);
			fail("Should be invalid over amount");
		} catch (ValidationException e) {}
	}

	@Test
	void testGetCategoryName() {
		assertEquals(goodObj.getCategoryName(), goodString);
	}

	@Test
	void testGetBudgetAmount() {
		assertEquals(goodObj.getBudgetAmount(), goodAmount);
	}

	@Test
	void testGetbudgetSpent() {
		BudgetCategory c5 = new BudgetCategory(goodString, goodAmount);
		assertEquals(c5.getbudgetSpent(), ZERO);
		c5.increasebudgetSpent(goodSmallAmount);
		assertEquals(c5.getbudgetSpent(), goodSmallAmount);
		c5.increasebudgetSpent(goodSmallAmount);
		assertEquals(c5.getbudgetSpent(), goodSmallAmount * 2);
	}

	@Test
	void testGetbudgetRemaining() {
		BudgetCategory c7 = new BudgetCategory(goodString, goodAmount);
		assertEquals(c7.getbudgetRemaining(), ZERO);
		c7.increasebudgetSpent(goodSmallAmount);
		assertEquals(c7.getbudgetRemaining(), goodAmount - goodSmallAmount);
	}

	@Test
	void testSetBudgetAmount() {
		BudgetCategory c5 = new BudgetCategory(goodString, goodAmount);
		assertEquals(c5.getBudgetAmount(), goodAmount);
	}

	@Test
	void testResetbudgetSpent() {
		BudgetCategory c5 = new BudgetCategory(goodString, goodAmount);
		c5.increasebudgetSpent(goodSmallAmount);
		assertEquals(c5.getbudgetSpent(), goodSmallAmount);
		c5.resetbudgetSpent();
		assertEquals(c5.getbudgetSpent(), ZERO);
	}

	@Test
	void testResetBudgetCategory() {
		BudgetCategory c5 = new BudgetCategory(goodString, goodAmount);
		c5.increasebudgetSpent(goodSmallAmount);
		assertEquals(c5.getbudgetSpent(), goodSmallAmount);
		c5.resetBudgetCategory();
		assertEquals(c5.getbudgetSpent(), ZERO);
		assertEquals(c5.getBudgetAmount(), ZERO);
	}

	@Test
	void testIncreasebudgetSpent() {
		BudgetCategory c5 = new BudgetCategory(goodString, goodAmount);
		assertEquals(c5.getbudgetSpent(), ZERO);
		c5.increasebudgetSpent(goodSmallAmount);
		assertEquals(c5.getbudgetSpent(), goodSmallAmount);
		c5.increasebudgetSpent(goodSmallAmount);
		assertEquals(c5.getbudgetSpent(), goodSmallAmount * 2);
	}

	@Test
	void testDecreasebudgetSpent() {
		BudgetCategory c6 = new BudgetCategory(goodString, goodAmount);
		assertEquals(c6.getbudgetSpent(), ZERO);
		c6.decreasebudgetSpent(goodSmallAmount);
		c6.increasebudgetSpent(goodSmallAmount * 2);
		try {
			c6.decreasebudgetSpent(overAmount);
		} catch (ValidationException e) {}
	}

}
