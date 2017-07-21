package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class us_simpleclass_haircut_spec  extends Specification  {
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
    def "cash has haircut 0 in US regime"() {
        when: "add an cash asset"
        def asset = new LocalAsset(type: "cash", id: "a1",currency: "GBP")
        def agreement = new LocalAgreement(id: "ag1", majorCurrency: "EUR,USD,GBP",settlementCurrency: "JPY")
        def eligible = new Eligible()
        def methods = new Methods()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)
        ksession.insert(methods)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US1"
        eligible.isEligible
        eligible.haircut == 0
    }
    def "gold has haircut 0.15 in US regime"() {
        when: "add an gold asset"
        def asset = new LocalAsset(type: "gold", id: "a1")
        def eligible = new Eligible()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(asset)
        ksession.insert(eligible)
        ksession.insert(provider)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        eligible.classType == "US10"
        eligible.haircut == 0.15
    }
}
