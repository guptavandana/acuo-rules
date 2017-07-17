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
    def "a bond has haircut 0.15 in CSA fitch rules"() {
        when: "add a bond asset"
        def asset = new LocalAsset(currency: "USD", id: "csa_fitch_govt",type: "bond",maturityYears: 0.5, fitchRating:"F1+", datascopeAssetType: "GOVT")
        def issuer = new Issuer(countryCode: "ASL")
        def eligible = new Eligible(classType: "")
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.haircut == 0.15
    }
    def "a bond has haircut 0.01 in CSA fitch rules with ccy GBP"() {
        when: "add a bond asset"
        def asset = new LocalAsset(currency: "GBP", id: "csa_fitch_govt",type: "bond",maturityYears: 0.5, fitchRating:"F1+", datascopeAssetType: "GOVT")
        def issuer = new Issuer(countryCode: "ASL")
        def eligible = new Eligible(classType: "")
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.haircut == 0.01
    }

    def "a cash has haircut 0 in CSA fitch rules with ccy GBP"() {
        when: "add a cash asset"
        def asset = new LocalAsset(currency: "GBP", id: "csa_fitch_govt",type: "cash")
        def eligible = new Eligible(classType: "")
        ksession.insert(asset)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.haircut == 0
    }
}