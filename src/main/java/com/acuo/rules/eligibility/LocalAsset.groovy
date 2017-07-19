package com.acuo.rules.eligibility

import lombok.Data

@Data
class LocalAsset {
    String id
    String type
    Integer CQS
    String datascopeAssetType
    String currency
    String fitchRating;
    String moodyRating;
    String creditPrincipalCode;
    String convertibleTypeCode;
    String issuer;
    String index;
    String exchange;
    String rateType;
    double maturityYears = -1;
    boolean convertibleFlag;
}
