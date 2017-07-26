package com.acuo.rules.services

import org.kie.api.runtime.KieSession

class EligibilityService extends RulesServiceImpl {

    protected KieSession newSession() {
        return engine.newKieSession("EligibilityKS")
    }
}