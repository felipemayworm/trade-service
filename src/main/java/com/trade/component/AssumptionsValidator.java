package com.trade.component;

import static com.trade.model.TradeResult.tradeResult;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.trade.model.Trade;
import com.trade.model.TradeResult;
import com.trade.service.TradeValidatorService;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Component
public class AssumptionsValidator implements TradeValidatorService {
	
	private List<String> validCustomers = new ArrayList<>();
	private List<String> weekendDays = new ArrayList<>();
	private Set<String> legalEntities = new HashSet<>();
	private Set<String> spotTypes = new HashSet<>();
    private Set<String> forwardTypes = new HashSet<>();
    private Set<String> iso = new HashSet<>();
    private Set<String> styles = new HashSet<>();
    private LocalDate todayDate;
	
	
    @Value("${assumptions.CUSTOMERS}")
    public String validCustomersFromString(String value) {
        validCustomers = new ArrayList<>(Arrays.asList(value.split(",")));
        return validCustomers.toString();
    }
    
    @Value("${assumptions.WEEKEND}")
    public String validWeekend(String value) {
    	weekendDays = new ArrayList<>(Arrays.asList(value.split(",")));
        return weekendDays.toString();
    }
    
    @Value("${assumptions.ENTITY}")
    public String loadValidLegalEntities(String value) {
        legalEntities = new HashSet<>(Arrays.asList(value.split(",")));
        return legalEntities.toString();
    }
    
    @Value("${assumptions.ISO}")
    public String loadValidISO(String value) {
    	iso = new HashSet<>(Arrays.asList(value.split(",")));
        return iso.toString();
    }
    
    @Value("${assumptions.CURRENTDATE}")
    public void setTodayDateString(String value) {
    	todayDate = LocalDate.parse(value);
    }

    @Value("${assumptions.SPOTTYPE}")
    public String loadValidSpotTypes(String value) {
        spotTypes = new HashSet<>(Arrays.asList(value.split(",")));
        return spotTypes.toString();
    }

    @Value("${assumptions.FORWARDTYPE}")
    public String loadValidForwardTypes(String value) {
        forwardTypes = new HashSet<>(Arrays.asList(value.split(",")));
        return forwardTypes.toString();
    }
    
    @Value("${assumptions.STYLES}")
    public String validStyles(String value) {
    	styles = new HashSet<>(Arrays.asList(value.split(",")));
        return styles.toString();
    }

    
    
    @Override
    public TradeResult validate(Trade trade) {
    	TradeResult tradeResult = tradeResult();
    	validateDateValue(trade, tradeResult);
    	validateCustomer(trade, tradeResult);
    	validateEntity(trade, tradeResult);
    	validateType(trade, tradeResult);
    	validateStyle(trade, tradeResult);
    	validateISO(trade, tradeResult);
    	
    	return tradeResult;
    } 

    private TradeResult validateCustomer(Trade trade, TradeResult tradeResult) {
    	if (!StringUtils.hasText(trade.getCustomer())) {
        	tradeResult.withError(tradeResult.item("customer")
        									 .msg("Customer is empty"));
        	log.info("Customer is empty.");
        }

        if (!validCustomers.contains(trade.getCustomer())) {
        	tradeResult.withError(tradeResult.item("customer")
        									 .msg("Customer is non-valid assumption list"));
        	log.info("Customer is non-valid assumption list.");
        }
        
        return tradeResult;        
    }
    
    private TradeResult validateDateValue(Trade trade, TradeResult tradeResult) {
		 if (trade.getValueDate() == null) {
	     	tradeResult.withError(tradeResult.item("valueDate")
	     									 .msg("Value Date is empty"));
	     	log.info("Value Date is empty.");
	     	return tradeResult;
	     }
		 
         if (weekendDays.contains(trade.getValueDate().getDayOfWeek().name())) {
        	 tradeResult.withError(tradeResult.item("valueDate")
        			 						  .msg("Value Date is a Weekend day"));
        	 log.info("Value Date is a Weekend day.");
        	 return tradeResult;
         }
	
	     if (trade.getTradeDate() == null) {
	     	tradeResult.withError(tradeResult.item("tradeDate")
	     									 .msg("Trade Date is empty"));
	     	log.info("Trade date is empty.");
	     	return tradeResult;
	     }
	
	     if (!tradeResult.hasIssue()) {
	         if (trade.getValueDate().isBefore(trade.getTradeDate())) {
	         	tradeResult.withError(tradeResult.item("valueDate")
	         									 .msg("Value date cannot be before Trade date"));
	         	log.info("Value date cannot be before the Trade Date.");
	         }
	     }        
	     
	     if (trade.getDeliveryDate() != null) {
	    		
            if (trade.getExpiryDate() == null) {
            	tradeResult.withError(tradeResult.item("expiryDate")
            									 .msg("Expiry date is empty"));
            	log.info("Expiry date is empty.");
            } else {
                if (!trade.getExpiryDate().isBefore(trade.getDeliveryDate())) {
                	tradeResult.withError(tradeResult.item("expiryDate")
                									 .msg("Expiry date cannot be after Delivery Date"));
                	log.info("Expiry date cannot be after Delivery Date.");
                }
            }

            if (trade.getPremiumDate() == null) {
            	tradeResult.withError(tradeResult.item("premiumDate")
            									 .msg("Premium Date is empty"));
            	log.info("Premium Date is empty.");
            } else {
                if (!trade.getPremiumDate().isBefore(trade.getDeliveryDate())) {
                	tradeResult.withError(tradeResult.item("premiumDate")
                									 .msg("Premium Date cannot be after Delivery Date"));
                	log.info("Premium Date cannot be after Delivery Date.");
                }
            }
            
        } else {
        	tradeResult.withError(tradeResult.item("deliveryDate")
        									 .msg("DeliveryDate is empty"));
        	log.info("Delivery Date is empty.");
        }
	     
	    return tradeResult;    	
    }
    
