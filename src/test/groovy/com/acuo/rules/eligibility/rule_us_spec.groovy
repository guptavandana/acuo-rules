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

class rule_us_spec extends Specification{
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
    def "cash is in US class 1"() {
        when: "add a cash asset"
        def asset = new Assets(type: "cash", assetId: "a1",currency: Currency.USD)
        def agreement = new Agreement(id: "ag1",majorCurrency: [Currency.EUR,Currency.USD,Currency.GBP],settlementCurrency: Currency.JPY)
        def eligible = new EligibleResult()
        def methods = new Methods()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(methods)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US1"
        eligible.isEligible
    }
    def "a bond is in US class 2"() {
        when: "add a bond asset"
        def asset = new com.acuo.common.model.assets.Assets( assetId: "a1",assetType: "GOVT")
        def issuer = new Issuer(countryCode: "US" )
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US2"
        eligible.isEligible
    }
    def "a bond is in US class 3"() {
        when: "add a bond asset"
        def asset = new Assets(assetType: "AGNC", assetId: "us3")
        def issuer = new Issuer(countryCode: "US")
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US3"
        eligible.isEligible
    }
    def "a bond is in US class 4"() {
        when: "add a bond asset"
        def asset = new Assets(assetType: "GOVT", assetId: "a1")
        def issuer = new Issuer(countryCode: "GB")
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US4"
        eligible.isEligible
    }
    def "a bond is in US class 6"() {
        when: "add a bond asset"
        def asset = new Assets(type: "bond", assetId: "us6")
        def issuer = new Issuer(sector:"SPRA")
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US6"
        eligible.isEligible
    }
    def "gold is in US class 10"() {
        when: "add a gold asset"
        def asset = new Assets(type: "gold", assetId: "a1")
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(eligible)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US10"
        eligible.isEligible
    }
}
