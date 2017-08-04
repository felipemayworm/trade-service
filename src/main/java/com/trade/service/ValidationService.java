package com.trade.service;

import static com.trade.model.TradeValidator.tradeValidator;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.model.Trade;
import com.trade.model.TradeValidator;

@Service
public class ValidationService{

    private Collection<TradeValidatorService> tradeValidatorService;

    public ValidationService() {}
    
    @Autowired
    public ValidationService withValidators(Collection<TradeValidatorService> tradeValidatorService) {
        this.tradeValidatorService = tradeValidatorService;
        return this;
    }

    public TradeValidator checkTrade(Trade trade) {

        TradeValidator tradeValidator = tradeValidator().trade(trade);

        tradeValidatorService.stream()
							 .map( validator ->  validator.validate(trade))             
							 .filter(validationResult -> validationResult != null && validationResult.hasIssue())  
							 .forEach(validationError -> validationError.issues().forEach(issue -> tradeValidator.addInvalidField(issue.item(), issue.msg())) );

        return tradeValidator;
    }

	public Collection<TradeValidator> checkTrades(Collection<Trade> trades) {
        return trades.parallelStream()
                	 .map(this::checkTrade)
                	 .collect(Collectors.toList());
    }

}
