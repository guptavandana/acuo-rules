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

class LoopHaircurProvider_test extends Specification  {
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
    def "multiple regime and provider with UK high rating CTPY"() {
        when: "add an bond asset"

        def provider = new HaircutProvider(name: "Fitch,US,Moody,EEA")
        def rulelist = new RuleList()
        def asset = new Assets(type:"bond", assetId: "c1", assetType:"GOVT", CQS:1,currency:Currency.EUR,maturityYears: 0.5,fitchRating: "AAA", rateType: "fix", moodyRating: "Aa1",issuerCountryCode: "AT",issuerSector: "SPRA")
        def eligible = new EligibleResult()
        def agreement = new Agreement(id: "ag1", baseCurrency: Currency.GBP, majorCurrency: [Currency.EUR,Currency.USD,Currency.GBP], trigger: 1,marginType: "Initial",terminateCurrency: Currency.USD)
        def counterpart = new Counterpart(fitchRating: "AA")

        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(counterpart)
        ksession.insert(provider)
        ksession.insert(rulelist)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        rulelist.provider == "Fitch"
        eligible.isEligible
        Math.round(eligible.haircut*100000)/100000 == 1-0.99*0.86
        eligible.fxHaircut == 0
        //eligible.haircut ==0
        //rulelist.listHaircut==0
        //rulelist.listLength==0
        //rulelist.listname==0
        //rulelist.listIndex == 0
        //rulelist.lengthRegime == 0
        //rulelist.startSignal==0
    }

    def "multiple regime and provider with US low rating CTPY"() {
        when: "add an bond asset"

        def provider = new HaircutProvider(name: "Fitch,US,Moody,EEA")
        def rulelist = new RuleList()
        def asset = new Assets(type:"bond", assetId: "c1", assetType:"GOVT", CQS:1,currency:Currency.EUR,maturityYears: 0.5,fitchRating: "AAA", rateType: "fix", moodyRating: "Aa1",issuerCountryCode: "US",issuerSector: "SOVERGRN")
        def eligible = new EligibleResult()
        def agreement = new Agreement(id: "ag1", baseCurrency: Currency.GBP, majorCurrency: [Currency.EUR,Currency.USD,Currency.GBP], trigger: 1,marginType: "Initial",terminateCurrency: Currency.USD)
        def counterpart = new Counterpart(fitchRating: "BBB",countryCode: "US")

        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(counterpart)
        ksession.insert(provider)
        ksession.insert(rulelist)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        //regime.name == "US"
        //rulelist.listFXHaircut == 0
        //provider.name == "Fitch"
        rulelist.provider == "Fitch"
        eligible.isEligible
        Math.round(eligible.haircut*100000)/100000 == 1-0.98*0.905
        eligible.fxHaircut == 0
        //eligible.haircut ==0
        //rulelist.listHaircut==0
        //rulelist.listLength==0
        //rulelist.listname==0
        //rulelist.listIndex == 0
        //rulelist.lengthRegime == 0
        //rulelist.startSignal==0
    }

}
