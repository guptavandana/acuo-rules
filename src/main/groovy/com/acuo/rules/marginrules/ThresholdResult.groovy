package com.acuo.rules.marginrules

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class ThresholdResult {
    double exposure=0;
    boolean aboveThreshold=false;
}
