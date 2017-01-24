package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

/**
 * Created by richard on 12/20/16.
 */

public class DiseaseDetail {

    public String diseaseid;//疾病id
    public String diseasename;//疾病名称
    public String introduction;//疾病简介
    public String cause;//病因
    public String clinical_manifestation;//症状
    public String lab_check;//检查
    public String differential_diagnosis;//鉴别
    public String complication;//并发症
    public String prevention;//预防
    public String treatment;//治疗
    public int drugcount;//药品种类数目

    public DiseaseDetail() {
    }

    public String getDiseaseid() {
        return diseaseid;
    }

    public void setDiseaseid(String diseaseid) {
        this.diseaseid = diseaseid;
    }

    public String getDiseasename() {
        return diseasename;
    }

    public void setDiseasename(String diseasename) {
        this.diseasename = diseasename;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getClinical_manifestation() {
        return clinical_manifestation;
    }

    public void setClinical_manifestation(String clinical_manifestation) {
        this.clinical_manifestation = clinical_manifestation;
    }

    public String getLab_check() {
        return lab_check;
    }

    public void setLab_check(String lab_check) {
        this.lab_check = lab_check;
    }

    public String getDifferential_diagnosis() {
        return differential_diagnosis;
    }

    public void setDifferential_diagnosis(String differential_diagnosis) {
        this.differential_diagnosis = differential_diagnosis;
    }

    public String getComplication() {
        return complication;
    }

    public void setComplication(String complication) {
        this.complication = complication;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public int getDrugcount() {
        return drugcount;
    }

    public void setDrugcount(int drugcount) {
        this.drugcount = drugcount;
    }

    @Override
    public String toString() {
        return "DiseaseDetail{" +
                "diseaseid=" + diseaseid +
                ", diseasename='" + diseasename + '\'' +
                ", introduction='" + introduction + '\'' +
                ", cause='" + cause + '\'' +
                ", clinical_manifestation='" + clinical_manifestation + '\'' +
                ", lab_check='" + lab_check + '\'' +
                ", differential_diagnosis='" + differential_diagnosis + '\'' +
                ", complication='" + complication + '\'' +
                ", prevention='" + prevention + '\'' +
                ", treatment='" + treatment + '\'' +
                ", drugcount=" + drugcount +
                '}';
    }
}
