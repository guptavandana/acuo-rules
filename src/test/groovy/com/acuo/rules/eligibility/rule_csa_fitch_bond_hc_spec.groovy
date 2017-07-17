package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_csa_fitch_bond_hc_spec extends Specification {
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

    def "An Australian Bond will be applied 1-0.795 haircut."() {
        when: "add a bond asset"
        def asset = new LocalAsset(type: "bond", id: "a1",currency:"AUD",maturityYears: 0.5,fitchRating: "AAA",datascopeAssetType: "GOVT")
        def issuer = new Issuer(countryCode: "AU",legalEntityType: "GVT")
        def agreement = new LocalAgreement(id: "ag1", baseCurrency: "GBP", majorCurrency: "EUR,USD,GBP")
        def haircutProvider = new HaircutProvider(name: "Fitch")
        def counterpart = new Counterpart(fitchRating: "AA")
        def eligible = new Eligible()
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(agreement)
        ksession.insert(eligible)
        ksession.insert(haircutProvider)
        ksession.insert(counterpart)

        and: "we fire all rules"
        ksession.getAgenda().getAgendaGroup( "FitchHaircut" ).setFocus();
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.securityAR == 0.99
        eligible.FXAR == 0.86
        eligible.isEligible
        Math.round(eligible.haircut*100000)/100000 == 1-0.99*0.86
    }
}