    private TradeResult validateEntity(Trade trade, TradeResult tradeResult) {
    	if(!StringUtils.hasText(trade.getLegalEntity())){
    		tradeResult.withError(tradeResult.item("legalEntity")
					 						 .msg("Legal entity is empty"));
    		log.info("Legal Entity is empty.");
    		
    		return tradeResult;
    	}
    	
    	if(!legalEntities.contains(trade.getLegalEntity())) {
        	tradeResult.withError(tradeResult.item("legalEntity")
        									 .msg("Legal entity is invalid assumption"));
        	log.info("Legal Entity is invalid assumption.");
        }
    	
    	return tradeResult;        
    }
    
    private TradeResult validateType(Trade trade, TradeResult tradeResult) {
    	if (trade.getValueDate() == null) return tradeResult;
    	
    	if (Stream.concat(spotTypes.stream(), forwardTypes.stream()).filter( type -> type.equalsIgnoreCase(trade.getType()) ).findFirst().isPresent()) {
    	
	    	Period periodDays = Period.between(LocalDate.now(), trade.getValueDate());
	    	
	    	if (forwardTypes.stream()
	                .filter( type -> type.equalsIgnoreCase(trade.getType()))
	                .findFirst().isPresent())  {        	
	        	
	            if (periodDays.getDays() <= 3) {        
	                tradeResult.withError(tradeResult.item("valueDate")
	                								 .msg("Value Date to Forward Trades have than more 3 days"));
	                log.info("Value Date to Forward Trades have than more 3 days.");
	            }
	            
	        }
	        
	    	if (spotTypes.stream().filter( type -> type.equalsIgnoreCase(trade.getType()))
	        					  .findFirst().isPresent()) {        	            
	            if (periodDays.getDays() != 3) {
	                tradeResult.withError(tradeResult.item("valueDate")
	                								 .msg("Value Date to Spot Trades have 3 days plus from current day"));
	                log.info("Spot Trades have 3 days plus from current day.");
	            }
	            
	            return tradeResult;
	        }
	        
    	}else{
    		tradeResult.withError(tradeResult.item("style")
								 .msg("Type is invalid"));
			log.info("Type is invalid.");
			
			return tradeResult;
			}
        
        return tradeResult;        
    }
    
    private TradeResult validateStyle(Trade trade, TradeResult tradeResult) {
    	if (styles.stream().filter( style -> style.equalsIgnoreCase(trade.getStyle()) ).findFirst().isPresent()) {
	
    		// check Excercise Start Date exist to AMERICAN and check the rules
			if (styles.contains(trade.getStyle()) && trade.getStyle().equalsIgnoreCase("AMERICAN")) {
				
			    if (trade.getExcerciseStartDate() == null) {
			    	tradeResult.withError(tradeResult.item("excerciseStartDate")
			    									 .msg("Excercise Start Date is empty"));
			    	log.info("Excercise Start Date is empty.");
			    	return tradeResult;			    
			    }else {
			    	
			    	if (trade.getTradeDate() != null){
			        	if(!trade.getExcerciseStartDate().isAfter(trade.getTradeDate())) {
			        		tradeResult.withError(tradeResult.item("excerciseStartDate")
			        										 .msg("Excercise Start Date cannot be before Trade Date"));
			        		log.info("Excercise Start Date cannot be before Trade Date.");
				        }			        	
			        }else {
			        	tradeResult.withError(tradeResult.item("tradeDate")
								 .msg("Trade Date is empty"));
			        	log.info("Trade Date is empty.");				        	
					}
			    	
			        if (trade.getExpiryDate() != null){ 
			        	if(!trade.getExcerciseStartDate().isBefore(trade.getExpiryDate())) {			        
			            	tradeResult.withError(tradeResult.item("excerciseStartDate")
			            									 .msg("Excercise Start Date cannot be after Expiry Date"));
			            	log.info("Excercise Start Date cannot be after Expiry Date.");
			        	}
			        }else {
			        	tradeResult.withError(tradeResult.item("expiryDate")
								 .msg("Expiry Date is empty"));
			        	log.info("Expiry Date is empty.");
					}			    	
			    }			    
			}			
			
    	}else{
    		tradeResult.withError(tradeResult.item("style")
    										 .msg("Style is invalid"));
    		log.info("Style is invalid.");
    		
        	return tradeResult;
    	}
    	
    	return tradeResult;        
    }
    
    private TradeResult validateISO(Trade trade, TradeResult tradeResult) {
    	String ccyPair = trade.getCcyPair();
        
    	if (ccyPair.isEmpty()) {
        	tradeResult.withError(tradeResult.item("ccyPair")
					 .msg("CCY Pair is empty"));
        	log.info("CCY Pair is empty.");     
        	
        	return tradeResult;
        }

        if (ccyPair.length() != 6) {
        	tradeResult.withError(tradeResult.item("ccyPair")
					 .msg("CCY Pair need to have six character"));
        	log.info("CCY Pair need to have six character."); 
        	
        	return tradeResult;
        }

        if(!iso.contains(ccyPair.substring(0, 3)) && !iso.contains(ccyPair.substring(3))) {
        	tradeResult.withError(tradeResult.item("ccyPair")
					 .msg("CCY Pair does not a currency assumption"));
        	log.info("CCY Pair does not a currency assumption.");   
        }
        
        return tradeResult;
        
    }
   
}
