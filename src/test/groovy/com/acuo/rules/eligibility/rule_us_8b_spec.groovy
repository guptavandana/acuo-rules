


package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_us_8b_spec  extends Specification{
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
    def "a bond is in US class 8b"() {
        when: "add a bond asset"
        def asset = new LocalAsset(type: "equity", id: "us8a", index: "asdf,S&P 1500")
        def eligible = new Eligible()
        def regime = new Regime(name:"US")
        ksession.insert(asset)
        ksession.insert(eligible)
        ksession.insert(regime)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US8b"
        eligible.isEligible
    }
}
