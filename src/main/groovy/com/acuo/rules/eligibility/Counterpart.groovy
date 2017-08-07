package com.acuo.rules.eligibility

import groovy.transform.ToString

@ToString(includePackage=false, includeNames=true)
class Counterpart {
    String lei
    String name
    String entityLei
    String ultimateParentLei
    String ultimateParentName
    String countryCode
    boolean withCollateralFlipClause = false
    String fitchRating
    String highestRatedNote
}
