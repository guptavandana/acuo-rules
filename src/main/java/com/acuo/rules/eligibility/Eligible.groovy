package com.acuo.rules.eligibility

import lombok.Data

@Data
class Eligible {
    String classType
    boolean isEligible = false
    double haircut = -1
    double fxHaircut = -1

}
