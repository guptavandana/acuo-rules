package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class RuleList {
    String[] listname;
    double[] listHaircut;
    double[] listFXHaircut;
    boolean[] listEligible;
    Integer startSignal = 0;
    Integer listLength = 0;
    Integer listIndex = -1;
    Integer lengthRegime = 0;
    Integer lengthProvider = 0;
    Integer fitchFxStop = 0;
    Integer fitchSecStop = 0;
    Integer endSignal = 0;
    String provider;
}
