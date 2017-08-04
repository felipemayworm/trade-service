package com.trade.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.trade.model.Trade;
import com.trade.model.TradeValidator;
import com.trade.service.ValidationService;

@RestController
public class ValidationController {

	@Autowired
    private ValidationService validationService;

    @PostMapping("/api/validateTrade")
    public TradeValidator validateTrade(@RequestBody Trade trade) {
    	return validationService.checkTrade(trade);
    }

    @PostMapping("/api/validateTrades")
    public Collection<TradeValidator> validateTrades(@RequestBody Collection<Trade> trades) {
        return validationService.checkTrades(trades);
    }
    
}
