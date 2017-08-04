package com.trade.service;

import com.trade.model.Trade;
import com.trade.model.TradeResult;

public interface TradeValidatorService {
	
	TradeResult validate(Trade trade);
	
}
