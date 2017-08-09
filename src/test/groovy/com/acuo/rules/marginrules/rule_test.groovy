package com.acuo.rules.marginrules

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_test   extends Specification {
    String ksessionName = "MarginRulesKS"

    KieSession ksession
    Logger ruleLogger
    def setup() {
        def ks = KieServices.Factory.get()
        def kContainer = ks.getKieClasspathContainer()
        ksession = kContainer.newKieSession(ksessionName)

        ruleLogger = LoggerFactory.getLogger(ksessionName)
        ksession.setGlobal("log", ruleLogger)

    }
    def "A bond with fitch rating AA using standardized approach has correspond CQS 1 "() {
        when: "add a bond asset"
        def asset = new LocalAsset(id: "a1",type: "cash")
        ksession.insert(asset)
        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get CQS"
    }

}
