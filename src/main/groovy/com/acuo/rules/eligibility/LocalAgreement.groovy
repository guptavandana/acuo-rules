package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class LocalAgreement {
    Integer trigger;
    String marginType
    String eligibleCurrency
    String terminateCurrency
    String settlementCurrency
    String id;
    String majorCurrency;
    String baseCurrency;
}
