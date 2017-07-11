package com.acuo.rules.eligibility

import lombok.Data

@Data
class Issuer {
    String name
    String lei
    String country
    String countryCode
    String domCurrency
    String ultimateParentLei
    String entityLei
    Boolean isMemberState = false
    Boolean isMultiDevBank = false
    Boolean isInterOrg = false
    boolean isUSGovtAgency = false
    Boolean isEUCtrBank = false
    Boolean isSovRWLess20 = false
}