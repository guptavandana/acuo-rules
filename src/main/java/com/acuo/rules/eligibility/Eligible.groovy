package com.acuo.rules.eligibility

import lombok.Data

@Data
class Eligible {
    String classType
    String exchange
    boolean isEligible = false
    double haircut
    double fxHaircut

}
