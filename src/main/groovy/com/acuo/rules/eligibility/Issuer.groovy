package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class Issuer {
    String countryCode
    String domCurrency
    String sector
    Boolean isMemberState = false
}