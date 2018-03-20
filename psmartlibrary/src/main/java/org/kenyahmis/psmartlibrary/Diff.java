package org.kenyahmis.psmartlibrary;

import org.kenyahmis.psmartlibrary.Models.SHR.HIVTest;
import org.kenyahmis.psmartlibrary.Models.SHR.InternalPatientId;
import org.kenyahmis.psmartlibrary.Models.SHR.MotherDetail;
import org.kenyahmis.psmartlibrary.Models.SHR.MotherIdentifier;
import org.kenyahmis.psmartlibrary.Models.SHR.PatientIdentification;
import org.kenyahmis.psmartlibrary.Models.SHR.SHRMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by GMwasi on 3/20/2018.
 */

public class Diff {
    SHRMessage _fromFile;
    SHRMessage _fromHts;
    HIVTest[] _fileHivTestArray;
    HIVTest[] _htsHivTestArray;
    InternalPatientId[] _fileInternalPatientIdArray;
    InternalPatientId[] _htsInternalPatientIdArray;
    MotherIdentifier[] _fileMotherIdentifierArray;
    MotherIdentifier[] _htsMotherIdentifierArray;
    PatientIdentification _filePatientIdentification;
    PatientIdentification _htsPatientIdentification;


    public Diff(SHRMessage fromFile, SHRMessage fromHts) {
        _fromFile = fromFile;
        _fromHts = fromHts;
        _fileHivTestArray = (HIVTest[]) fromFile.getHivTests().toArray();
        _htsHivTestArray = (HIVTest[]) fromHts.getHivTests().toArray();
        _fileMotherIdentifierArray = (MotherIdentifier[]) fromFile.getPatientIdentification().getMotherDetail().getMotherIdentifiers().toArray();
        _htsMotherIdentifierArray = (MotherIdentifier[]) fromHts.getPatientIdentification().getMotherDetail().getMotherIdentifiers().toArray();
        _fileInternalPatientIdArray = (InternalPatientId[]) fromFile.getPatientIdentification().getInternalpatientids().toArray();
        _htsInternalPatientIdArray = (InternalPatientId[]) fromHts.getPatientIdentification().getInternalpatientids().toArray();
        _filePatientIdentification = fromFile.getPatientIdentification();
        _htsPatientIdentification = fromHts.getPatientIdentification();
    }

    private List<HIVTest> getFinalHIVTests(HIVTest []fileHivTests, HIVTest []htsHivTests) {
        List<HIVTest> finalHIVTests = new ArrayList<>();
        finalHIVTests.addAll(Arrays.asList(fileHivTests));
        finalHIVTests.addAll(Arrays.asList(htsHivTests));

        HashSet<HIVTest> hashSet = new HashSet(finalHIVTests);

        List<HIVTest> uniqueTests = new ArrayList<>(hashSet);

        return uniqueTests;
    }

    private List<MotherIdentifier> getFinalMotherIdentifier(MotherIdentifier []fileMotherIdentifier, MotherIdentifier []htsMotherIdentifier) {
        List<MotherIdentifier> finalMotherIdentifier = new ArrayList<>();
        finalMotherIdentifier.addAll(Arrays.asList(fileMotherIdentifier));
        finalMotherIdentifier.addAll(Arrays.asList(htsMotherIdentifier));

        HashSet<MotherIdentifier> hashSet = new HashSet(finalMotherIdentifier);

        List<MotherIdentifier> uniqueTests = new ArrayList<>(hashSet);

        return uniqueTests;
    }

    private List<InternalPatientId> getFinalInternalPatientId(InternalPatientId []fileInternalPatientId, InternalPatientId []htsInternalPatientId) {
        List<InternalPatientId> finalInternalPatientId = new ArrayList<>();
        finalInternalPatientId.addAll(Arrays.asList(fileInternalPatientId));
        finalInternalPatientId.addAll(Arrays.asList(htsInternalPatientId));

        HashSet<InternalPatientId> hashSet = new HashSet(finalInternalPatientId);

        List<InternalPatientId> uniqueTests = new ArrayList<>(hashSet);

        return uniqueTests;
    }



    private PatientIdentification getFinalPatientIdentification(PatientIdentification filePatientIdentification, PatientIdentification htsPatientIdentification) {
        PatientIdentification patientIdentification = _fromHts.getPatientIdentification();
        MotherDetail motherDetail = patientIdentification.getMotherDetail();
        MotherIdentifier[] motherDetails = getFinalMotherIdentifier(_fileMotherIdentifierArray, _htsMotherIdentifierArray).toArray(new MotherIdentifier[0]);
        InternalPatientId[] internalPatientIds = getFinalInternalPatientId(_fileInternalPatientIdArray, _htsInternalPatientIdArray).toArray(new InternalPatientId[0]);
        motherDetail.setMotherIdentifiers(Arrays.asList(motherDetails));
        patientIdentification.setMotherDetail(motherDetail);
        patientIdentification.setInternalpatientids(Arrays.asList(internalPatientIds));
        return null;
    }

    public SHRMessage getFinalShr(){
        SHRMessage finalShr = _fromFile;
        PatientIdentification patientIdentification = getFinalPatientIdentification(_fromFile.getPatientIdentification(), _fromHts.getPatientIdentification());
        HIVTest[] finalHivTest = getFinalHIVTests(_fileHivTestArray, _htsHivTestArray).toArray(new HIVTest[0]);
        finalShr.setPatientIdentification(patientIdentification);
        finalShr.setHivTests(Arrays.asList(finalHivTest));
        return finalShr;
    }
    

}
