
package com.acuo.rules.eligibility

import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_csa_moody_haircut_spec  extends Specification {
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
    def "a cash has haircut 0 in CSA moody rules"() {
        when: "add a cash asset"
        def asset = new LocalAsset(currency: "GBP", id: "csa_moody_cash",type: "cash")
        def issuer = new Issuer()
        def eligible = new Eligible()
        def haircutProvider = new HaircutProvider(name: "Moody")
        def agreement = new LocalAgreement(trigger: 1)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)
        ksession.insert(haircutProvider)
        ksession.insert(agreement)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        eligible.valuationPercentage == 1
        eligible.haircut == 0
    }
    def "a bond has haircut 0.02 in CSA moody rules with ccy USD"() {
        when: "add a bond asset"
        def asset = new LocalAsset(currency: "USD", id: "csa_moody_govt",type: "bond",maturityYears: 0.5, fitchRating:"F1+", rateType: "fix")
        def issuer = new Issuer(name: "U.S. Treasury Department")
        def eligible = new Eligible()
        def haircutProvider = new HaircutProvider(name: "Moody")
        def agreement = new LocalAgreement(trigger: 1)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)
        ksession.insert(haircutProvider)
        ksession.insert(agreement)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        eligible.valuationPercentage == 0.98
        Math.round(eligible.haircut*100000)/100000 == 0.02
    }
    def "a bond has haircut 0.02 in CSA moody rules with government agency"() {
        when: "add a bond asset"
        def asset = new LocalAsset(currency: "USD", id: "csa_moody_govt",type: "bond",maturityYears: 0.5, rateType: "fix")
        def issuer = new Issuer(name: "Department of Transportation")
        def eligible = new Eligible()
        def haircutProvider = new HaircutProvider(name: "Moody")
        def agreement = new LocalAgreement(trigger: 1)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)
        ksession.insert(haircutProvider)
        ksession.insert(agreement)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        eligible.valuationPercentage == 0.98
        Math.round(eligible.haircut*100000)/100000 == 0.02
    }
    def "a bond has haircut 0.01 in CSA moody rules with moody rating"() {
        when: "add a bond asset"
        def asset = new LocalAsset(datascopeAssetType: "GOVT", currency: "EUR", id: "csa_moody_govt",type: "bond",maturityYears: 0.5, rateType: "fix", moodyRating: "Aa1")
        def issuer = new Issuer()
        def eligible = new Eligible()
        def haircutProvider = new HaircutProvider(name: "Moody")
        def agreement = new LocalAgreement(trigger: 1)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)
        ksession.insert(haircutProvider)
        ksession.insert(agreement)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        eligible.valuationPercentage == 0.99
        Math.round(eligible.haircut*100000)/100000 == 0.01
    }
    def "a bond has haircut 0 in CSA moody rules with UK"() {
        when: "add a bond asset"
        def asset = new LocalAsset(datascopeAssetType: "GOVT", currency: "GBP", id: "csa_moody_govt",type: "bond",maturityYears: 0.5, rateType: "fix")
        def issuer = new Issuer(countryCode: "UK")
        def eligible = new Eligible()
        def haircutProvider = new HaircutProvider(name: "Moody")
        def agreement = new LocalAgreement(trigger: 1)
        ksession.insert(asset)
        ksession.insert(issuer)
        ksession.insert(eligible)
        ksession.insert(haircutProvider)
        ksession.insert(agreement)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        eligible.isEligible
        eligible.valuationPercentage == 1
        eligible.haircut == 0
    }
}