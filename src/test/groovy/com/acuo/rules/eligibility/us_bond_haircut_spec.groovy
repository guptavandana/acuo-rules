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

class us_bond_haircut_spec  extends Specification{
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
    def "a bond has haircut 0.02 in US regime"() {
        when: "add a bond asset"
        def asset = new Assets(assetId: "a1",type:"bond",assetType: "GOVT",maturityYears: 3,issuerCountryCode: "US")
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
        eligible.classType == "US2"
        eligible.isEligible
        eligible.haircut == 0.02
    }
}
