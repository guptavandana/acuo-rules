package com.acuo.rules.assets

import com.acuo.common.model.assets.Assets
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class existSpec extends Specification {

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

    def "One of the two assets is in type cash or equity"() {
        when: "add an asset"
        def asset1 = new Assets(assetId: "a1",type:"cash")
        def asset2 = new Assets(assetId: "a1",type:"bond")
        def exist = new Exist()
        ksession.insert(asset1)
        ksession.insert(asset2)
        ksession.insert(exist)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "we get eligibility set to true"
        exist.doesExist
    }
    def "Neither of the two assets are in type cash or equity"() {
        when: "add an asset"
        def asset1 = new Assets(assetId: "a1",type:"bond")
        def asset2 = new Assets(assetId: "a1",type:"bond")
        def exist = new Exist()
        ksession.insert(asset1)
        ksession.insert(asset2)
        ksession.insert(exist)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "we get eligibility set to true"
        !exist.doesExist
    }
}
