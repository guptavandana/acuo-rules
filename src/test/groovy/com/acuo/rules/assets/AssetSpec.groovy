package com.acuo.rules.assets

import com.acuo.common.model.agreements.Agreement
import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class AssetSpec extends Specification {

    String ksessionName = "AssetsKS"

    KieSession ksession
    Logger ruleLogger

    def setup() {
        def ks = KieServices.Factory.get()
        def kContainer = ks.getKieClasspathContainer()
        ksession = kContainer.newKieSession(ksessionName)

        ruleLogger = LoggerFactory.getLogger(ksessionName)
        ksession.setGlobal("log", ruleLogger)
    }

    def "should AA assets be eligible"() {
        when: "add an AA asset"
        def asset = new Assets(fitchRating: "AA", assetId: "a1")
        def agreement = new Agreement(id: "a1")
        def eligible = new Eligible()
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "we get eligibility set to true"
        eligible.isEligible
    }

    def "should none AA assets not be eligible"() {
        when: "add a none AA asset"
        def asset = new Assets(fitchRating: "AB", assetId: "a2")
        def eligible = new Eligible()
        ksession.insert(asset)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "we get eligibility set to true"
        !eligible.isEligible
    }
}
