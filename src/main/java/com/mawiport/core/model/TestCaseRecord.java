package com.mawiport.core.model;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentRowHeight;
import cn.idev.excel.annotation.write.style.HeadRowHeight;
import com.mawiport.core.annotation.GherkinMap;
import lombok.Data;

@Data
@ContentRowHeight(25)
@HeadRowHeight(30)
public class TestCaseRecord {
    @ExcelProperty("TC_ID")
    @ColumnWidth(18)
    @GherkinMap(sourceKey = "TC_ID", required = true)
    private String tcId;

    @ExcelProperty("Feature")
    @ColumnWidth(30)
    @GherkinMap(sourceKey = "FEATURE_NAME", required = true)
    private String feature;

    @ExcelProperty("Type")
    @ColumnWidth(12)
    @GherkinMap(sourceKey = "TAG_TYPE")
    private String type;

    @ExcelProperty("Priority")
    @ColumnWidth(10)
    @GherkinMap(sourceKey = "TAG_PRIORITY")
    private String priority;

    @ExcelProperty("Rule")
    @ColumnWidth(30)
    @GherkinMap(sourceKey = "RULE_NAME")
    private String rule;

    @ExcelProperty("Title")
    @ColumnWidth(45)
    @GherkinMap(sourceKey = "SCENARIO_NAME", required = true)
    private String title;

    @ExcelProperty("Precondition (Given)")
    @ColumnWidth(50)
    @GherkinMap(sourceKey = "GIVEN_STEPS")
    private String preconditions;

    @ExcelProperty("Test Steps (When/And)")
    @ColumnWidth(50)
    @GherkinMap(sourceKey = "WHEN_STEPS")
    private String steps;

    @ExcelProperty("Test Data")
    @ColumnWidth(40)
    @GherkinMap(sourceKey = "TEST_DATA")
    private String testData;

    @ExcelProperty("Expected Result (Then/And)")
    @ColumnWidth(50)
    @GherkinMap(sourceKey = "THEN_STEPS")
    private String expectedResults;

    @ExcelProperty("Tag 1")
    @ColumnWidth(15)
    @GherkinMap(sourceKey = "TAG_1")
    private String tag1;

    @ExcelProperty("Tag 2")
    @ColumnWidth(15)
    @GherkinMap(sourceKey = "TAG_2")
    private String tag2;
}