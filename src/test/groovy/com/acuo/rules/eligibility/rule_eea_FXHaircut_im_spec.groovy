package com.acuo.rules.eligibility

import com.acuo.common.model.agreements.Agreement
import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class rule_eea_FXHaircut_im_spec extends Specification {
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
    def "FX Haircut rule im"() {
        when: "add an asset"
        def asset = new LocalAsset(currency: "SGD", assetId: "im")
        def agreement = new LocalAgreement(marginType: "Initial", terminateCurrency: "SGD")
        def regime = new Regime(name:"EEA")
        ksession.insert(asset)
        ksession.insert(regime)
        ksession.insert(agreement)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        asset.FXHaircut == 0
    }
}
