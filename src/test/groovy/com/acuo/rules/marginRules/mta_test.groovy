package com.acuo.rules.marginRules

import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class mta_test extends Specification {
    String ksessionName = "MarginRulesKS"

    KieSession ksession
    Logger ruleLogger

    def setup() {
        def ks = KieServices.Factory.get()
        def kContainer = ks.getKieClasspathContainer()
        ksession = kContainer.newKieSession(ksessionName)

        ruleLogger = LoggerFactory.getLogger(ksessionName)
        ksession.setGlobal("log", ruleLogger)
    }
    def "VM above MTA should be generated"() {
        when: "add a VM call"
        def callVM = new MarginCall(marginType: "Variation",deliverAmount: 50000,returnAmount: 10000,direction: "OUT")
        def agreement = new LocalAgreement(MTA1: 50000,MTA2: 100000,type: "bilateral")
        def marginResult = new MarginResult();

        ksession.insert(callVM)
        ksession.insert(agreement)
        ksession.insert(marginResult)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
        marginResult.getMarginAmountVM() == 60000;
        marginResult.getDirectionVM() == "OUT";
    }

    def "IM below MTA should not be generated"() {
        when: "add an IM call"
        def callIM = new MarginCall(marginType: "Initial",deliverAmount: 50000,returnAmount: 10000,direction: "IN")
        def agreement = new LocalAgreement(MTA1: 50000,MTA2: 100000,type: "bilateral")
        def marginResult = new MarginResult();

        ksession.insert(callIM)
        ksession.insert(agreement)
        ksession.insert(marginResult)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
        marginResult.getMarginAmountIM() == 0;
    }

    def "IM and VM with the same direction sum above MTA should be generated"() {
        when: "add a VM and an IM call"
        def callVM = new MarginCall(marginType: "Variation",deliverAmount: 50000,returnAmount: 10000,direction: "IN")
        def callIM = new MarginCall(marginType: "Initial",deliverAmount: 50000,returnAmount: 20000,direction: "IN")
        def agreement = new LocalAgreement(MTA1: 50000,MTA2: 100000,type: "legacy")
        def marginResult = new MarginResult();

        ksession.insert(callVM)
        ksession.insert(callIM)
        ksession.insert(agreement)
        ksession.insert(marginResult)


        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
        marginResult.getDirectionIM() == "IN";
        marginResult.getDirectionVM() == "IN";
        marginResult.getDirectionNet() == "IN";
        marginResult.getMarginAmountNet() == 130000;
    }

    def "IM and VM with different directions difference above MTA should be generated"() {
        when: "add a VM and an IM call"
        def callVM = new MarginCall(marginType: "Variation",deliverAmount: 50000,returnAmount: 10000,direction: "OUT")
        def callIM = new MarginCall(marginType: "Initial",deliverAmount: 150000,returnAmount: 20000,direction: "IN")
        def agreement = new LocalAgreement(MTA1: 50000,MTA2: 100000,type: "legacy")
        def marginResult = new MarginResult();

        ksession.insert(callVM)
        ksession.insert(callIM)
        ksession.insert(agreement)
        ksession.insert(marginResult)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
        marginResult.getDirectionIM() == "IN";
        marginResult.getDirectionVM() == "OUT";
        marginResult.getDirectionNet() == "IN";
        marginResult.getMarginAmountNet() == 110000;
    }

    def "IM and VM with different directions difference below MTA should not be generated"() {
        when: "add a VM and an IM call"
        def callVM = new MarginCall(marginType: "Variation",deliverAmount: 50000,returnAmount: 10000,direction: "OUT")
        def callIM = new MarginCall(marginType: "Initial",deliverAmount: 10000,returnAmount: 20000,direction: "IN")
        def agreement = new LocalAgreement(MTA1: 50000,MTA2: 100000,type: "legacy")
        def marginResult = new MarginResult();

        ksession.insert(callVM)
        ksession.insert(callIM)
        ksession.insert(agreement)
        ksession.insert(marginResult)

        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get margin calls result"
        marginResult.getMarginAmountNet() == 0;
    }
}
