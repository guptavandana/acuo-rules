package com.acuo.rules.eligibility

import com.acuo.common.model.agreements.Agreement
import com.acuo.common.model.assets.Assets
import com.opengamma.strata.basics.currency.Currency
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_us_FXHaircut_spec extends Specification {
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
    def "FX Haircut rule us_im_a"() {
        when: "add an asset"
        def asset = new Assets(currency: Currency.SGD, assetId: "usima")
        def agreement = new Agreement(marginType: "Initial",terminateCurrency: Currency.USD, settlementCurrency: Currency.JPY)
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
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
    def "FX Haircut rule us_im"() {
        when: "add an asset"
        def asset = new Assets(currency: Currency.SGD, assetId: "usim")
        def agreement = new Agreement(marginType: "Initial",terminateCurrency: Currency.SGD, settlementCurrency: Currency.SGD)
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.fxHaircut == 0
    }
    def "FX Haircut rule us vm_a"() {
        when: "add an asset"
        def asset = new Assets(type: "bond", assetId: "usvma", currency: Currency.CNY)
        def agreement = new Agreement(marginType:"Variation", id: "ag1",majorCurrency: [Currency.EUR,Currency.USD,Currency.GBP],settlementCurrency: Currency.JPY)
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"US")
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
    def "FX Haircut rule us vm"() {
        when: "add an asset"

        def asset = new Assets(type: "bond", assetId: "usvma", currency: Currency.USD)
        def agreement = new Agreement(marginType:"Variation", id: "ag1",majorCurrency: [Currency.EUR,Currency.USD,Currency.GBP],settlementCurrency: Currency.JPY)
        def provider = new HaircutProvider(name:"US")
        ksession.insert(provider)
        def eligible = new EligibleResult()
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.fxHaircut == 0
    }
}
