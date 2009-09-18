/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * 
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.drugref.ca.dpd.fetch;

import java.io.InputStream;
import org.drugref.ca.dpd.CdVeterinarySpecies;
import com.Ostermiller.util.CSVParser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.drugref.ca.dpd.CdActiveIngredients;
import org.drugref.ca.dpd.CdCompanies;
import org.drugref.ca.dpd.CdDrugProduct;
import org.drugref.ca.dpd.CdDrugStatus;
import org.drugref.ca.dpd.CdForm;
import org.drugref.ca.dpd.CdPackaging;
import org.drugref.ca.dpd.CdPharmaceuticalStd;
import org.drugref.ca.dpd.CdRoute;
import org.drugref.ca.dpd.CdSchedule;
import org.drugref.ca.dpd.CdTherapeuticClass;

/**
 *
 * @author jaygallagher
 */
public class RecordParser {


    static public String memString(){
         // Get current size of heap in bytes
    long heapSize = Runtime.getRuntime().totalMemory();

    // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
    // Any attempt will result in an OutOfMemoryException.
    long heapMaxSize = Runtime.getRuntime().maxMemory();

    // Get amount of free memory within the heap in bytes. This size will increase
    // after garbage collection and decrease as new objects are created.
    long heapFreeSize = Runtime.getRuntime().freeMemory();
        String ret = heapSize+"/"+heapMaxSize+" :"+heapFreeSize;
        return ret;
    }

    static public String looksLike(String[] s){
        StringBuffer sb = new StringBuffer();
        if (s==null) return "";
        int count=0;
        for (String str:s){
            sb.append(count+".");
            sb.append(str+"-- ");
            count++;
        }
        sb.append(memString());
        return sb.toString();
//        return "";
    }

    static public Date getDate(String s) throws Exception{
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
        Date date = (Date)formatter.parse(s);
        return date;
    }

