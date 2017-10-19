package com.acuo.rules.concentrationlimit

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class CLResult {
    List<CLConstraint> constraints;
    int index;
}
