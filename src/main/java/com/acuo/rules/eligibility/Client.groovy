package com.acuo.rules.eligibility

import lombok.Data;

@Data
class Client {
    String lei
    String name
    String entityLei
    String ultimateParentLei
    String ultimateParentName
    String fitchRating;
}
