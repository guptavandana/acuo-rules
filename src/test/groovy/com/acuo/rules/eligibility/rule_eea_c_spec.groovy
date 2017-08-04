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
    def "is bond in EEA class c"() {
        when: "add an bond asset"
        def asset = new LocalAsset(type:"bond", id: "c1", datascopeAssetType:"GOVT", CQS:1,currency:"AUD",maturityYears: 2)
        def eligible = new Eligible()
        def issuer = new Issuer(countryCode: "AU",sector:"SOVERGRN")
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(eligible)
        ksession.insert(issuer)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        issuer.isMemberState
        eligible.classType == "EEAc"
        eligible.isEligible
    }
    def "An AU bond with fitch rating B is not in EEA class c"() {
        when: "add an bond asset"
        def asset = new LocalAsset(type:"bond", id: "c1", datascopeAssetType:"GOVT", fitchRating: "B",ratingMethod: "Standard",currency:"AUD",maturityYears: 2)
        def eligible = new Eligible()
        def issuer = new Issuer(countryCode: "AU",sector:"SOVERGRN")
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(eligible)
        ksession.insert(issuer)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        //issuer.isMemberState
        //eligible.classType == "EEAc"
        !eligible.isEligible
    }
}
