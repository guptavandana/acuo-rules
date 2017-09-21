package com.acuo.rules.marginrules

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class threshold_test extends Specification {
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
    def "VM above threshold should be generated"() {
        when: "add a VM call"
        def callVM = new MarginCall(marginType: "Variation",exposure: 60000000,deliverAmount: 50000,returnAmount: 10000,direction: "OUT")
        def agreement = new LocalAgreement(MTA1: 50000,MTA2: 100000,thresholdAmount: 50000000,thresholdTreatment: "Secured",type: "bilateral")
        def result = new ThresholdResult();

        ksession.insert(callVM)
        ksession.insert(agreement)
        ksession.insert(result)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
        result.getExposure() == 60000000;
        result.getAboveThreshold();
    }

    def "IM below threshold should be generated"() {
        when: "add a VM call"
        def callVM = new MarginCall(marginType: "Initial",exposure: 20000000,deliverAmount: 50000,returnAmount: 10000,direction: "OUT")
        def agreement = new LocalAgreement(MTA1: 50000,MTA2: 100000,thresholdAmount: 50000000,thresholdTreatment: "Secured",type: "bilateral")
        def result = new ThresholdResult();

        ksession.insert(callVM)
        ksession.insert(agreement)
        ksession.insert(result)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
        result.getExposure() == 0;
        !result.getAboveThreshold();
    }
}
