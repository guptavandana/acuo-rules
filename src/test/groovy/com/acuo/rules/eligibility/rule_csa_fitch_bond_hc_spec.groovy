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
        def asset = new Assets(type: "bond", assetId: "a1",currency:Currency.AUD,maturityYears: 0.5,fitchRating: "AAA",assetType: "GOVT",issuerCountryCode: "AU",issuerSector: "SOVERGRN")
        def agreement = new Agreement(id: "ag1", baseCurrency: Currency.GBP, majorCurrency: [Currency.EUR,Currency.USD,Currency.GBP])
        def haircutProvider = new HaircutProvider(name: "Fitch")
        def counterpart = new Counterpart(fitchRating: "AA",countryCode:"UK")
        def eligible = new EligibleResult()
        def rulelist = new RuleList()
        ksession.insert(rulelist)
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)
        ksession.insert(haircutProvider)
        ksession.insert(counterpart)

        and: "we fire all rules"
        //ksession.getAgenda().getAgendaGroup( "FitchHaircut" ).setFocus();
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        Math.round(eligible.haircut*100000)/100000 == 1-0.99*0.86
    }
}
