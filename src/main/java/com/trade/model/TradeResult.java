package com.trade.model;

import java.util.ArrayList;
import java.util.Collection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TradeResult {

	private String item;
    private String msg;
    private Collection<TradeResult> issues = new ArrayList<>();
   
    public static TradeResult tradeResult() {
        return new TradeResult();
    }
    
    public Collection<TradeResult> issues() {
        return issues;
    }

    public TradeResult withError(TradeResult issue) {
    	issues.add(issue);
        return this;
    }
    
    public boolean hasIssue() {
        return !issues.isEmpty();
    }
    
    public String item() {
        return item;
    }

    public TradeResult item(String item) {
        this.item = item;
        return this;
    }

    public String msg() {
        return msg;
    }

    public TradeResult msg(String msg) {
        this.msg = msg;
        return this;
    }
    
}
