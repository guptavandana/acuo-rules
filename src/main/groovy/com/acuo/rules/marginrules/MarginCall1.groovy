package com.acuo.rules.marginrules

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class MarginCall1 {
    String marginType;
    String direction;
    double exposure;
    double deliverAmount;
    double returnAmount;
}
