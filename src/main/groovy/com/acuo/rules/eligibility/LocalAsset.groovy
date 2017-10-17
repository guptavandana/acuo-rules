package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class LocalAsset {
    String id
    String assetId
    String idType
    String name;
    String currency
    String type
    String ICADCode;
    String ticker;

    String fitchRating;
    double parValue;
    double minUnit;
    double internalCost;
    double opptCost;
    double availableQuantities;
    double notional;

    Integer CQS
    String assetType
    String eeaClass;
    String issuerId;
    String issuerGroupId;
    String issuerCountryCode;
    String issuerSector;
    boolean issuerIsMemeberState;
    String moodyRating;
    String snpRating;
    String ratingMethod;
    String creditPrincipalCode;
    String convertibleTypeCode;

    String index;
    String exchange;
    String rateType;
    double maturityYears = -1;
    double usClass;
    boolean convertibleFlag;
}
