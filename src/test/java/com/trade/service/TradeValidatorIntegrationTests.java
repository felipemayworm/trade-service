package com.trade.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.model.Trade;
import com.trade.model.TradeValidator;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeValidatorIntegrationTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ValidationService validationService;
	
	@Test
	public void contextLoads() {}
	
	@Test
	public void testTradeValidation() throws IOException {
		InputStream tradeFile = getClass().getResourceAsStream("/trade.json");
		
		Trade trade = objectMapper.readValue(tradeFile, Trade.class);
		
		TradeValidator tradeValidator = validationService.checkTrade(trade);
		
		assertThat(tradeValidator, is(not(nullValue())));
		assertThat(tradeValidator.getTrade(), is(trade));		
		assertThat(tradeValidator.haveErrors(), is(true)); // because the single json does not have Value Date
		assertThat(tradeValidator.getNonValidItem(), is(not(nullValue())));
		assertThat(tradeValidator.getNonValidItem().size(), is(1));
		assertThat(tradeValidator.getNonValidItem().get("valueDate"), is(not(nullValue())));

	}
	
}
