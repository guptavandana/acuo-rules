package com.acuo.rules.services

import com.google.common.collect.ImmutableList
import groovy.util.logging.Slf4j
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.KieSession
import org.kie.api.runtime.rule.FactHandle

import javax.inject.Inject

import static java.util.stream.Collectors.joining

@Slf4j
abstract class RulesServiceImpl implements RulesService {

	@Inject
	protected KieContainer engine = null

	protected abstract KieSession newSession()

	void fireRules(Object fact) {
		fireRules(ImmutableList.of(fact))
	}

	void fireRules(List<?> facts) {
		KieSession session = newSession()

		try {
			facts.forEach{ session.insert(it) }
			session.fireAllRules()
			if(log.isDebugEnabled()) {
				printFactsMessage(session)
			}
		} finally {
			session.dispose()
		}
	}

	private static void printFactsMessage(KieSession session) {
		String msg = session.getFactHandles().stream()
				.map{ session.getObject(it as FactHandle) }
				.filter{ Objects.nonNull(it) }
				.map{ it.toString() }
				.collect(joining("\n"))
		log.debug("All facts: \n {}" , msg)
	}
}