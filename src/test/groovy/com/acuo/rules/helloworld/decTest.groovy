package com.acuo.rules.helloworld

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class decTest extends Specification {
    String ksessionName = "HelloWorldKS"

    KieSession ksession
    Logger ruleLogger

    def setup() {
        def ks = KieServices.Factory.get()
        def kContainer = ks.getKieClasspathContainer()
        ksession = kContainer.newKieSession(ksessionName)

        ruleLogger = LoggerFactory.getLogger(ksessionName)
        ksession.setGlobal("log", ruleLogger)
    }
    def "decision table"() {
        when:
        def product1 = new Product(type:"gold")
        def product2 = new Product(type:"diamond")
        ksession.insert(product1)
        ksession.insert(product2)
        ksession.fireAllRules()

        then:
        product1.discount == 25
        product2.discount == 15
    }
}
