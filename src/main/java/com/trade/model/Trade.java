package com.trade.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor()
@EqualsAndHashCode
@ToString
public class Trade {

	private String customer;
    private String ccyPair;
    private String type;
    private String style;
    private String direction;
    private String strategy;
   
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tradeDate;
    
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal rate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate excerciseStartDate;
    
    private String payCcy;
    private BigDecimal premium;
    private String premiumCcy;
    private String premiumType;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate premiumDate;
    
    private String legalEntity;
    private String trader;    
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate valueDate;
	
    public Trade() {
		super();
	}
    
}
