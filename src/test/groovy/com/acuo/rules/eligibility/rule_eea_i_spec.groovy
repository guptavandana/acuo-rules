package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_i_spec extends Specification {
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
    def "a bond is in EEA class i"() {
        when: "add a bond asset"
        def asset = new LocalAsset(type: "bond", id: "a1")
        def issuer = new Issuer(name: "European Union")
        def eligible = new Eligible()
        def regime = new Regime(name:"EEA")
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)
        ksession.insert(regime)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        issuer.isInterOrg
        eligible.classType == "EEAi"
        eligible.isEligible
    }
}
