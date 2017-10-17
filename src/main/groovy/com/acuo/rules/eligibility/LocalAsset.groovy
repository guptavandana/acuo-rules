package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class LocalAsset {
    String id
    String type
    Integer CQS
    String assetType
    String eeaClass;
    String currency
    String idType;
    String name;
    String ICADCode;
    String ticker;
    String fitchRating;
    String moodyRating;
    String snpRating;
    String ratingMethod;
    String creditPrincipalCode;
    String convertibleTypeCode;
    String issuerId;
    String issuerGroupId;
    String issuerCountryCode;
    String issuerSector;
    boolean issuerIsMemeberState;
    String index;
    String exchange;
    String rateType;
    double maturityYears = -1;
    double setUsClassType;
    double parValue;
    double minUnit;
    double internalCost;
    double opptCost;
    double availableQuantities;
    double notional;
    boolean convertibleFlag;
}
