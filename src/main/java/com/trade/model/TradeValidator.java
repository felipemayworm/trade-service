package com.trade.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeValidator {

	private Trade trade;
    private Map<String, Collection<String>> nonValidItem = new ConcurrentHashMap<>();

    public static TradeValidator tradeValidator() {
        return  new TradeValidator();
    }

    public TradeValidator() {}

    public TradeValidator addInvalidField(String item, String msg) {

        if (!nonValidItem.containsKey(item)) {
        	nonValidItem.put(item, ConcurrentHashMap.newKeySet());
        }
        nonValidItem.get(item).add(msg);
        return this;
    }


    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    @JsonGetter("haveErrors")
    public boolean haveErrors() {
        return !nonValidItem.isEmpty();
    }

    public TradeValidator trade(Trade trade) {
        this.trade = trade;
        return this;
    }

    public Map<String, Collection<String>> invalidFields() {
        return nonValidItem;
    }
    
}
