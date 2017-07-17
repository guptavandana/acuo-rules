package com.acuo.rules.eligibility

import com.acuo.common.model.agreements.Agreement
import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_FXHaircut_vm_a_spec extends Specification {
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
    def "FX Haircut rule vm_a"() {
        when: "add an asset"
        def asset = new LocalAsset(type: "bond", id: "vma", currency: "SGD")
        def agreement = new LocalAgreement(marginType: "Variation", eligibleCurrency: "USD")
        def regime = new Regime(name:"EEA")
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
