package com.acuo.rules.assets

import com.acuo.common.model.assets.Assets
import com.acuo.common.model.margin.Types
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import java.lang.reflect.Type

class arrayTest extends Specification {

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
        def array = new Array(currencies: [Currency.getInstance("USD"),Currency.getInstance("GBP")],
                currency: Currency.getInstance("JPY"))
        ksession.insert(array)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "we get eligibility set to true"
        array.currencies
    }
}
