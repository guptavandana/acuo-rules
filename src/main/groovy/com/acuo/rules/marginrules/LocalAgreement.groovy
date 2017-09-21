package com.acuo.rules.marginrules

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class LocalAgreement {
    double MTA1;
    double MTA2;
    String type;
    double thresholdAmount;
    String thresholdTreatment;
}
