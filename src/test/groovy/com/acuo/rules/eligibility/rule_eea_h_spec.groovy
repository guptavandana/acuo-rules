package com.acuo.rules.eligibility

import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_h_spec extends Specification {
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
    def "is cash in EEA class h"() {
        when: "add an bond asset"
        def asset = new LocalAsset(type: "bond", assetId: "h1")
        def eligible = new Eligible()
        def regime = new Regime(name:"EEA")
        def issuer = new Issuer(name: "Nordic Investment Bank")
        ksession.insert(asset)
        ksession.insert(eligible)
        ksession.insert(regime)
        ksession.insert(issuer)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        issuer.isMultiDevBank
        eligible.classType == "EEAh"
        eligible.isEligible
    }
}
