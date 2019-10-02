/
    create table accountstack (
        ACSTACKID number(10,0) not null,
        ACCOUNTNR varchar2(255 char),
        ACCOUNT_TYPE number(10,0),
        ACCOUNT_TAX_TYPE varchar2(255 char),
        ACCOUNT_DEBIT varchar2(255 char),
        ACCOUNT_CREDIT varchar2(255 char),
        ACCOUNT_D_NAME varchar2(255 char),
        ACCOUNT_C_NAME varchar2(255 char),
        ACCOUNT_C_VALUE double precision,
        ACCOUNT_D_VALUE double precision,
        ACCOUNTNAME varchar2(255 char),
        ACCOUNTVALUE double precision,
        DESCRIPTION long,
        ACCOUNTDATE date,
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (ACSTACKID)
    )
/
    create table accountstackcd (
        ACCOUNTSTACKCDID number(10,0) not null,
        ACCOUNTSTACKID number(10,0),
        CREDITDEBITNUMBER varchar2(150 char),
        CREDITDEBITNAME varchar2(255 char),
        CREDITDEBITVALUE double precision,
        CREDITDEBITTAXTNAME varchar2(150 char),
        CREDITDEBITTYPE number(10,0),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        primary key (ACCOUNTSTACKCDID)
    )
/
    create table accountstackdocs (
        ACCOUNTDOCID number(10,0) not null,
        ACCOUNTID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        primary key (ACCOUNTDOCID)
    )