    public static Object getDPDObject(String type,InputStream is,EntityManager em)throws Exception{
        CSVParser csv = new CSVParser(is);
        String[] items = null;
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        if("vet.txt".equals(type)){
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdVeterinarySpecies vet = new CdVeterinarySpecies();
                vet.setDrugCode(new Integer(items[0]));
                vet.setVetSpecies(items[1]);
                vet.setVetSubSpecies(items[2]);
                em.persist(vet);
                em.flush();
                em.clear();
                vet =null;
            }
        }else if("comp.txt".equals(type)){
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdCompanies vet = new CdCompanies();
                /*
                 DRUG_CODE                              NOT NULL  NUMBER(8)
                 MFR_CODE                                         VARCHAR2(5)
                 COMPANY_CODE                                     NUMBER(6)
                 COMPANY_NAME                                     VARCHAR2(80)
                 COMPANY_TYPE                                     VARCHAR2(40)
                 ADDRESS_MAILING_FLAG				  VARCHAR2(1)
                 ADDRESS_BILLING_FLAG				  VARCHAR2(1)
                 ADDRESS_NOTIFICATION_FLAG			  VARCHAR2(1)
                 ADDRESS_OTHER					  VARCHAR2(1)
                 SUITE_NUMBER                                     VARCHAR2(20)
                 STREET_NAME                                      VARCHAR2(80)
                 CITY_NAME                                        VARCHAR2(60)
                 PROVINCE                                         VARCHAR2(40)
                 COUNTRY                                          VARCHAR2(40)
                 POSTAL_CODE                                      VARCHAR2(20)
                 POST_OFFICE_BOX                                  VARCHAR2(15)
                 */
                vet.setDrugCode(new Integer(items[0]));
                vet.setMfrCode(items[1]);
                vet.setCompanyCode(new Integer(items[2]));
                vet.setCompanyName(items[3]);
                vet.setCompanyType(items[4]);
                vet.setAddressMailingFlag(items[5]);//.charAt(0));
                vet.setAddressBillingFlag(items[6]);//.charAt(0));
                vet.setAddressNotificationFlag(items[7]);//.charAt(0));
                vet.setAddressOther(items[8]);
                vet.setSuiteNumber(items[9]);
                vet.setStreetName(items[10]);
                vet.setCityName(items[11]);
                vet.setProvince(items[12]);
                vet.setCountry(items[13]);
                vet.setPostalCode(items[14]);
                vet.setPostOfficeBox(items[15]);

                System.out.println("addrBillingFlag>"+vet.getAddressBillingFlag()+"<");

                em.persist(vet);
                em.flush();
                em.clear();
                vet =null;
            }
        }else if("drug.txt".equals(type)){
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdDrugProduct prod = new CdDrugProduct();
                /*
                 DRUG_CODE                              NOT NULL  NUMBER(8)
                 PRODUCT_CATEGORIZATION                           VARCHAR2(80)
                 CLASS                                            VARCHAR2(40)
                 DRUG_IDENTIFICATION_NUMBER                       VARCHAR2(8)
                 BRAND_NAME                                       VARCHAR2(200)
                 PEDIATRIC_FLAG                                   VARCHAR2(1)
                 ACCESSION_NUMBER                                 VARCHAR2(5)
                 NUMBER_OF_AIS                                    VARCHAR2(10)
                 LAST_UPDATE_DATE                                 DATE
                 AI_GROUP_NO					  VARCHAR2(10)
                 */
                prod.setDrugCode(new Integer(items[0]));
                prod.setProductCategorization(items[1]);
                prod.setClass1(items[2]);
                prod.setCompanyCode(0);
                prod.setDrugIdentificationNumber(items[3]);
                prod.setBrandName(items[4]);
                prod.setGpFlag(items[5]);//.charAt(0));
                prod.setAccessionNumber(items[6]);
                prod.setNumberOfAis(items[7]);
                prod.setLastUpdateDate(getDate(items[8]));
                prod.setAiGroupNo(items[9]);
                em.persist(prod);
                em.flush();
                em.clear();
                prod =null;

            }
        }else if("form.txt".equals(type)){
            /*
             DRUG_CODE                                      NOT NULL  NUMBER(8)
             PHARM_FORM_CODE                                NUMBER(7)
             PHARMACEUTICAL_FORM                            VARCHAR2(40)
             */
             while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdForm vet = new CdForm();
                vet.setDrugCode(new Integer(items[0]));
                vet.setPharmCdFormCode(new Integer((items[1])));
                vet.setPharmaceuticalCdForm(items[2]);
                em.persist(vet);
                em.flush();
                em.clear();
                vet =null;
             }
        }else if("ingred.txt".equals(type)){
            /*
             DRUG_CODE                                NOT NULL NUMBER(8)
             ACTIVE_INGREDIENT_CODE                   NUMBER(6)
             INGREDIENT                               VARCHAR2(240)
             INGREDIENT_SUPPLIED_IND                  VARCHAR2(1)
             STRENGTH                                 VARCHAR2(20)
             STRENGTH_UNIT                            VARCHAR2(40)
             STRENGTH_TYPE                            VARCHAR2(40)
             DOSAGE_VALUE                             VARCHAR2(20)
             BASE                                     VARCHAR2(1)
             DOSAGE_UNIT                              VARCHAR2(40)
             NOTES                                    VARCHAR2(2000)
            */
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdActiveIngredients vet = new CdActiveIngredients();
                vet.setDrugCode(new Integer(items[0]));
                vet.setActiveIngredientCode(new Integer((items[1])));
                vet.setIngredient(items[2]);
                vet.setIngredientSuppliedInd(items[3]);//.charAt(0));
                vet.setStrength(items[4]);
                vet.setStrengthUnit(items[5]);
                vet.setStrengthType(items[6]);
                vet.setDosageValue(items[7]);
                vet.setBase(items[8]);//.charAt(0));
                vet.setDosageUnit(items[9]);
                vet.setNotes(items[10]);
                em.persist(vet);
                em.flush();
                em.clear();
                vet =null;
            }
        }else if("package.txt".equals(type)){
            /*
             DRUG_CODE                              NOT NULL  NUMBER(8)
             UPC                                              VARCHAR2(12)
             PACKAGE_SIZE_UNIT                                VARCHAR2(40)
             PACKAGE_TYPE                                     VARCHAR2(40)
             PACKAGE_SIZE                                     VARCHAR2(5)
             PRODUCT_INFORMATION                              VARCHAR2(80)
             */
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdPackaging vet = new CdPackaging();
                vet.setDrugCode(new Integer(items[0]));
                vet.setUpc(items[1]);
                vet.setPackageSizeUnit(items[2]);
                vet.setPackageType(items[3]);
                vet.setPackageSize(items[4]);
                vet.setProductInforation(items[5]);
                em.persist(vet);
                em.flush();
                em.clear();
             }
        }else if("pharm.txt".equals(type)){
            /*
             DRUG_CODE                              NOT NULL  NUMBER(8)
             PHARMACEUTICAL_STD                               VARCHAR2(40)
             */
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdPharmaceuticalStd vet = new CdPharmaceuticalStd();
                vet.setDrugCode(new Integer(items[0]));
                vet.setPharmaceuticalStd(items[1]);
                em.persist(vet);
                em.flush();
                em.clear();
             }
        }else if("route.txt".equals(type)){
            /*
            DRUG_CODE                              NOT NULL  NUMBER(8)
            ROUTE_OF_ADMINISTRATION_CODE		 	  NUMBER(6)
            ROUTE_OF_ADMINISTRATION                          VARCHAR2(40)
             */
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdRoute vet = new CdRoute();
                vet.setDrugCode(new Integer(items[0]));
                vet.setRouteOfAdministrationCode(new Integer(items[1]));
                vet.setRouteOfAdministration(items[2]);
                em.persist(vet);
                em.flush();
                em.clear();
             }

        }else if("schedule.txt".equals(type)){
            /*
             DRUG_CODE                              NOT NULL  NUMBER(8)
             SCHEDULE                                         VARCHAR2(40)
             */
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdSchedule vet = new CdSchedule();
                vet.setDrugCode(new Integer(items[0]));
                vet.setSchedule(items[1]);
                em.persist(vet);
                em.flush();
                em.clear();
             }
        }else if("status.txt".equals(type)){
            /*
            DRUG_CODE                              NOT NULL  NUMBER(8)
            CURRENT_STATUS_FLAG                              VARCHAR2(1)
            STATUS                                           VARCHAR2(40)
            HISTORY_DATE                                     DATE

             */
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdDrugStatus vet = new CdDrugStatus();
                vet.setDrugCode(new Integer(items[0]));
                vet.setCurrentStatusFlag(items[1]);//.charAt(0));
                vet.setStatus(items[2]);
                vet.setHistoryDate(getDate(items[3]));
                em.persist(vet);
                em.flush();
                em.clear();
             }
        }else if("ther.txt".equals(type)){
            /*
             DRUG_CODE                              NOT NULL  NUMBER(8)
             TC_ATC_NUMBER                                    VARCHAR2(8)
             TC_ATC                                           VARCHAR2(120)
             TC_AHFS_NUMBER                                   VARCHAR2(20)
             TC_AHFS                                          VARCHAR2(80)
             */
            //0.64437-- 1.C03EA01-- 2.HYDROCHLOROTHIAZIDE AND POTASSIUM-SPARING AGENTS-- 3.24:08.24.16-- 4.POTASSIUM-SPARING DIURETICS-- 420909056/1068302336 :56619472
            while (( items = csv.getLine()) != null) {
                System.out.println(looksLike(items));
                CdTherapeuticClass vet = new CdTherapeuticClass();
                vet.setDrugCode(new Integer(items[0]));
                vet.setTcAtcNumber(items[1]);
                vet.setTcAtc(items[2]);
                vet.setTcAhfsNumber(items[3]);
                vet.setTcAhfs(items[4]);
                em.persist(vet);
                em.flush();
                em.clear();
             }
        }
        tx.commit();

        return null;
    }


}