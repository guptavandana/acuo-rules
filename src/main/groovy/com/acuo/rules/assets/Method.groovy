package com.acuo.rules.assets
import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class Method {
    boolean substrInStr(Currency substr,Currency[] str) {
        return str().contains(substr);
    }
}
