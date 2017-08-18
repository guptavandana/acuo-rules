package com.acuo.rules.helloworld

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class cheeseTest extends Specification {
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
            def person = new Person(age:1)
            ksession.insert(person)
            ksession.fireAllRules()

            then:
            person.age == 1
        }
    }

