package com.acuo.rules.assets

import groovy.transform.ToString
import java.util.Set;

@ToString(includePackage=false, includeNames=true)
class Array {
    Currency[] currencies;
    Set<Currency> ccys;
    Currency currency;
    boolean stop=false;
}
