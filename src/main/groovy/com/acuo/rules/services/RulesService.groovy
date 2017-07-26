package com.acuo.rules.services

interface RulesService {

    void fireRules(Object fact)

    void fireRules(List<?> facts)
}
