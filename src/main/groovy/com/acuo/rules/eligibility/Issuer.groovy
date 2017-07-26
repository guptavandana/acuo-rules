package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class Issuer {
    String name
    String lei
    String country
    String countryCode
    String domCurrency
    String ultimateParentLei
    String entityLei
    String legalEntityType
    Boolean isMemberState = false
    Boolean isMultiDevBank = false
    Boolean isInterOrg = false
    Boolean isUSGovtAgency = false
    Boolean isEUCtrBank = false
    Boolean isSovRWLess20 = false
}