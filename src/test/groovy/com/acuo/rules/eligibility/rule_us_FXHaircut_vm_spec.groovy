package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_us_FXHaircut_vm_spec extends Specification {
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
    def "FX Haircut rule us vm"() {
        when: "add an asset"

        def asset = new LocalAsset(type: "bond", id: "usvma", currency: "USD")
        def agreement = new LocalAgreement(marginType:"Variation", id: "ag1", majorCurrency: "EUR,USD,GBP",settlementCurrency: "JPY")
        def provider = new HaircutProvider(name:"US")
        ksession.insert(provider)
        def eligible = new Eligible()
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.fxHaircut == 0
    }
}
