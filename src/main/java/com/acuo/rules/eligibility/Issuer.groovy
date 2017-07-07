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
    Boolean isMemberState = false
    Boolean isMultiDevBank = false
    Boolean isInterOrg = false
}