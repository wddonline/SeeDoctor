package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

/**
 * Created by richard on 12/20/16.
 */

/*
{
    "status": 1,
    "msg": "成功",
    "data": {
        "diseaseid": "413",
        "diseasename": "肺炎",
        "introduction": "肺炎是指终末气道、肺泡和肺间质的炎症，可由疾病微生物、理化因素、免疫损伤、过敏及药物所致。细菌性肺炎是最常见的肺炎，也是最常见的感染性疾病之一。日常所讲的肺炎主要是指细菌性感染引起的肺炎，此肺炎也是最常见的一种。在抗生素应用以前，细菌性肺炎对儿童及老年人额健康威胁极大，抗生素的出现及发展曾一度使肺炎病死率明显下降。但近年来，尽管应用强有力的抗生素和有效的疫苗，肺炎总的病死率不再降低，甚至有所上升。",
        "cause": "患肺炎的原因可能是：接触到一些厉害的病菌或病毒身体抵抗力弱，如长期吸烟。上呼吸道感染时，没有正确处理。例如是没有正确地看医生、没有正确地看服药，又或者是滥用止咳药止咳以至痰和菌愈积愈多(Sputumretention)。\r\n如果一年内有多过一次真正的肺炎(一些医生误看X光片或滥用了肺炎的诊继)，原因可能是：身体抵抗力弱(先天性或后天性)气管有异物。尤其是幼童。心肺有其它病变：如癌病、气管扩张、肺尘埃沉着病、没有正确地看医生、没有正确地看服药，又或者是滥用止咳药止咳以至痰和菌愈积愈多(Sputumretention)工作环境有问题。注意改善空气流通、冷气系统。\r\n40岁以上长期吸烟的男性。当然，亦有可能纯粹巧合。",
        "pathogenesis": "",
        "clinical_manifestation": "多数起病急骤，常有受凉淋雨、劳累、病毒感染等诱因，约1/3患病前有上呼吸道感染。病程7~10天。\r\n症状\r\n(一)寒战、高热\r\n典型病例以突然寒战起病，继之高热，体温可高达39℃~40℃，呈稽留热型，常伴有头痛、全身肌肉酸痛，食量减少。抗生素使用后热型可不典型，年老体弱者可仅有低热或不发热。\r\n(二)咳嗽、咳痰\r\n初期为刺激性干咳，继而咳出白色粘液痰或带血丝痰，经1~2天后，可咳出粘液血性痰或铁锈色痰，也可呈脓性痰，进入消散期痰量增多，痰黄而稀薄。\r\n(三)胸痛\r\n多有剧烈侧胸痛，常呈针刺样，随咳嗽或深呼吸而加剧，可放射至肩或腹部。如为下叶肺炎可刺激隔胸膜引起剧烈腹痛，易被误诊为急腹症。\r\n(四)呼吸困难\r\n由于肺实变通气不足、胸痛以及毒血症而引起呼吸困难、呼吸快而浅。病情严重时影响气体交换，使动脉血氧饱和度下降而出现紫绀。\r\n(五)其他症状\r\n少数有恶心、呕吐、腹胀或腹泻等胃肠道症状。严重感染者可出现神志模糊、烦躁、嗜睡、昏迷等。",
        "drugcount": 247,
        "hospitalcount": 1170,
        "doctorcount": 2850,
        "iscollection": 0
    }
}
 */
public class DiseaseDetail {

    public int diseaseid;
    public String diseasename;
    public String introduction;
    public String cause;
    public String clinical_manifestation;
    public String lab_check;
    public String differential_diagnosis;
    public String complication;
    public String prevention;
    public String treatment;
    public int drugcount;

    public DiseaseDetail() {
    }

    public int getDiseaseid() {
        return diseaseid;
    }

    public void setDiseaseid(int diseaseid) {
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
