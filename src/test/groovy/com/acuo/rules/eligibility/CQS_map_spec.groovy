
package com.acuo.rules.eligibility

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class CQS_map_spec  extends Specification {
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
    def "A bond with fitch rating AA using standardized approach has correspond CQS 1 "() {
        when: "add a bond asset"
        def asset = new LocalAsset(id: "a1",type: "bond",fitchRating: "AA",ratingMethod: "Standard")
        ksession.insert(asset)
        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get CQS"
        asset.CQS == 1
    }

    def "A bond with moody's rating A3 using standardized approach has correspond CQS 2 "() {
        when: "add a bond asset"
        def asset = new LocalAsset(id: "a1",type: "bond",moodyRating: "A3",ratingMethod: "Standard")
        ksession.insert(asset)
        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get CQS"
        asset.CQS == 2
    }
    def "A bond with moody's short term rating P-1 using standardized approach has correspond CQS 1 "() {
        when: "add a bond asset"
        def asset = new LocalAsset(id: "a1",type: "bond",moodyRating: "P-1",ratingMethod: "Standard")
        ksession.insert(asset)
        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get CQS"
        asset.CQS == 1
    }
    def "A bond with S&P long term rating BBB using standardized approach has correspond CQS 3 "() {
        when: "add a bond asset"
        def asset = new LocalAsset(id: "a1",type: "bond",snpRating: "BBB",ratingMethod: "Standard")
        ksession.insert(asset)
        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get CQS"
        asset.CQS == 3
    }
    def "A bond with Moody's long term rating BBa1 using IRB approach has correspond CQS 8 "() {
        when: "add a bond asset"
        def asset = new LocalAsset(id: "a1",type: "bond",moodyRating: "Baa1",ratingMethod: "IRB")
        ksession.insert(asset)
        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get CQS"
        asset.CQS == 8
    }

}
