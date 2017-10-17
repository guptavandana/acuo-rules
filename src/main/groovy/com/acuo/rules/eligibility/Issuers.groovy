package com.acuo.rules.eligibility

import groovy.transform.ToString
import java.util.*

@ToString(includePackage=false, includeNames=true)
class Issuers {
    List<String> groupIds;
    List<String> groupIdsUnique=[];
    int groupIdsLength=0;
    List<String> ids;
    List<String> idsUnique=[];
    int idsLength=0;
    List<String> countries;
    List<String> countriesUnique=[];
    int countriesLength=0;
    int index;
    boolean initiate=false;
    boolean end_1a=false;
    boolean end_2a=false;
}
