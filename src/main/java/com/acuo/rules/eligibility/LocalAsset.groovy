package com.acuo.rules.eligibility

import lombok.Data

@Data
class LocalAsset {
    String id
    String type
    double maturityYears
    Integer CQS
}
