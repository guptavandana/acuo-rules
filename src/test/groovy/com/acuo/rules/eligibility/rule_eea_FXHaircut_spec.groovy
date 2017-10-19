package com.acuo.rules.eligibility

import com.acuo.common.model.agreements.Agreement
import com.acuo.common.model.assets.Assets
import com.opengamma.strata.basics.currency.Currency
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_FXHaircut_spec extends Specification {
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
    def "eea FX Haircut rule im_a"() {
        when: "add an asset"
        def asset = new Assets(currency: Currency.SGD, assetId: "ima")
        def agreement = new Agreement(marginType: "Initial",terminateCurrency: Currency.USD)
        def eligible = new EligibleResult()
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
    def "eea FX Haircut rule im"() {
        when: "add an asset"
        def asset = new Assets(currency: Currency.SGD, assetId: "im")
        def agreement = new Agreement(marginType: "Initial", terminateCurrency: Currency.SGD)
        def eligilbe = new EligibleResult()
        def provider = new HaircutProvider(name:"EEA")
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(provider)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligilbe)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligilbe.fxHaircut == 0
    }
    def "eea FX Haircut rule vm_a"() {
        when: "add an asset"
        def asset = new Assets(type: "bond", assetId: "vma", currency: Currency.SGD)
        def agreement = new Agreement(marginType: "Variation", eligibleCurrency: [Currency.USD])
        def eligible = new EligibleResult()
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
    def "eea FX Haircut rule vm_cash"() {
        when: "add an asset"
        def asset = new Assets(type: "cash", assetId: "vmcash")
        def agreement = new Agreement(marginType: "Variation")
        def eligible = new EligibleResult()
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
        eligible.fxHaircut == 0
    }
}
