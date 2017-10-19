package com.acuo.rules.eligibility

import com.acuo.common.model.assets.Assets
import com.acuo.common.model.agreements.Agreement
import com.acuo.common.model.margin.Types
import com.opengamma.strata.basics.currency.Currency
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_spec extends Specification {
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
    def "is cash in EEA class a"() {
        when: "add an cash asset"
        def asset = new Assets(type: "cash", assetId: "a1")
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(eligible)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "EEAa"
        eligible.isEligible
    }
    def "is cash in EEA class b"() {
        when: "add an gold asset"
        def asset = new Assets(type: "gold", assetId: "a1")
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(eligible)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "EEAb"
        eligible.isEligible
    }
    def "is bond in EEA class c"() {
        when: "add an bond asset"
        def asset = new Assets(type:"bond", assetId: "c1", assetType:"GOVT", CQS:1,currency:Currency.AUD,maturityYears: 2)
        def eligible = new EligibleResult()
        def issuer = new Issuer(countryCode: "AT")
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
        def asset = new Assets(type:"bond", assetId: "c1", assetType:"GOVT", fitchRating: "B",ratingMethod: "Standard",currency:Currency.AUD,maturityYears: 2)
        def eligible = new EligibleResult()
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
    def "is a bond in EEA class hi"() {
        when: "add an bond asset"
        def asset = new Assets(type: "bond", assetId: "h1")
        def eligible = new EligibleResult()
        def issuer = new Issuer(sector:"SPRA")
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
        eligible.classType == "EEAhi"
        eligible.isEligible
    }
    def "is cash in EEA class j"() {
        when: "add an bond asset"
        def asset = new Assets(type: "bond", assetId: "j1", assetType: "GOVT", CQS:1)
        def eligible = new EligibleResult()
        def issuer = new Issuer(countryCode: "CNA")
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
        eligible.classType == "EEAj"
        eligible.isEligible
    }
    def "is a bond in EEA class n"() {
        when: "add a bond asset"
        def asset = new Assets(type: "bond", assetId: "n1", assetType: "CORP",CQS:1)
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "EEAn"
        eligible.isEligible
    }
}
