package com.acuo.rules.marginrules

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class MarginResult {
    double marginAmountIM = 0;
    double marginAmountVM = 0;
    double marginAmountNet = 0;
    String directionIM;
    String directionVM;
    String directionNet;
}
