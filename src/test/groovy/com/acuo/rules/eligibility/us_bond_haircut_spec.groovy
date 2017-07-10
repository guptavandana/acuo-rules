package com.acuo.rules.eligibility

import com.acuo.common.model.assets.Assets
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
        def asset = new LocalAsset(id: "a1",type: "bond",maturityYears: 3)
        def issuer = new Issuer(name: "UNITED STATES TREASURY")
        def eligible = new Eligible()
        def regime = new Regime(name:"US")
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)
        ksession.insert(regime)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.classType == "US2"
        eligible.isEligible
        eligible.haircut == 0.02
    }
}
