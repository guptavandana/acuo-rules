package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_FXHaircut_im_a_spec extends Specification {
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
    def "FX Haircut rule im_a"() {
        when: "add an asset"
        def asset = new LocalAsset(currency: "SGD", id: "ima")
        def agreement = new LocalAgreement(marginType: "Initial",terminateCurrency: "USD")
        def eligible = new Eligible()
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.fxHaircut == 0.08
    }
}
