package com.trade.component;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.model.Trade;
import com.trade.model.TradeResult;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssumptionsValidatorTest {

	@Spy private Trade trade;
	@Spy private AssumptionsValidator assumptionsValidator;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Before
    public void before() throws IOException{
		MockitoAnnotations.initMocks(this);
	    InputStream tradeFile = getClass().getResourceAsStream("/trade.json");
        trade = objectMapper.readValue(tradeFile, Trade.class);        
    }
	
	@Test
    public void testCustomerAssumptionsValidation() throws ParseException {
        TradeResult tradeValidator = assumptionsValidator.validate(trade);
        assertThat(tradeValidator, is(not(nullValue())));        
        
        List<String> mockedValidList = Arrays.asList("PLUTO1", "PLUTO2");
        
        assertThat(mockedValidList, hasItem(trade.getCustomer()));
        
        trade.setCustomer("PLUTO3");
        tradeValidator = assumptionsValidator.validate(trade);
        assertThat(tradeValidator.hasIssue(), is(true));
        assertThat(mockedValidList, not(hasItem(trade.getCustomer())));
             
    }
	
	@Test
    public void testLegalEntityAssumptionsValidation() throws ParseException {
        TradeResult tradeValidator = assumptionsValidator.validate(trade);
        assertThat(tradeValidator, is(not(nullValue())));
        
        List<String> mockedValidList = Arrays.asList("CS Zurich");
        
        assertThat(mockedValidList, hasItem(trade.getLegalEntity()));
        
        trade.setLegalEntity("Bolsa de valores");
        assertThat(mockedValidList, not(hasItem(trade.getLegalEntity())));
        tradeValidator = assumptionsValidator.validate(trade);
        assertThat(tradeValidator.hasIssue(), is(true));
        
    }
	
	@Test
    public  void testTypeAssumptionsValidation() {
		TradeResult tradeValidator = assumptionsValidator.validate(trade);
		assertThat(tradeValidator, is(not(nullValue())));
		
		List<String> mockedValidList = Arrays.asList("FORWARD","SPOT");
		
        Stream.of("FORWARD", "SPOT").forEach( type -> {
            trade.setType(type);
            assertThat(mockedValidList, hasItem(trade.getType()));
        });
        
        trade.setType("HOUSE");
        assertThat(mockedValidList, not(hasItem(trade.getType())));
        tradeValidator = assumptionsValidator.validate(trade);
        assertThat(tradeValidator.hasIssue(), is(true));
     
    }
	
	@Test
    public  void testISOAssumptionsValidation() {
		TradeResult tradeValidator = assumptionsValidator.validate(trade);
        assertThat(tradeValidator, is(not(nullValue())));
        
        String ccyPair = trade.getCcyPair();
        assertThat(ccyPair.length(), is(6));
        assertEquals(ccyPair, "EURUSD");
        
        List<String> mockedValidList = Arrays.asList("USD","EUR");
        Stream.of(ccyPair.substring(0, 3), ccyPair.substring(3)).forEach( iso -> {
        	assertThat(mockedValidList, hasItem(iso));	            
	    });
        
    }
	
	@Test
    public  void testStyleAssumptionsValidation() {
		TradeResult tradeValidator = assumptionsValidator.validate(trade);
		assertThat(tradeValidator, is(not(nullValue())));
		
		List<String> mockedValidList = Arrays.asList("AMERICAN","EUROPEAN");
		
        Stream.of("AMERICAN","EUROPEAN").forEach( type -> {
        	trade.setStyle(type);
            assertThat(mockedValidList, hasItem(trade.getStyle()));
        });
        
        trade.setStyle("BRAZIL");
        assertThat(mockedValidList, not(hasItem(trade.getStyle())));
        assertThat(tradeValidator.hasIssue(), is(true));
       
    }
	
	@Test
    public  void testDateValueAssumptionsValidation() {
		TradeResult tradeValidator = assumptionsValidator.validate(trade);
		assertThat(tradeValidator, is(not(nullValue())));
		
		//value date is empty
	    assertThat(tradeValidator.hasIssue(), is(true));
	    
	    trade.setValueDate(trade.getTradeDate().minusDays(1));
	    
	    assertTrue(trade.getTradeDate().isAfter(trade.getValueDate()));
	    
	    assertTrue(trade.getExpiryDate().isBefore(trade.getDeliveryDate()));
	    
	    assertTrue(trade.getPremiumDate().isBefore(trade.getDeliveryDate()));
	    
	    assertTrue(trade.getExcerciseStartDate().isAfter(trade.getTradeDate()));
	    
	    assertTrue(trade.getExcerciseStartDate().isBefore(trade.getExpiryDate()));
        
    }

}