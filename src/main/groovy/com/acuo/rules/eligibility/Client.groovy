package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class Client {
    String lei
    String name
    String entityLei
    String ultimateParentLei
    String ultimateParentName
    String fitchRating;
}
