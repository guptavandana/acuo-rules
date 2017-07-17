package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification


class rule_us_FXHaircut_vm_a_spec extends Specification {
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
    def "FX Haircut rule us vm_a"() {
        when: "add an asset"
        def asset = new LocalAsset(type: "bond", id: "usvma", currency: "RMB")
        def agreement = new LocalAgreement(marginType:"Variation", id: "ag1", majorCurrency: "EUR,USD,GBP",settlementCurrency: "JPY")
        def regime = new Regime(name: "US")
        def eligible = new Eligible()
        ksession.insert(asset)
        ksession.insert(regime)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.fxHaircut == 0.08
    }
}
