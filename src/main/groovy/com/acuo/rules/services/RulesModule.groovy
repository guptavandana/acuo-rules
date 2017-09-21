package com.acuo.rules.services

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.name.Names
import org.kie.api.KieServices
import org.kie.api.runtime.KieContainer

class RulesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RulesService.class).annotatedWith(Names.named("eligibility")).to(EligibilityService.class)
    }

    @Provides
    KieContainer createKieContainer() {
        KieServices kieServices = KieServices.Factory.get()
        return kieServices.getKieClasspathContainer()
    }
}