/
    create table company (
        COMPANYID number(10,0) not null,
        COMPANYNUMBER number(10,0),
        CUSTOMERNR varchar2(255 char),
        BEGINCHAR varchar2(10 char),
        NAME varchar2(255 char),
        NAME2 varchar2(255 char),
        PHONE varchar2(50 char),
        FAX varchar2(50 char),
        EMAIL varchar2(50 char),
        EMPLOYEE varchar2(100 char),
        QUALIFICATION varchar2(100 char),
        CATEGORY varchar2(150 char),
        COOPERATION varchar2(150 char),
        ISLOCK number(1,0),
        WEB varchar2(150 char),
        TAXNUMBER varchar2(50 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        ISACTUAL number(1,0),
        primary key (COMPANYID)
    )
/
    create table companyactivities (
        ACTIVITYID number(10,0) not null,
        COMPANYID number(10,0),
        ACTIVITYNAME varchar2(150 char),
        ACTIVITYTYPE varchar2(50 char),
        ACTIVITYSTATUS varchar2(50 char),
        DUEDATE timestamp,
        DURATION number(10,0),
        ACOLOR varchar2(18 char),
        ACTIVITYCLOSEDATE date,
        ACTIVITYISCLOSED number(1,0),
        ACTIVITYDESCRIPTION long,
        ISLOCK number(1,0),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (ACTIVITYID)
    )
/
    create table companyactivitiesdocs (
        ACTIVITYDOCID number(10,0) not null,
        ACTIVITYID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        primary key (ACTIVITYDOCID)
    )
/
    create table companyactivitystatus (
        ID number(10,0) not null,
        NAME varchar2(255 char),
        primary key (ID)
    )
/
    create table companyactivitytype (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyaddress (
        ADDRESSID number(10,0) not null,
        COMPANYID number(10,0),
        ADDRESSTYPE varchar2(150 char),
        STREET varchar2(50 char),
        ZIP varchar2(10 char),
        LOCATION varchar2(50 char),
        PBOX varchar2(150 char),
        COUNTRY varchar2(150 char),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (ADDRESSID)
    )
/
    create table companybank (
        BANKID number(10,0) not null,
        COMPANYID number(10,0),
        BANKNAME varchar2(255 char),
        BANKBSB varchar2(255 char),
        BANKACCOUNT varchar2(255 char),
        BANKBIC varchar2(255 char),
        BANKIBAN varchar2(255 char),
        BANKCOUNTRY varchar2(150 char),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (BANKID)
    )
/
    create table companycategory (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyclassification (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companycontactaddress (
        ADDRESSID number(10,0) not null,
        CONTACTID number(10,0),
        ADDRESSTYPE varchar2(150 char),
        STREET varchar2(80 char),
        ZIP varchar2(20 char),
        LOCATION varchar2(150 char),
        COUNTRY varchar2(150 char),
        PBOX varchar2(150 char),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (ADDRESSID)
    )
/
    create table companycontacts (
        CONTACTID number(10,0) not null,
        COMPANYID number(10,0),
        MAINCONTACT number(1,0),
        GENDER varchar2(20 char),
        TITLE varchar2(255 char),
        SURNAME varchar2(150 char),
        NAME varchar2(150 char),
        MITTELNAME varchar2(150 char),
        POSITION varchar2(150 char),
        BIRDDATE date,
        PHONE varchar2(30 char),
        FAX varchar2(30 char),
        MOBILE varchar2(30 char),
        EMAIL varchar2(50 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (CONTACTID)
    )
/
    create table companycooperation (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyhirarchie (
        HIERARCHIEID number(10,0) not null,
        COMPANYID number(10,0),
        NAME varchar2(255 char),
        ROOT number(10,0),
        PARENT number(10,0),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (HIERARCHIEID)
    )
/
    create table companymeetingcontacts (
        MEETINGCONTACTID number(10,0) not null,
        MEETINGID number(10,0),
        POS number(10,0),
        GENDER varchar2(20 char),
        SURNAME varchar2(150 char),
        NAME varchar2(150 char),
        MITTELNAME varchar2(150 char),
        POSITION varchar2(150 char),
        BIRDDATE date,
        PHONE varchar2(30 char),
        FAX varchar2(30 char),
        MOBILE varchar2(30 char),
        EMAIL varchar2(50 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date not null,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (MEETINGCONTACTID)
    )
/
    create table companymeetingdoc (
        MEETINGDOCID number(10,0) not null,
        MEETINGID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        primary key (MEETINGDOCID)
    )
/
    create table companymeetingprotocol (
        MEETINGPROTOCOLID number(10,0) not null,
        COMPANYID number(10,0),
        MEETINGSUBJECT varchar2(255 char),
        MEETINGTYPE varchar2(150 char),
        PROTOCOL long,
        METINGDATE date,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (MEETINGPROTOCOLID)
    )
/
    create table companymeetingtype (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companynumber (
        ID number(10,0) not null,
        CATEGORYID number(10,0),
        CATEGORY varchar2(255 char),
        BEGINCHAR varchar2(10 char),
        NUMBERFROM number(10,0),
        NUMBERTO number(10,0),
        primary key (ID)
    )
/
    create table companyoffer (
        OFFERID number(10,0) not null,
        COMPANYID number(10,0),
        OPPORTUNITYID number(10,0),
        OFFERNR varchar2(255 char) not null,
        NAME varchar2(255 char),
        STATUS varchar2(100 char),
        OFFERVERSION number(10,0),
        OFFERDATE date,
        VALIDTO date,
        ISRECIEVED number(1,0),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (OFFERID)
    )
/
    create table companyofferdocs (
        OFFERDOCID number(10,0) not null,
        OFFERID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        primary key (OFFERDOCID)
    )
/
    create table companyofferpositions (
        POSITIONID number(10,0) not null,
        OFFERID number(10,0),
        PRODUCTID number(10,0),
        PRODUCTNR varchar2(255 char),
        DEDUCTION varchar2(5 char),
        PRODUCTNAME varchar2(255 char),
        QUANTITY number(19,0),
        NETAMOUNT double precision not null,
        PRETAX double precision not null,
        TAXTYPE varchar2(150 char),
        TYPE varchar2(255 char),
        CATEGORY varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        primary key (POSITIONID)
    )
/
    create table companyofferreceiver (
        RECEIVERID number(10,0) not null,
        OFFERID number(10,0),
        CNUM number(10,0),
        RECEIVERVIA varchar2(150 char),
        GENDER varchar2(20 char),
        SURNAME varchar2(150 char),
        MITTELNAME varchar2(150 char),
        POSITION varchar2(150 char),
        EMAIL varchar2(50 char),
        PHONE varchar2(30 char),
        FAX varchar2(30 char),
        STREET varchar2(150 char),
        ZIP varchar2(15 char),
        LOCATION varchar2(80 char),
        PBOX varchar2(100 char),
        COUNTRY varchar2(100 char),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        NAME varchar2(150 char),
        primary key (RECEIVERID)
    )
/
    create table companyofferstatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyopportunity (
        OPPORTUNITYID number(10,0) not null,
        COMPANYID number(10,0),
        NAME varchar2(255 char),
        SALESTAGE varchar2(100 char),
        PROBABILITY varchar2(100 char),
        OPPORTUNITYVALUE double precision,
        ISCLOSE number(1,0),
        CLOSEDATE date,
        BUSINESSTYPE varchar2(150 char),
        EVALUATIONSTATUS varchar2(150 char),
        EVALUETIONDATE date,
        BUDGETSTATUS varchar2(150 char),
        BUDGETDATE date,
        SALESTAGEDATE date,
        OPPORTUNITYSTATUS varchar2(150 char),
        OPPORTUNITYSTATUSDATE date,
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (OPPORTUNITYID)
    )
/
    create table companyopportunitybgstatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyopportunitybustyp (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyopportunitycontact (
        OPPORTUNITYCONTACTID number(10,0) not null,
        OPPORTUNITYID number(10,0),
        POS number(10,0),
        GENDER varchar2(20 char),
        SURNAME varchar2(150 char),
        NAME varchar2(150 char),
        MITTELNAME varchar2(150 char),
        POSITION varchar2(150 char),
        BIRDDATE date,
        PHONE varchar2(30 char),
        FAX varchar2(30 char),
        MOBILE varchar2(30 char),
        EMAIL varchar2(50 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (OPPORTUNITYCONTACTID)
    )
/
    create table companyopportunityevstatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyopportunitysstage (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyopportunitystatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyopporunitydocs (
        DOCID number(10,0) not null,
        OPPORTUNITYID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        primary key (DOCID)
    )
/
    create table companyorder (
        ORDERID number(10,0) not null,
        COMPANYID number(10,0),
        ORDERNR varchar2(150 char),
        OFFERID number(10,0),
        NAME varchar2(255 char),
        INVOICECREATED number(1,0),
        STATUS varchar2(150 char),
        ORDERVERSION number(10,0),
        OFFERDATE date,
        VALIDTO date,
        ISRECIEVED number(1,0),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (ORDERID)
    )
/
    create table companyorderdocs (
        ORDERDOCID number(10,0) not null,
        ORDERID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        primary key (ORDERDOCID)
    )
/
    create table companyorderpositions (
        POSITIONID number(10,0) not null,
        ORDERID number(10,0),
        PRODUCTID number(10,0),
        DEDUCTION varchar2(5 char),
        PRODUCTNR varchar2(255 char),
        PRODUCTNAME varchar2(255 char),
        QUANTITY number(19,0),
        NETAMOUNT double precision,
        PRETAX double precision,
        TAXTYPE varchar2(150 char),
        TYPE varchar2(255 char),
        CATEGORY varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        primary key (POSITIONID)
    )
/
    create table companyorderreceiver (
        RECEIVERID number(10,0) not null,
        ORDERID number(10,0),
        CNUM number(10,0),
        RECEIVERVIA varchar2(50 char),
        GENDER varchar2(20 char),
        SURNAME varchar2(150 char),
        NAME varchar2(150 char),
        MITTELNAME varchar2(150 char),
        POSITION varchar2(150 char),
        EMAIL varchar2(50 char),
        PHONE varchar2(30 char),
        FAX varchar2(30 char),
        STREET varchar2(50 char),
        ZIP varchar2(15 char),
        LOCATION varchar2(100 char),
        PBOX varchar2(100 char),
        COUNTRY varchar2(100 char),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (RECEIVERID)
    )
/
    create table companyorderstatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyproductcategory (
        ID number(10,0) not null,
        NAME varchar2(255 char),
        primary key (ID)
    )
/
    create table companyproducttax (
        ID number(10,0) not null,
        NAME varchar2(255 char),
        TAXVALUE double precision,
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        primary key (ID)
    )
/
    create table companyproducttaxvalue (
        ID number(10,0) not null,
        NAME varchar2(255 char),
        primary key (ID)
    )
/
    create table companyproducttype (
        ID number(10,0) not null,
        NAME varchar2(255 char),
        primary key (ID)
    )
/
    create table companyservice (
        SERVICEID number(10,0) not null,
        COMPANYID number(10,0),
        SERVICENR varchar2(150 char),
        NAME varchar2(255 char),
        CATEGORY varchar2(255 char),
        TYPE varchar2(255 char),
        STATUS varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (SERVICEID)
    )
/
    create table companyservicecategory (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyservicedocs (
        SERVICEDOCID number(10,0) not null,
        SERVICEID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        primary key (SERVICEDOCID)
    )
/
    create table companyservicepositions (
        POSITIONID number(10,0) not null,
        SERVICEID number(10,0),
        PRODUCTID number(10,0),
        DEDUCTION varchar2(5 char),
        PRODUCTNR varchar2(255 char),
        PRODUCTNAME varchar2(255 char),
        QUANTITY number(19,0),
        NETAMOUNT double precision,
        PRETAX double precision,
        TAXTYPE varchar2(150 char),
        TYPE varchar2(255 char),
        CATEGORY varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        primary key (POSITIONID)
    )
/
    create table companyservicepsol (
        PROSOLID number(10,0) not null,
        SERVICEID number(10,0),
        SOLUTIONNR varchar2(150 char),
        NAME varchar2(255 char),
        CLASSIFICATION varchar2(255 char),
        CATEGORY varchar2(255 char),
        TYPE varchar2(255 char),
        STATUS varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (PROSOLID)
    )
/
    create table companyservicestatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table companyservicetype (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crmaddresstype (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crmcalendar (
        CALENDARID number(10,0) not null,
        CUSER varchar2(150 char),
        NAME varchar2(255 char),
        COLOR varchar2(15 char),
        DESCRIPTION long,
        STARTDATE timestamp,
        ENDDATE timestamp,
        CICON raw(255),
        primary key (CALENDARID)
    )
/
    create table crmcampaign (
        CAMPAIGNID number(10,0) not null,
        CAMPAIGNNR varchar2(255 char),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        NAME varchar2(255 char),
        STATUS varchar2(255 char),
        VALIDFROM date,
        VALIDTO date,
        primary key (CAMPAIGNID)
    )
/
    create table crmcampaigndocs (
        DOCID number(10,0) not null,
        CAMPAIGNID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        primary key (DOCID)
    )
/
    create table crmcampaignposition (
        POSITIONID number(10,0) not null,
        CAMPAIGNID number(10,0),
        PRODUCTID number(10,0),
        PRODUCTNR varchar2(255 char),
        DEDUCTION varchar2(5 char),
        PRODUCTNAME varchar2(255 char),
        QUANTITY number(19,0),
        NETAMOUNT double precision not null,
        PRETAX double precision not null,
        TAXTYPE varchar2(150 char),
        TYPE varchar2(255 char),
        CATEGORY varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        primary key (POSITIONID)
    )
/
    create table crmcampaignprop (
        PROPERTIESID number(10,0) not null,
        CAMPAIGNID number(10,0),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        NAME varchar2(255 char),
        VALUE varchar2(255 char),
        primary key (PROPERTIESID)
    )
/
    create table crmcampaignprops (
        ID number(10,0) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    )
/
    create table crmcampaignreceiver (
        RECEIVERID number(10,0) not null,
        CAMPAIGNID number(10,0),
        CNUM number(10,0),
        RECEIVERVIA varchar2(50 char),
        COMPANYNAME varchar2(255 char),
        COMPANYNUMBER varchar2(255 char),
        GENDER varchar2(20 char),
        SURNAME varchar2(150 char),
        NAME varchar2(150 char),
        MITTELNAME varchar2(150 char),
        POSITION varchar2(150 char),
        EMAIL varchar2(50 char),
        PHONE varchar2(30 char),
        FAX varchar2(30 char),
        STREET varchar2(50 char),
        ZIP varchar2(15 char),
        LOCATION varchar2(100 char),
        PBOX varchar2(100 char),
        COUNTRY varchar2(100 char),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (RECEIVERID)
    )
/
    create table crmcampaignstatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crminvoice (
        INVOICEID number(10,0) not null,
        ASSOSIATION varchar2(150 char),
        INVOICENR number(10,0),
        BEGINCHAR varchar2(10 char),
        NAME varchar2(255 char),
        STATUS varchar2(255 char),
        CATEGORY varchar2(255 char),
        DATE date,
        TAXTYPE varchar2(150 char),
        GENDER varchar2(100 char),
        POSITION varchar2(100 char),
        COMPANYNAME varchar2(255 char),
        CONTACTNAME varchar2(255 char),
        CONTACTSURNAME varchar2(255 char),
        CONTACTSTREET varchar2(255 char),
        CONTACTZIP varchar2(100 char),
        CONTACTLOCATION varchar2(255 char),
        CONTACTPOSTCODE varchar2(255 char),
        CONTACTCOUNTRY varchar2(255 char),
        CONTACTTELEPHONE varchar2(255 char),
        CONTACTFAX varchar2(255 char),
        CONTACTEMAIL varchar2(255 char),
        CONTACTWEB varchar2(255 char),
        CONTACTDESCRIPTION long,
        CREATEDDATE date,
        CREATEDFROM varchar2(255 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(255 char),
        primary key (INVOICEID)
    )
/
    create table crminvoicecategory (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crminvoicenumber (
        ID number(10,0) not null,
        CATEGORYID number(10,0),
        CATEGORY varchar2(255 char),
        BEGINCHAR varchar2(10 char),
        NUMBERFROM number(10,0),
        NUMBERTO number(10,0),
        primary key (ID)
    )
/
    create table crminvoiceposition (
        POSITIONID number(10,0) not null,
        INVOICEID number(10,0),
        PRODUCTID number(10,0),
        DEDUCTION varchar2(5 char),
        PRODUCTNR varchar2(255 char),
        PRODUCTNAME varchar2(255 char),
        QUANTITY number(19,0),
        NETAMOUNT double precision,
        PRETAX double precision,
        TAXTYPE varchar2(150 char),
        TYPE varchar2(255 char),
        CATEGORY varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        primary key (POSITIONID)
    )
/
    create table crminvoicestatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crmproblemsolcategory (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crmproblemsolclass (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crmproblemsoldocs (
        SOLUTIONDOCID number(10,0) not null,
        SOLUTIONID number(10,0),
        NAME varchar2(255 char),
        FILES raw(255),
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        primary key (SOLUTIONDOCID)
    )
/
    create table crmproblemsolposition (
        PRODUCTID number(10,0) not null,
        SOLUTIONID number(10,0),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        PRODUCTNR varchar2(255 char),
        PRODUCTNAME varchar2(255 char),
        CATEGORY varchar2(255 char),
        TYPE varchar2(255 char),
        TAXTYPE varchar2(255 char),
        NETAMOUNT double precision,
        PRETAX double precision,
        DIPENDENCYID number(10,0),
        DIMENSIONID number(10,0),
        PICTURE raw(255),
        PICTURENAME varchar2(255 char),
        DESCRIPTION long,
        primary key (PRODUCTID)
    )
/
    create table crmproblemsolstatus (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crmproblemsoltype (
        ID number(10,0) not null,
        NAME varchar2(150 char),
        primary key (ID)
    )
/
    create table crmproblemsolutions (
        PROSOLID number(10,0) not null,
        SERVICENR varchar2(150 char),
        NAME varchar2(255 char),
        CLASSIFICATION varchar2(255 char),
        CATEGORY varchar2(255 char),
        TYPE varchar2(255 char),
        STATUS varchar2(255 char),
        DESCRIPTION long,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (PROSOLID)
    )
/
    create table crmproduct (
        PRODUCTID number(10,0) not null,
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        PRODUCTNR varchar2(255 char),
        PRODUCTNAME varchar2(255 char),
        CATEGORY varchar2(255 char),
        TYPE varchar2(255 char),
        TAXTYPE varchar2(255 char),
        NETAMOUNT double precision,
        PRETAX double precision,
        SALEPRICE double precision,
        DIPENDENCYID number(10,0),
        DIMENSIONID number(10,0),
        PICTURE raw(255),
        PICTURENAME varchar2(255 char),
        DESCRIPTION long,
        primary key (PRODUCTID)
    )
/
    create table crmproductdependency (
        DEPENDENCYID number(10,0) not null,
        PRODUCTID number(10,0),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        PRODUCTNR varchar2(255 char),
        PRODUCTNAME varchar2(255 char),
        primary key (DEPENDENCYID)
    )
/
    create table crmproductdimension (
        DIMENSIONID number(10,0) not null,
        PRODUCTID number(10,0),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        NAME varchar2(255 char),
        VALUE varchar2(255 char),
        primary key (DIMENSIONID)
    )
/
    create table crmproductdimensions (
        ID number(10,0) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    )
/
    create table crmproject (
        PROJECTID number(10,0) not null,
        PROJECTNR varchar2(255 char),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        NAME varchar2(255 char),
        MANAGER varchar2(255 char),
        BUDGET double precision,
        ACTUALCOST double precision,
        REMAINCOST double precision,
        STATUS varchar2(255 char),
        VALIDFROM date,
        VALIDTO date,
        primary key (PROJECTID)
    )
/
    create table crmprojectcost (
        COSTID number(10,0) not null,
        TASKID number(10,0),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        NAME varchar2(255 char),
        VALUE double precision,
        primary key (COSTID)
    )
/
    create table crmprojectcosts (
        ID number(10,0) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    )
/
    create table crmprojectprop (
        PROPERTIESID number(10,0) not null,
        TASKID number(10,0),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        CHANGEDDATE date,
        CHANGEDFROM varchar2(150 char),
        NAME varchar2(255 char),
        VALUE varchar2(255 char),
        primary key (PROPERTIESID)
    )
/
    create table crmprojectprops (
        ID number(10,0) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    )
/
    create table crmprojectstatus (
        ID number(10,0) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    )
/
    create table crmprojecttask (
        TASKIID number(10,0) not null,
        PROJECTID number(10,0),
        TASKID varchar2(255 char) not null unique,
        PARENTSTASKID long,
        X number(10,0),
        Y number(10,0),
        NAME varchar2(255 char),
        STATUS varchar2(255 char),
        TYPE varchar2(255 char),
        DURATION number(10,0),
        COLOR varchar2(12 char),
        DONE number(10,0),
        DESCRIPTION long,
        primary key (TASKIID)
    )
/
    create table crmprojecttaskstatus (
        ID number(10,0) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    )
/
    create table crmprojecttasktype (
        ID number(10,0) not null,
        NAME varchar2(255 char) not null,
        primary key (ID)
    )
/
    create table ebicrmhistory (
        HISTORYID number(10,0) not null,
        COMPANYID number(10,0),
        CATEGORY varchar2(255 char),
        CHANGEDVALUE long,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        primary key (HISTORYID)
    )
/
    create table ebipessimistic (
        OPTIMISTICID number(10,0) not null,
        RECORDID number(10,0),
        MODULENAME varchar2(255 char),
        USER varchar2(255 char),
        LOCKDATE timestamp,
        STATUS number(10,0),
        primary key (OPTIMISTICID)
    )
/
    create table ebiuser (
        ID number(10,0) not null,
        CREATEDFROM varchar2(150 char),
        CREATEDDATE date,
        CHANGEDFROM varchar2(150 char),
        CHANGEDDATE date,
        EBIUSER varchar2(50 char),
        PASSWD varchar2(255 char),
        IS_ADMIN number(1,0),
        CANSAVE number(1,0),
        CANPRINT number(1,0),
        CANDELETE number(1,0),
        primary key (ID)
    )
/
    create table mail_account (
        ID number(10,0) not null,
        ACCOUNTNAME varchar2(255 char),
        FOLDER_NAME varchar2(120 char),
        DELETE_MESSAGE number(1,0),
        SMTP_SERVER varchar2(255 char),
        SMTP_USER varchar2(255 char),
        SMTP_PASSWORD varchar2(255 char),
        EMAILADRESS varchar2(255 char),
        POP_SERVER varchar2(255 char),
        POP_USER varchar2(255 char),
        POP_PASSWORD varchar2(255 char),
        EMAILS_TITLE varchar2(150 char),
        CREATEFROM varchar2(255 char),
        CREATEDATE date,
        primary key (ID)
    )
/
    create table mail_assigned (
        ID number(10,0) not null,
        COMPANYID number(10,0),
        MAIL_DATE date,
        SETFROM varchar2(150 char),
        MAIL_FROM varchar2(255 char),
        MAIL_TO varchar2(255 char),
        MAIL_CC varchar2(255 char),
        MAIL_SUBJECT varchar2(255 char),
        MAIL_MESSAGE long,
        ATTACHID varchar2(255 char),
        primary key (ID)
    )
/
    create table mail_attach (
        ID number(10,0) not null,
        MAIL_ATTACHID varchar2(255 char),
        FILENAME varchar2(255 char),
        FILEBIN raw(255),
        primary key (ID)
    )
/
    create table mail_deleted (
        ID number(10,0) not null,
        MAIL_FROM varchar2(255 char),
        MAIL_TO varchar2(255 char),
        MAIL_CC varchar2(255 char),
        MAIL_SUBJECT varchar2(255 char),
        MAIL_MESSAGE long,
        ATTACHID varchar2(255 char),
        MAIL_DATE date,
        SETFROM varchar2(150 char),
        primary key (ID)
    )
/
    create table mail_inbox (
        ID number(10,0) not null,
        MAIL_FROM varchar2(255 char),
        MAIL_TO varchar2(255 char),
        MAIL_CC varchar2(255 char),
        MAIL_SUBJECT varchar2(255 char),
        MAIL_MESSAGE long,
        ATTACHID varchar2(255 char),
        MAIL_DATE timestamp,
        SETFROM varchar2(150 char),
        primary key (ID)
    )
/
    create table mail_outbox (
        ID number(10,0) not null,
        MAIL_FROM varchar2(255 char),
        MAIL_TO varchar2(255 char),
        MAIL_CC varchar2(255 char),
        MAIL_SUBJECT varchar2(255 char),
        MAIL_MESSAGE long,
        ATTACHID varchar2(255 char),
        MAIL_DATE date,
        SETFROM varchar2(150 char),
        primary key (ID)
    )
/
    create table mail_template (
        ID number(10,0) not null,
        SETFROM varchar2(150 char),
        SETDATE date,
        NAME varchar2(255 char),
        TEMPLATE long,
        ISACTIVE number(1,0),
        primary key (ID)
    )
/
    create table set_reportformodule (
        IDREPORTFORMODULE number(10,0) not null,
        REPORTNAME varchar2(255 char),
        REPORTCATEGORY varchar2(255 char),
        REPORTFILE raw(255),
        REPORTFILENAME varchar2(255 char),
        REPORTDATE date,
        SHOWASPDF number(1,0),
        SHOWASWINDOW number(1,0),
        PRINTAUTO number(1,0),
        ISACTIVE number(1,0),
        primary key (IDREPORTFORMODULE)
    )
/
    create table set_reportparameter (
        PARAMID number(10,0) not null,
        REPORTID number(10,0),
        POSITION number(10,0),
        CREATEDDATE date,
        CREATEDFROM varchar2(150 char),
        PARAMNAME varchar2(150 char),
        PARAMTYPE varchar2(150 char),
        primary key (PARAMID)
    )
/
    alter table accountstackdocs 
        add constraint FK9275F936EDEA7D2 
        foreign key (ACCOUNTID) 
        references accountstack
/
    alter table companyactivities 
        add constraint FK9B92C02A4B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companyactivitiesdocs 
        add constraint FK16B1BD65260096A5 
        foreign key (ACTIVITYID) 
        references companyactivities
/
    alter table companyaddress 
        add constraint FK4887C8D74B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companybank 
        add constraint FKE1B8BC394B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companycontactaddress 
        add constraint FK12E6B1918586275C 
        foreign key (CONTACTID) 
        references companycontacts
/
    alter table companycontacts 
        add constraint FKEF42CFD04B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companyhirarchie 
        add constraint FK234282E64B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companymeetingcontacts 
        add constraint FK5CE1D971E2F680DB 
        foreign key (MEETINGID) 
        references companymeetingprotocol
/
    alter table companymeetingdoc 
        add constraint FK5151515AE2F680DB 
        foreign key (MEETINGID) 
        references companymeetingprotocol
/
    alter table companymeetingprotocol 
        add constraint FK43BF07B64B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companynumber 
        add constraint FK6C0E060610245D65 
        foreign key (CATEGORYID) 
        references companycategory
/
    alter table companyoffer 
        add constraint FK561823FF4B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companyofferdocs 
        add constraint FK85872EBA33647FC5 
        foreign key (OFFERID) 
        references companyoffer
/
    alter table companyofferpositions 
        add constraint FKCA47774B33647FC5 
        foreign key (OFFERID) 
        references companyoffer
/
    alter table companyofferreceiver 
        add constraint FK993B9EEE33647FC5 
        foreign key (OFFERID) 
        references companyoffer
/
    alter table companyopportunity 
        add constraint FKC073EA364B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companyopportunitycontact 
        add constraint FK5D67712A9A49D973 
        foreign key (OPPORTUNITYID) 
        references companyopportunity
/
    alter table companyopporunitydocs 
        add constraint FKB8C5545B9A49D973 
        foreign key (OPPORTUNITYID) 
        references companyopportunity
/
    alter table companyorder 
        add constraint FK561D90F14B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companyorderdocs 
        add constraint FKFA48D2AC47C7E529 
        foreign key (ORDERID) 
        references companyorder
/
    alter table companyorderpositions 
        add constraint FKE9087A9947C7E529 
        foreign key (ORDERID) 
        references companyorder
/
    alter table companyorderreceiver 
        add constraint FKCBC5F9E047C7E529 
        foreign key (ORDERID) 
        references companyorder
/
    alter table companyservice 
        add constraint FK333F3984B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table companyservicedocs 
        add constraint FK9A8F89D33F54B1B7 
        foreign key (SERVICEID) 
        references companyservice
/
    alter table companyservicepositions 
        add constraint FKDE923D23F54B1B7 
        foreign key (SERVICEID) 
        references companyservice
/
    alter table companyservicepsol 
        add constraint FK9A950EB83F54B1B7 
        foreign key (SERVICEID) 
        references companyservice
/
    alter table crmcampaigndocs 
        add constraint FKECB8C02984114DEA 
        foreign key (CAMPAIGNID) 
        references crmcampaign
/
    alter table crmcampaignposition 
        add constraint FK5E1F901784114DEA 
        foreign key (CAMPAIGNID) 
        references crmcampaign
/
    alter table crmcampaignprop 
        add constraint FKECBE415184114DEA 
        foreign key (CAMPAIGNID) 
        references crmcampaign
/
    alter table crmcampaignreceiver 
        add constraint FK158E0DD84114DEA 
        foreign key (CAMPAIGNID) 
        references crmcampaign
/
    alter table crminvoicenumber 
        add constraint FKB7363F987D593035 
        foreign key (CATEGORYID) 
        references crminvoicecategory
/
    alter table crminvoiceposition 
        add constraint FK5FA825B8FB72A7A6 
        foreign key (INVOICEID) 
        references crminvoice
/
    alter table crmproblemsoldocs 
        add constraint FK93D959AA3838265E 
        foreign key (SOLUTIONID) 
        references crmproblemsolutions
/
    alter table crmproblemsolposition 
        add constraint FKE86581183838265E 
        foreign key (SOLUTIONID) 
        references crmproblemsolutions
/
    alter table crmproductdependency 
        add constraint FK3B43375C9D2652AA 
        foreign key (PRODUCTID) 
        references crmproduct
/
    alter table crmproductdimension 
        add constraint FKC0B1C3359D2652AA 
        foreign key (PRODUCTID) 
        references crmproduct
/
    alter table crmprojectcost 
        add constraint FK9DC30DC8B0FD4C6F 
        foreign key (TASKID) 
        references crmprojecttask
/
    alter table crmprojectprop 
        add constraint FK9DC9015EB0FD4C6F 
        foreign key (TASKID) 
        references crmprojecttask
/
    alter table crmprojecttask 
        add constraint FK9DCA9380A6846C7E 
        foreign key (PROJECTID) 
        references crmproject
/
    alter table mail_assigned 
        add constraint FK883BB7564B8DEA26 
        foreign key (COMPANYID) 
        references company
/
    alter table set_reportparameter 
        add constraint FK765F07183724B25D 
        foreign key (REPORTID) 
        references set_reportformodule
/
    create sequence hibernate_sequence
/