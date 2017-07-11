package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_c_spec extends Specification {
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
    def "is cash in EEA class c"() {
        when: "add an bond asset"
        def asset = new LocalAsset(type:"bond", id: "c1", datascopeAssetType:"GOVT", CQS:1,currency:"USD")
        def eligible = new Eligible()
        def issuer = new Issuer(country: "Austria", domCurrency:"GBP")
        def regime = new Regime(name:"EEA")
        ksession.insert(asset)
        ksession.insert(eligible)
        ksession.insert(regime)
        ksession.insert(issuer)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        issuer.isMemberState
        eligible.classType == "EEAc"
        eligible.isEligible
    }
}