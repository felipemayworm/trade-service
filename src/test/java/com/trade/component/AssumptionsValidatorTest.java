package com.trade.component;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.model.Trade;
import com.trade.model.TradeResult;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssumptionsValidatorTest {

	private Trade trade;
	private AssumptionsValidator assumptionsValidator = new AssumptionsValidator();
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Before
    public void before() throws IOException{
	    trade = new Trade();
        InputStream tradeFile = getClass().getResourceAsStream("/trade.json");
        trade = objectMapper.readValue(tradeFile, Trade.class);
    }
	
	@Test
    public void testCustomerAssumptionsValidation() throws ParseException {
        trade.setCustomer("PLUTO3");
        
        TradeResult tradeValidator = assumptionsValidator.validate(trade);

        assertThat(tradeValidator, is(not(nullValue())));
        assertThat(tradeValidator.hasIssue(), is(true));
        assertThat(tradeValidator.issues().size(), is(5));

    }
	
	@Test
    public void testLegalEntityAssumptionsValidation() throws ParseException {
        trade.setCustomer("Bolsa de valores");

        TradeResult tradeValidator = assumptionsValidator.validate(trade);

        assertThat(tradeValidator, is(not(nullValue())));
        assertThat(tradeValidator.hasIssue(), is(true));
        assertThat(tradeValidator.issues().size(), is(5));

    }
	
	@Test
    public  void testTypeAssumptionsValidation() {
        Stream.of("FORWARD", "SPOT").forEach( type -> {
            trade.setType(type);
            trade.setValueDate(null);

            TradeResult tradeValidator = assumptionsValidator.validate(trade);

            assertThat(tradeValidator, is(not(nullValue())));
            assertThat(tradeValidator.hasIssue(), is(true));
            assertThat(tradeValidator.issues().size(), is(5));

        });
    }
	
	@Test
    public  void testISOAssumptionsValidation() {
		trade.setCcyPair("EUSROSW");

        TradeResult tradeValidator = assumptionsValidator.validate(trade);

        assertThat(tradeValidator, is(not(nullValue())));
        assertThat(tradeValidator.hasIssue(), is(true));
        assertThat(tradeValidator.issues().size(), is(5));
       
    }
	
	@Test
    public  void testStyleAssumptionsValidation() {
		trade.setStyle("BRAZIL");

		TradeResult tradeValidator = assumptionsValidator.validate(trade);

		assertThat(tradeValidator, is(not(nullValue())));
        assertThat(tradeValidator.hasIssue(), is(true));
        assertThat(tradeValidator.issues().size(), is(5));
       
    }
	
	@Test
    public  void testDateValueAssumptionsValidation() {
		trade.setTradeDate(null);
	    trade.setValueDate(null);

		TradeResult tradeValidator = assumptionsValidator.validate(trade);

		assertThat(tradeValidator, is(not(nullValue())));
        assertThat(tradeValidator.hasIssue(), is(true));
        assertThat(tradeValidator.issues().size(), is(5));
       
    }

}
