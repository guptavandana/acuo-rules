package com.acuo.rules.eligibility

import com.acuo.common.model.assets.Assets
import com.acuo.common.model.agreements.Agreement
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification


class cashVMSpec extends Specification {

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

    def "should cash be eligible"() {
        when: "add an cash asset"
        def asset = new Assets(type: "cash", assetId: "a1")
        def agreement = new Agreement(id:"ag1")
        def eligible = new Eligible()
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "we get eligibility set to true"
        eligible.isEligible
    }

    def "should cash not be eligible"() {
        when: "add a cash asset,IM"
        def asset = new Assets(type: "cash", assetId: "a1")
        def agreement = new Agreement(id: "ag2")
        def eligible = new Eligible()
        ksession.insert(asset)
        ksession.insert(agreement)
        ksession.insert(eligible)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "we get eligibility set to true"
        !eligible.isEligible
    }
}
