package com.acuo.rules.marginRules

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class MarginCall {
    String marginType;
    String direction;
    double deliverAmount;
    double returnAmount;
}
