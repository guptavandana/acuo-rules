package com.acuo.rules.eligibility

import lombok.Data

@Data
class Issuer {
    private String name
    private String lei
    private String country
    private String countryCode
    private String domCurrency
    private String ultimateParentLei
    private Boolean isMemberState = false
    private Boolean isMultiDevBank = false
    private Boolean isInterOrg = false
}