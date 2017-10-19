
package com.acuo.rules.eligibility

import com.opengamma.strata.basics.currency.Currency
import com.acuo.common.model.assets.Assets
import com.opengamma.strata.basics.currency.Currency
import org.kie.api.KieServices
import org.kie.api.runtime.KieSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

class countryCurrency_test extends Specification {
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
    def "Issuer country US domiciled currency is USD "() {
        when: "add a bond asset"
        def issuer = new Issuer(countryCode: "US")
        ksession.insert(issuer)
        and: "we fire all rules"
        ksession.fireAllRules()

        then: "then we get currency"
        issuer.domCurrency == Currency.USD

    }

}
