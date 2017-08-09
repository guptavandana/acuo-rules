package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class EligibleResult {
    String classType = "Null"
    boolean isEligible = false
    double haircut = -1
    double fxHaircut = -1
    double valuationPercentage = -1
    double securityAR = -1
    double FXAR = -1
}
