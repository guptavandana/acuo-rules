package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class MarginCall {
    String callType;
    double amount;
    String id;
    double fxRate;
    String currency;
}
