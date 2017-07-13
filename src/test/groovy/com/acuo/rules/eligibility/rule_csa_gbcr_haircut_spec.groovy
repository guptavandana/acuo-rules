package com.acuo.rules.eligibility

import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_csa_gbcr_haircut_spec  extends Specification {
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
    def "a bond has haircut 0.075 in CSA fitch rules"() {
        when: "add a bond asset"
        def asset = new LocalAsset(id: "csa_fitch_govt",type: "bond",maturityYears: 9, fitchRating:"A+", datascopeAssetType: "GOVT")
        def issuer = new Issuer(country: "Australia")
        def eligible = new Eligible(classType: "")
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.haircut == 0.075
    }
}