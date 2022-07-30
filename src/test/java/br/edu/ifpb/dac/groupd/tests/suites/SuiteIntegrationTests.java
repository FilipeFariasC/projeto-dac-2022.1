package br.edu.ifpb.dac.groupd.tests.suites;

import org.junit.platform.commons.annotation.Testable;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.edu.ifpb.dac.groupd.tests.integration.IntegrationTestsSuite;

@Suite
@Testable
//@SelectPackages({"br.edu.ifpb.dac.groupd.tests.unittest"})
@SelectClasses(IntegrationTestsSuite.class)
@SuiteDisplayName("Suite de Testes de Integração")
@ActiveProfiles("test")
@Profile("test")
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration()
public class SuiteIntegrationTests {}
