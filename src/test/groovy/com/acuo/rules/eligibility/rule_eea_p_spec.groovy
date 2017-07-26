package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_p_spec extends Specification {
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
    def "is cash in EEA class p"() {
        when: "add an bond asset"
        def asset = new LocalAsset(type: "bond", id: "p1", convertibleFlag: true, convertibleTypeCode: "EI", index: "S&P 500,asdf", exchange:"asdf,XPAR")
        def eligible = new Eligible()
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "EEAp"
        eligible.isEligible
        eligible.haircut == 0.15
    }
}
