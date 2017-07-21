package com.acuo.rules.eligibility

import lombok.Data

@Data
class Eligible {
    String classType = "Null"
    boolean isEligible = false
    double haircut = -1
    double fxHaircut = -1
    double valuationPercentage = -1
    double securityAR = -1
    double FXAR = -1
}
