package org.hisp.dhis.dataintegrity;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import org.hisp.dhis.DhisSpringTest;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.QuarterlyPeriodType;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.programrule.ProgramRule;
import org.hisp.dhis.programrule.ProgramRuleAction;
import org.hisp.dhis.programrule.ProgramRuleActionService;
import org.hisp.dhis.programrule.ProgramRuleActionType;
import org.hisp.dhis.programrule.ProgramRuleService;
import org.hisp.dhis.programrule.ProgramRuleVariable;
import org.hisp.dhis.programrule.ProgramRuleVariableService;
import org.hisp.dhis.programrule.ProgramRuleVariableSourceType;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.trackedentity.TrackedEntityAttributeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DataIntegrityServiceTest
    extends DhisSpringTest
{
    @Autowired
    private DataIntegrityService dataIntegrityService;

    @Autowired
    private DataElementService dataElementService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private OrganisationUnitService organisationUnitService;

    @Autowired
    private OrganisationUnitGroupService organisationUnitGroupService;

    @Autowired
    private ProgramRuleService programRuleService;

    @Autowired
    private ProgramRuleVariableService ruleVariableService;

    @Autowired
    private ProgramRuleActionService ruleActionService;

    @Autowired
    private TrackedEntityAttributeService attributeService;

    @Autowired
    private ProgramService programService;

    private DataElement elementA;
    private DataElement elementB;
    private DataElement elementC;

    private TrackedEntityAttribute attributeA;
    
    private DataElementGroup elementGroupA;
    
    private IndicatorType indicatorTypeA;
    
    private Indicator indicatorA;
    private Indicator indicatorB;
    private Indicator indicatorC;
    
    private IndicatorGroup indicatorGroupA;
    
    private DataSet dataSetA;
    private DataSet dataSetB;
    
    private OrganisationUnit unitA;
    private OrganisationUnit unitB;
    private OrganisationUnit unitC;
    private OrganisationUnit unitD;
    private OrganisationUnit unitE;
    private OrganisationUnit unitF;
    
    private OrganisationUnitGroup unitGroupA;
    private OrganisationUnitGroup unitGroupB;
    private OrganisationUnitGroup unitGroupC;
    private OrganisationUnitGroup unitGroupD;

    private Program programA;
    private Program programB;

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    @Override
    public void setUpTest()
    {
        // ---------------------------------------------------------------------
        // Objects
        // ---------------------------------------------------------------------

        elementA = createDataElement( 'A' );
        elementB = createDataElement( 'B' );
        elementC = createDataElement( 'C' );
        attributeA = createTrackedEntityAttribute( 'A' );

        dataElementService.addDataElement( elementA );
        dataElementService.addDataElement( elementB );
        dataElementService.addDataElement( elementC );
        attributeService.addTrackedEntityAttribute( attributeA );

        indicatorTypeA = createIndicatorType( 'A' );
        
        indicatorService.addIndicatorType( indicatorTypeA );
        
        indicatorA = createIndicator( 'A', indicatorTypeA );
        indicatorB = createIndicator( 'B', indicatorTypeA );
        indicatorC = createIndicator( 'C', indicatorTypeA );
        
        indicatorA.setNumerator( " " );
        indicatorB.setNumerator( "Numerator" );
        indicatorB.setDenominator( "Denominator" );
        indicatorC.setNumerator( "Numerator" );
        indicatorC.setDenominator( "Denominator" );

        indicatorService.addIndicator( indicatorA );
        indicatorService.addIndicator( indicatorB );
        indicatorService.addIndicator( indicatorC );        
        
        unitA = createOrganisationUnit( 'A' );
        unitB = createOrganisationUnit( 'B', unitA );
        unitC = createOrganisationUnit( 'C', unitB );
        unitD = createOrganisationUnit( 'D', unitC );
        unitE = createOrganisationUnit( 'E', unitD );
        unitF = createOrganisationUnit( 'F' );

        organisationUnitService.addOrganisationUnit( unitA );
        organisationUnitService.addOrganisationUnit( unitB );
        organisationUnitService.addOrganisationUnit( unitC );
        organisationUnitService.addOrganisationUnit( unitD ); 
        organisationUnitService.addOrganisationUnit( unitE );
        organisationUnitService.addOrganisationUnit( unitF );
        
        unitA.setParent( unitC );

        organisationUnitService.updateOrganisationUnit( unitA );
        
        dataSetA = createDataSet( 'A', new MonthlyPeriodType() );
        dataSetB = createDataSet( 'B', new QuarterlyPeriodType() );

        dataSetA.addDataSetElement( elementA );
        dataSetA.addDataSetElement( elementB );
        
        dataSetA.getSources().add( unitA );
        unitA.getDataSets().add( dataSetA );
        
        dataSetB.addDataSetElement( elementA );     
        
        dataSetService.addDataSet( dataSetA );
        dataSetService.addDataSet( dataSetB );

        programA = createProgram( 'A' );
        programB = createProgram( 'B' );

        programService.addProgram( programA );
        programService.addProgram( programB );

        // ---------------------------------------------------------------------
        // Groups
        // ---------------------------------------------------------------------

        elementGroupA = createDataElementGroup( 'A' );
        
        elementGroupA.getMembers().add( elementA );
        elementA.getGroups().add( elementGroupA );
        
        dataElementService.addDataElementGroup( elementGroupA );
        
        indicatorGroupA = createIndicatorGroup( 'A' );
        
        indicatorGroupA.getMembers().add( indicatorA );
        indicatorA.getGroups().add( indicatorGroupA );
        
        indicatorService.addIndicatorGroup( indicatorGroupA );
        
        unitGroupA = createOrganisationUnitGroup( 'A' );
        unitGroupB = createOrganisationUnitGroup( 'B' );
        unitGroupC = createOrganisationUnitGroup( 'C' );
        unitGroupD = createOrganisationUnitGroup( 'D' );
        
        unitGroupA.getMembers().add( unitA );
        unitGroupA.getMembers().add( unitB );
        unitGroupA.getMembers().add( unitC );
        unitA.getGroups().add( unitGroupA );
        unitB.getGroups().add( unitGroupA );
        unitC.getGroups().add( unitGroupA );
        
        unitGroupB.getMembers().add( unitA );
        unitGroupB.getMembers().add( unitB );
        unitGroupB.getMembers().add( unitF );
        unitA.getGroups().add( unitGroupB );
        unitB.getGroups().add( unitGroupB );
        unitF.getGroups().add( unitGroupB );
        
        unitGroupC.getMembers().add( unitA );
        unitA.getGroups().add( unitGroupC );
        
        organisationUnitGroupService.addOrganisationUnitGroup( unitGroupA );
        organisationUnitGroupService.addOrganisationUnitGroup( unitGroupB );
        organisationUnitGroupService.addOrganisationUnitGroup( unitGroupC );
        organisationUnitGroupService.addOrganisationUnitGroup( unitGroupD );
    }
    
    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    public void testGetDataElementsWithoutDataSet()
    {
        Collection<DataElement> expected = dataIntegrityService.getDataElementsWithoutDataSet();
        
        assertTrue( equals( expected, elementC ) );
    }

    @Test
    public void testGetDataElementsWithoutGroups()
    {
        Collection<DataElement> expected = dataIntegrityService.getDataElementsWithoutGroups();
        
        assertTrue( message( expected ), equals( expected, elementB, elementC ) );
    }

    @Test
    public void testGetDataElementsAssignedToDataSetsWithDifferentPeriodType()
    {
        Map<DataElement, Collection<DataSet>> expected = dataIntegrityService.getDataElementsAssignedToDataSetsWithDifferentPeriodTypes();
        
        assertEquals( 1, expected.size() );
        assertEquals( elementA, expected.keySet().iterator().next() );
        assertTrue( equals( expected.get( elementA ), dataSetA, dataSetB ) );
    }

    @Test
    public void testGetDataSetsNotAssignedToOrganisationUnits()
    {
        Collection<DataSet> expected = dataIntegrityService.getDataSetsNotAssignedToOrganisationUnits();
        
        assertTrue( message( expected ), equals( expected, dataSetB ) );
    }

    @Test
    public void testGetIndicatorsWithIdenticalFormulas()
    {
        Set<Set<Indicator>> expected = dataIntegrityService.getIndicatorsWithIdenticalFormulas();

        Collection<Indicator> violation = expected.iterator().next();
        
        assertEquals( 1, expected.size());        
        assertEquals( 2, violation.size() );
        assertTrue( violation.contains( indicatorB ) );
        assertTrue( violation.contains( indicatorC ) );
    }

    @Test
    public void testGetIndicatorsWithoutGroups()
    {
        Collection<Indicator> expected = dataIntegrityService.getIndicatorsWithoutGroups();
        
        assertTrue( message( expected ), equals( expected, indicatorB, indicatorC ) );
    }

    @Test
    public void testGetOrganisationUnitsWithCyclicReferences()
    {
        Collection<OrganisationUnit> expected = dataIntegrityService.getOrganisationUnitsWithCyclicReferences();
        
        assertTrue( message( expected ), equals( expected, unitA, unitB, unitC ) );
    }

    @Test
    public void testGetOrphanedOrganisationUnits()
    {
        Collection<OrganisationUnit> expected = dataIntegrityService.getOrphanedOrganisationUnits();
        
        assertTrue( message( expected ), equals( expected, unitF ) );
    }

    @Test
    public void testGetOrganisationUnitsWithoutGroups()
    {
        Collection<OrganisationUnit> expected = dataIntegrityService.getOrganisationUnitsWithoutGroups();
        
        assertTrue( message( expected ), equals( expected, unitD, unitE ) );
    }

    @Test
    public void testGetAllProgramRulesWithNoCondition()
    {
        ProgramRule ruleA = new ProgramRule( "RuleA", "descriptionA", programA, null, null, "true", null );
        ProgramRule ruleB = new ProgramRule( "RuleB", "descriptionB", programA, null, null, "2 > 1", 1 );
        ProgramRule ruleC = new ProgramRule( "RuleC", "descriptionC", programA, null, null, null, 0 );

        ProgramRule ruleD = new ProgramRule( "RuleD", "descriptionD", programB, null, null, "true", null );
        ProgramRule ruleE = new ProgramRule( "RuleE", "descriptionE", programB, null, null, null, 0 );

        int idA = programRuleService.addProgramRule( ruleA );
        int idB = programRuleService.addProgramRule( ruleB );
        int idC = programRuleService.addProgramRule( ruleC );
        int idD = programRuleService.addProgramRule( ruleD );
        int idE = programRuleService.addProgramRule( ruleE );

        Map<Program, Collection<ProgramRule>> collectionMap = dataIntegrityService.getProgramRulesWithNoCondition();

        assertTrue( collectionMap.containsKey( programA ) );

        Collection<ProgramRule> rulesA = collectionMap.get( programA );

        assertEquals( 1, rulesA.size() );
        assertTrue( rulesA.contains( ruleC ) );

        assertTrue( collectionMap.containsKey( programB ) );

        Collection<ProgramRule> rulesB = collectionMap.get( programB );

        assertEquals( 1, rulesB.size() );
        assertTrue( rulesB.contains( ruleE ) );
    }

    @Test
    public void testGetAllProgramRulesWithNoPriority()
    {
        ProgramRule ruleA = new ProgramRule( "RuleA", "descriptionA", programA, null, null, "true", null );
        ProgramRule ruleB = new ProgramRule( "RuleB", "descriptionB", programA, null, null, null, 1 );

        int idA = programRuleService.addProgramRule( ruleA );
        int idC = programRuleService.addProgramRule( ruleB );

        ProgramRuleAction ruleActionA = new ProgramRuleAction();
        ruleActionA.setProgramRule( ruleA );
        ruleActionA.setContent( "content" );
        ruleActionA.setProgramRuleActionType( ProgramRuleActionType.ASSIGN );
        ruleActionService.addProgramRuleAction( ruleActionA );

        ProgramRuleAction ruleActionB = new ProgramRuleAction();
        ruleActionB.setProgramRule( ruleB );
        ruleActionB.setContent( "content" );
        ruleActionB.setProgramRuleActionType( ProgramRuleActionType.ASSIGN );
        ruleActionService.addProgramRuleAction( ruleActionB );

        ruleA.setProgramRuleActions( Sets.newHashSet( ruleActionA ) );
        ruleB.setProgramRuleActions( Sets.newHashSet( ruleActionB ) );
        programRuleService.updateProgramRule( ruleA );
        programRuleService.updateProgramRule( ruleB );

        Map<Program, Collection<ProgramRule>> collectionMap = dataIntegrityService.getProgramRulesWithNoPriority();

        assertTrue( collectionMap.containsKey( programA ) );

        Collection<ProgramRule> rulesA = collectionMap.get( programA );

        assertEquals( 1, rulesA.size() );
        assertTrue( rulesA.contains( ruleA ) );
    }

    @Test
    public void testGetAllProgramRulesWithNoAction()
    {
        ProgramRule ruleA = new ProgramRule( "RuleA", "descriptionA", programA, null, null, "true", null );
        ProgramRule ruleB = new ProgramRule( "RuleB", "descriptionB", programA, null, null, null, 0 );

        int idA = programRuleService.addProgramRule( ruleA );
        int idC = programRuleService.addProgramRule( ruleB );

        ProgramRuleAction ruleActionA = new ProgramRuleAction();
        ruleActionA.setProgramRule( ruleA );
        ruleActionA.setContent( "content" );
        ruleActionA.setProgramRuleActionType( ProgramRuleActionType.CREATEEVENT );
        ruleActionService.addProgramRuleAction( ruleActionA );

        ruleA.setProgramRuleActions( Sets.newHashSet( ruleActionA ) );
        programRuleService.updateProgramRule( ruleA );

        Map<Program, Collection<ProgramRule>> collectionMap = dataIntegrityService.getProgramRulesWithNoAction();

        assertTrue( collectionMap.containsKey( programA ) );

        Collection<ProgramRule> rulesA = collectionMap.get( programA );

        assertEquals( 1, rulesA.size() );
        assertTrue( rulesA.contains( ruleB ) );
    }

    @Test
    public void testGetAllProgramRuleVariablesWithNoDataElement()
    {
        ProgramRuleVariable variableA = new ProgramRuleVariable( "RuleVariableA", programA, ProgramRuleVariableSourceType.DATAELEMENT_CURRENT_EVENT, null, elementA, false, null );
        ProgramRuleVariable variableC = new ProgramRuleVariable( "RuleVariableC", programA, ProgramRuleVariableSourceType.DATAELEMENT_NEWEST_EVENT_PROGRAM, null, null, false, null );

        int idA = ruleVariableService.addProgramRuleVariable( variableA );
        int idC = ruleVariableService.addProgramRuleVariable( variableC );

        Map<Program, Collection<ProgramRuleVariable>> collectionMap = dataIntegrityService.getProgramRuleVariablesWithNoDataElement();

        assertTrue( collectionMap.containsKey( programA ) );

        Collection<ProgramRuleVariable> variables = collectionMap.get( programA );

        assertEquals( 1, variables.size() );
        assertTrue( variables.contains( variableC ) );
    }

    @Test
    public void testGetAllProgramRuleVariablesWithNoAttribute()
    {
        ProgramRuleVariable variableB = new ProgramRuleVariable( "RuleVariableB", programA, ProgramRuleVariableSourceType.TEI_ATTRIBUTE, attributeA, null, true, null );
        ProgramRuleVariable variableD = new ProgramRuleVariable( "RuleVariableD", programA, ProgramRuleVariableSourceType.TEI_ATTRIBUTE, null, null, true, null );

        int idB = ruleVariableService.addProgramRuleVariable( variableB );
        int idD = ruleVariableService.addProgramRuleVariable( variableD );

        Map<Program, Collection<ProgramRuleVariable>> collectionMap = dataIntegrityService.getProgramRuleVariablesWithNoAttribute();

        assertTrue( collectionMap.containsKey( programA ) );

        Collection<ProgramRuleVariable> variables = collectionMap.get( programA );

        assertEquals( 1, variables.size() );
        assertTrue( variables.contains( variableD ) );
    }
}
