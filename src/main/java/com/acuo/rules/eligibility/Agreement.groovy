package com.acuo.rules.eligibility

import lombok.Data;

@Data
class Agreement {
    String marginType;
    String id;
    String majorCurrency;
    String terminateCurrency;
    String settlementCurrency;
}
