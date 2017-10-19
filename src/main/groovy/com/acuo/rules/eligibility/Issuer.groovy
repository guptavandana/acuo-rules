package com.acuo.rules.eligibility

import groovy.transform.ToString
import com.opengamma.strata.basics.currency.Currency
@ToString(includePackage=false, includeNames=true)
class Issuer {
    String countryCode
    Currency domCurrency
    String sector
    Boolean isMemberState = false
    String id;
    String groupId;
}