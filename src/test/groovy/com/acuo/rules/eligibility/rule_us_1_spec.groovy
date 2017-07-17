package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_us_1_spec  extends Specification{
    String ksessionName = "EligibilityKS"

    KieSession ksession
    Logger ruleLogger

    def setup() {
        def ks = KieServices.Factory.get()
        def kContainer = ks.getKieClasspathContainer()
        ksession = kContainer.newKieSession(ksessionName)

        ruleLogger = LoggerFactory.getLogger(ksessionName)
        ksession.setGlobal("log", ruleLogger)
    }
    def "cash is in US class 1"() {
        when: "add a cash asset"
        def asset = new LocalAsset(type: "cash", id: "a1",currency:"USD")
        def agreement = new LocalAgreement(id: "ag1", majorCurrency: "EUR,USD,GBP",settlementCurrency: "JPY")
        def eligible = new Eligible()
        def methods = new Methods()
        def regime = new Regime(name:"US")
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(methods)
        ksession.insert(eligible)
        ksession.insert(regime)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US1"
        eligible.isEligible
    }
}
