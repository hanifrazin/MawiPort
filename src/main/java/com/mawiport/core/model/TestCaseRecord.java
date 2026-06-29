package com.mawiport.core.model;

import com.mawiport.core.annotation.GherkinMap;

public class TestCaseRecord {
    @GherkinMap(sourceKey = "TC_ID", order = 0, headerName = "Test Case ID", optional = false)
    private String testCaseId;
    
    @GherkinMap(sourceKey = "SCENARIO_NAME", order = 1, headerName = "Scenario", optional = false)
    private String description;

    @GherkinMap(sourceKey = "GIVEN_STEPS", order = 2, headerName = "Precondition", optional = false)
    private String precondition;
    
    @GherkinMap(sourceKey = "WHEN_STEPS", order = 3, headerName = "Test Steps", optional = false)
    private String testSteps;

    @GherkinMap(sourceKey = "TEST_DATA", order = 4, headerName = "Test Data", optional = true)
    private String testData;

    @GherkinMap(sourceKey = "THEN_STEPS", order = 5, headerName = "Expected Result", optional = false)
    private String expectedResult;

    @GherkinMap(sourceKey = "", order = 6, headerName = "Actual Result", width = 20, optional = false)
    private String actualResult;

    @GherkinMap(sourceKey = "TAG_PRIORITY", order = 7, headerName = "Priority", optional = false)
    private String priority;

    @GherkinMap(sourceKey = "TAG_SEVERITY", order = 8, headerName = "Severity", optional = false)
    private String severity;

    @GherkinMap(sourceKey = "TAG_PLATFORM", order = 9, headerName = "Platform", optional = false)
    private String platformTest;

    @GherkinMap(sourceKey = "TAG_ENVIRONMENT", order = 10, headerName = "Environment", optional = false)
    private String envTest;

    @GherkinMap(sourceKey = "TAG_TYPE", order = 11, headerName = "Type of Testing", optional = false)
    private String typeTest;

    @GherkinMap(sourceKey = "TAG_PHASE", order = 12, headerName = "Phase of Testing", optional = false)
    private String phaseTest;

    @GherkinMap(sourceKey = "TAG_EXECUTE", order = 13, headerName = "Execute of Testing", optional = false)
    private String executeTest;

    @GherkinMap(sourceKey = "TAG_VALID", order = 14, headerName = "Valid of Testing", optional = false)
    private String validTest;

    @GherkinMap(sourceKey = "TAG_1", order = 15, headerName = "Additional Tag 1", optional = false)
    private String additionalTag1;

    @GherkinMap(sourceKey = "TAG_2", order = 16, headerName = "Additional Tag 2", optional = false)
    private String additionalTag2;

    // Constructors, getters, and setters
    public TestCaseRecord() {
    }

    public TestCaseRecord(String testCaseId, String description, String precondition, String testSteps, String testData, String expectedResult, String actualResult, String priority, String severity, String platformTest, String envTest, String typeTest, String phaseTest, String executeTest, String validTest, String additionalTag1, String additionalTag2) {
        this.testCaseId = testCaseId;
        this.description = description;
        this.precondition = precondition;
        this.testSteps = testSteps;
        this.testData = testData;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
        this.priority = priority;
        this.severity = severity;
        this.platformTest = platformTest;
        this.envTest = envTest;
        this.typeTest = typeTest;
        this.phaseTest = phaseTest;
        this.executeTest = executeTest;
        this.validTest = validTest;
        this.additionalTag1 = additionalTag1;
        this.additionalTag2 = additionalTag2;
    }

    // Getters and setters
    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(String testSteps) {
        this.testSteps = testSteps;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPlatformTest() {
        return platformTest;
    }

    public void setPlatformTest(String platform) {
        this.platformTest = platform;
    }

    public String getEnvTest() {
        return envTest;
    }

    public void setEnvTest(String envTest) {
        this.envTest = envTest;
    }

    public String getTypeTest() {
        return typeTest;
    }

    public void setTypeTest(String typeTest) {
        this.typeTest = typeTest;
    }

    public String getPhaseTest() {
        return phaseTest;
    }

    public void setPhaseTest(String phaseTest) {
        this.phaseTest = phaseTest;
    }

    public String getExecuteTest() {
        return executeTest;
    }

    public void setExecuteTest(String executeTest) {
        this.executeTest = executeTest;
    }

    public String getValidTest() {
        return validTest;
    }

    public void setValidTest(String validTest) {
        this.validTest = validTest;
    }

    public String getAdditionalTag1() {
        return additionalTag1;
    }

    public void setAdditionalTag1(String additionalTag1) {
        this.additionalTag1 = additionalTag1;
    }

    public String getAdditionalTag2() {
        return additionalTag2;
    }

    public void setAdditionalTag2(String additionalTag2) {
        this.additionalTag2 = additionalTag2;
    }
}