package com.acuo.rules.services

import org.kie.api.runtime.KieSession
import org.slf4j.LoggerFactory

class EligibilityService extends RulesServiceImpl {

    final def ksessionName = "EligibilityKS"

    final def ruleLogger = LoggerFactory.getLogger(ksessionName)

    KieSession newSession() {
        def session = engine.newKieSession(ksessionName)
        session.setGlobal("log", ruleLogger)
        return session
    }
}