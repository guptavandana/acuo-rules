package com.acuo.rules.services

import com.acuo.rules.eligibility.EligibleResult
import com.acuo.rules.eligibility.HaircutProvider
import com.acuo.common.model.assets.Assets
import com.acuo.rules.eligibility.RuleList
import com.google.common.collect.ImmutableList
import spock.guice.UseModules
import spock.lang.Specification

import javax.inject.Inject
import javax.inject.Named

@UseModules(RulesModule)
class EligibilityServiceSpec extends Specification {

    @Inject
    @Named("eligibility")
    RulesService service

    def "eligible"() {
        when:
        Assets asset = new Assets()
        asset.setType("cash")
        asset.setAssetId("a1")
        EligibleResult eligible = new EligibleResult()
        HaircutProvider provider = new HaircutProvider()
        provider.setName("EEA")
        RuleList ruleList = new RuleList()

        service.fireRules(ImmutableList.of(asset, eligible, provider, ruleList))

        then:
        eligible != null
    }

}