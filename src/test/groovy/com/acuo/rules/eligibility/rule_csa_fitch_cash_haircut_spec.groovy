package com.acuo.rules.eligibility

import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_csa_fitch_cash_haircut_spec extends Specification {
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

    def "A cash asset will be applied 0 haircut."() {
        when: "add a bond asset"
        def asset = new LocalAsset(type: "cash", id: "a1",currency:"EUR")
        def agreement = new LocalAgreement(id: "ag1", baseCurrency: "GBP", eligibleCurrency: "EUR,USD,GBP", majorCurrency: "EUR,USD,GBP")
        def haircutProvider = new HaircutProvider(name: "Fitch")
        def methods = new Methods()
        def eligible = new Eligible()
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)
        ksession.insert(methods)
        ksession.insert(haircutProvider)

        and: "we fire all rules"
        ksession.getAgenda().getAgendaGroup( "FitchHaircut" ).setFocus();
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        eligible.fxHaircut == 0
        Math.round(eligible.haircut*10000)/10000 == 0
    }
}
