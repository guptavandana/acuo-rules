package com.acuo.rules.marginRules

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class xls_test extends Specification {
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
    def "VM above MTA should be generated"() {
        when: "add a VM call"
        def callVM = new MarginCall(marginType: "Variation")

        ksession.insert(callVM)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
    }

}
