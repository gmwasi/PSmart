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
    PatientIdentification _htsPatientIdentification;


    public Diff(SHRMessage fromFile, SHRMessage fromHts) {
        _fromFile = fromFile;
        _fromHts = fromHts;
        _htsHivTestArray = fromHts.getHivTests().toArray(new HIVTest[0]);
        _fileHivTestArray = fromFile.getHivTests().toArray(new HIVTest[0]);
        _fileMotherIdentifierArray = fromFile.getPatientIdentification().getMotherDetail().getMotherIdentifiers().toArray(new MotherIdentifier[0]);
        _htsMotherIdentifierArray = fromHts.getPatientIdentification().getMotherDetail().getMotherIdentifiers().toArray(new MotherIdentifier[0]);
        _fileInternalPatientIdArray = fromFile.getPatientIdentification().getInternalpatientids().toArray(new InternalPatientId[0]);
        _htsInternalPatientIdArray = fromHts.getPatientIdentification().getInternalpatientids().toArray(new InternalPatientId[0]);
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

        List<MotherIdentifier> uniqueMotherIds = new ArrayList<>(hashSet);

        return uniqueMotherIds;
    }

    private List<InternalPatientId> getFinalInternalPatientId(InternalPatientId []fileInternalPatientId, InternalPatientId []htsInternalPatientId) {
        List<InternalPatientId> finalInternalPatientId = new ArrayList<>();
        finalInternalPatientId.addAll(Arrays.asList(fileInternalPatientId));
        finalInternalPatientId.addAll(Arrays.asList(htsInternalPatientId));

        HashSet<InternalPatientId> hashSet = new HashSet(finalInternalPatientId);

        List<InternalPatientId> uniqueInternalPatientIds = new ArrayList<>(hashSet);

        return uniqueInternalPatientIds;
    }



    private PatientIdentification getFinalPatientIdentification() {
        PatientIdentification patientIdentification = _htsPatientIdentification;
        List<MotherIdentifier> motherIdentifiers = getFinalMotherIdentifier(_fileMotherIdentifierArray, _htsMotherIdentifierArray);
        List<InternalPatientId> internalPatientIds = getFinalInternalPatientId(_fileInternalPatientIdArray, _htsInternalPatientIdArray);
        MotherDetail motherDetail = _htsPatientIdentification.getMotherDetail();
        motherDetail.setMotherIdentifiers(motherIdentifiers);
        patientIdentification.setMotherDetail(motherDetail);
        patientIdentification.setInternalpatientids(internalPatientIds);
        return patientIdentification;
    }

    public SHRMessage getFinalShr(){
        SHRMessage finalShr = _fromFile;
        PatientIdentification patientIdentification = getFinalPatientIdentification();
        List<HIVTest> finalHivTest = getFinalHIVTests(_fileHivTestArray, _htsHivTestArray);
        finalShr.setPatientIdentification(patientIdentification);
        finalShr.setHivTests(finalHivTest);
        return finalShr;
    }
    

}
