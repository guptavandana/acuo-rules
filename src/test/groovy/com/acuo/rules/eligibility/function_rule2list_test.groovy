package com.acuo.rules.eligibility


import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class function_rule2list_test extends Specification  {
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
    def "regime and provider"() {
        when: "add an bond asset"

        def provider = new HaircutProvider(name: "Fitch,US,Moody,EEA")
        def rulelist = new RuleList()
        def asset = new LocalAsset(type:"bond", id: "c1", datascopeAssetType:"GOVT", CQS:1,currency:"EUR",maturityYears: 0.5,fitchRating: "AAA", rateType: "fix", moodyRating: "Aa1")
        def issuer = new Issuer(countryCode: "AU", domCurrency:"GBP",name: "UNITED STATES TREASURY",legalEntityType: "GVT")
        def eligible = new Eligible()
        def agreement = new LocalAgreement(id: "ag1", baseCurrency: "GBP", majorCurrency: "EUR,USD,GBP", trigger: 1,marginType: "Initial",terminateCurrency: "USD")
        def counterpart = new Counterpart(fitchRating: "AA")



        ksession.insert(asset)
        ksession.insert(issuer)
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
        provider.name == "Fitch"
        eligible.isEligible == true
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

}
