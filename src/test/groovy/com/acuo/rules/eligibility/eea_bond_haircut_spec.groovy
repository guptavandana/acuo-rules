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

class eea_bond_haircut_spec  extends Specification {
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
    def "a bond has haircut 0.03 in EEA regime"() {
        when: "add a bond asset"
        def asset = new Assets(assetId: "a1",type: "bond",maturityYears: 3,CQS: 3,issuerCountryCode: "FR",issuerSector: "SPRA")
        def eligible = new EligibleResult()
        def provider = new HaircutProvider(name:"EEA")
        ksession.insert(asset)
        ksession.insert(eligible)
        ksession.insert(provider)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "EEAhi"
        eligible.isEligible
        eligible.haircut == 0.03
    }
}
