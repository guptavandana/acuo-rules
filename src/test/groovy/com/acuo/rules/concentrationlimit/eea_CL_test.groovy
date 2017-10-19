package com.acuo.rules.concentrationlimit

import com.acuo.common.model.assets.Assets
import com.acuo.common.model.margin.MarginCall
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class eea_CL_test extends Specification {
    String ksessionName = "ConcentrationLimitKS"

    KieSession ksession
    Logger ruleLogger
    def setup() {
        def ks = KieServices.Factory.get()
        def kContainer = ks.getKieClasspathContainer()
        ksession = kContainer.newKieSession(ksessionName)

        ruleLogger = LoggerFactory.getLogger(ksessionName)
        ksession.setGlobal("log", ruleLogger)
    }
    def "Input 3 assets"() {
        when: "add an bond asset"
        def asset1 = new Assets(assetId: "a1", eeaClass:"EEAc",issuerGroupId: "group1",issuerId:"i1",issuerCountryCode: "GB")
        def asset2 = new Assets(assetId: "a2", eeaClass:"EEAf",issuerGroupId: "group2",issuerId:"i2",issuerCountryCode: "US")
        def asset3 = new Assets(assetId: "a3", eeaClass:"EEAl",issuerGroupId: "group2",issuerId:"i3",issuerCountryCode: "CA")
        def issuers = new Issuers()
        def marginCall = new MarginCall(id:"mc1",callType: "Initial",totalCallAmount:2000000000)
        def clResult = new com.acuo.rules.eligibility.CLResult(index:-1)
        ksession.insert(asset1)
        ksession.insert(asset2)
        ksession.insert(asset3)
        ksession.insert(issuers)
        ksession.insert(marginCall)
        ksession.insert(clResult)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get rules regime and class"
        asset1.assetId
    }
}
