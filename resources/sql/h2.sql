/
    create table if not exists  ACCOUNTSTACK (
        ACSTACKID integer not null auto_increment,
        ACCOUNTNR varchar(255),
        ACCOUNT text,
        ACCOUNT_TYPE integer,
        ACCOUNT_DEBIT_TAX_TYPE varchar(255),
        ACCOUNT_CREDIT_TAX_TYPE VARCHAR(255) NULL,
        TAX_VALUE double precision,
        ACCOUNT_DEBIT varchar(255),
        ACCOUNT_CREDIT varchar(255),
        ACCOUNT_D_NAME varchar(255),
        ACCOUNT_C_NAME varchar(255),
        ACCOUNT_C_VALUE double precision,
        ACCOUNT_D_VALUE double precision,
        ACCOUNTNAME varchar(255),
        ACCOUNTVALUE double precision,
        DESCRIPTION text,
        ACCOUNTDATE datetime,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (ACSTACKID)
    );
/
    create table if not exists  ACCOUNTSTACKCD (
        ACCOUNTSTACKCDID integer not null auto_increment,
        ACCOUNTSTACKID integer,
        CREDITDEBITNUMBER varchar(150),
        CREDITDEBITNAME varchar(255),
        CREDITDEBITVALUE double precision,
        CREDITDEBITTAXTNAME varchar(150),
        CREDITDEBITTYPE integer,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (ACCOUNTSTACKCDID)
    );
/
    create table if not exists  ACCOUNTSTACKDOCS (
        ACCOUNTDOCID integer not null auto_increment,
        ACCOUNTID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (ACCOUNTDOCID)
    );
/
    create table if not exists  COMPANY (
        COMPANYID integer not null auto_increment,
        COMPANYNUMBER integer,
        CUSTOMERNR varchar(255),
        BEGINCHAR varchar(10),
        `NAME` varchar(255),
        NAME2 varchar(255),
        PHONE varchar(50),
        FAX varchar(50),
        EMAIL varchar(50),
        EMPLOYEE varchar(100),
        QUALIFICATION varchar(100),
        CATEGORY varchar(150),
        COOPERATION varchar(150),
        ISLOCK bit,
        WEB varchar(150),
        TAXNUMBER varchar(50),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        ISACTUAL bit,
        primary key (COMPANYID)
    );
/
    create table if not exists  COMPANYACTIVITIES (
        ACTIVITYID integer not null auto_increment,
        COMPANYID integer,
        ACTIVITYNAME varchar(150),
        ACTIVITYTYPE varchar(50),
        ACTIVITYSTATUS varchar(50),
        DUEDATE datetime,
        DURATION integer,
        ACOLOR varchar(18),
        ACTIVITYCLOSEDATE datetime,
        ACTIVITYISCLOSED bit,
        ACTIVITYDESCRIPTION text,
        ISLOCK bit,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        TIMERSTART integer,
        TIMERDISABLED integer,
        TIMER_EXTRA text,
        primary key (ACTIVITYID)
    );
/
    create table if not exists  COMPANYACTIVITIESDOCS (
        ACTIVITYDOCID integer not null auto_increment,
        ACTIVITYID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (ACTIVITYDOCID)
    );
/
    create table if not exists  COMPANYACTIVITYSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(255),
        primary key (ID)
    );
/
    create table if not exists  COMPANYACTIVITYTYPE (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYADDRESS (
        ADDRESSID integer not null auto_increment,
        COMPANYID integer,
        ADDRESSTYPE varchar(150),
        STREET varchar(50),
        ZIP varchar(10),
        LOCATION varchar(50),
        PBOX varchar(150),
        COUNTRY varchar(150),
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (ADDRESSID)
    );
/
    create table if not exists  COMPANYBANK (
        BANKID integer not null auto_increment,
        COMPANYID integer,
        BANKNAME varchar(255),
        BANKBSB varchar(255),
        BANKACCOUNT varchar(255),
        BANKBIC varchar(255),
        BANKIBAN varchar(255),
        BANKCOUNTRY varchar(150),
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (BANKID)
    );
/
    create table if not exists  COMPANYCATEGORY (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYCLASSIFICATION (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYCONTACTADDRESS (
        ADDRESSID integer not null auto_increment,
        CONTACTID integer,
        ADDRESSTYPE varchar(150),
        STREET varchar(80),
        ZIP varchar(20),
        LOCATION varchar(150),
        COUNTRY varchar(150),
        PBOX varchar(150),
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (ADDRESSID)
    );
/
    create table if not exists  COMPANYCONTACTS (
        CONTACTID integer not null auto_increment,
        COMPANYID integer not null,
        MAINCONTACT bit,
        GENDER varchar(20),
        TITLE varchar(255),
        SURNAME varchar(150),
        `NAME` varchar(150),
        MITTELNAME varchar(150),
        `POSITION` varchar(150),
        BIRDDATE datetime,
        PHONE varchar(30),
        FAX varchar(30),
        MOBILE varchar(30),
        EMAIL varchar(50),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (CONTACTID)
    );
/
    create table if not exists  COMPANYCOOPERATION (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYHIRARCHIE (
        HIERARCHIEID integer not null auto_increment,
        COMPANYID integer,
        `NAME` varchar(255),
        ROOT integer,
        PARENT integer,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (HIERARCHIEID)
    );
/
    create table if not exists  COMPANYMEETINGCONTACTS (
        MEETINGCONTACTID integer not null auto_increment,
        MEETINGID integer,
        POS integer,
        GENDER varchar(20),
        SURNAME varchar(150),
        `NAME` varchar(150),
        MITTELNAME varchar(150),
        `POSITION` varchar(150),
        BIRDDATE datetime,
        PHONE varchar(30),
        FAX varchar(30),
        MOBILE varchar(30),
        EMAIL varchar(50),
        STREET varchar(255),
        LOCATION varchar(255),
        ZIP varchar(255),
        COUNTRY varchar(255),
        PBOX varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE date not null,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (MEETINGCONTACTID)
    );
/
    create table if not exists  COMPANYMEETINGDOC (
        MEETINGDOCID integer not null auto_increment,
        MEETINGID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (MEETINGDOCID)
    );
/
    create table if not exists  COMPANYMEETINGPROTOCOL (
        MEETINGPROTOCOLID integer not null auto_increment,
        COMPANYID integer,
        MEETINGSUBJECT varchar(255),
        MEETINGTYPE varchar(150),
        PROTOCOL text,
        METINGDATE datetime,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (MEETINGPROTOCOLID)
    );
/
    create table if not exists  COMPANYMEETINGTYPE (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYNUMBER (
        ID integer not null auto_increment,
        CATEGORYID integer,
        CATEGORY varchar(255),
        BEGINCHAR varchar(10),
        NUMBERFROM integer,
        NUMBERTO integer,
        primary key (ID)
    );
/
    create table if not exists  COMPANYOFFER (
        OFFERID integer not null auto_increment,
        COMPANYID integer,
        OPPORTUNITYID integer,
        OFFERNR varchar(255) not null,
        `NAME` varchar(255),
        STATUS varchar(100),
        OFFERVERSION integer,
        OFFERDATE datetime,
        VALIDTO datetime,
        ISRECIEVED bit,
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (OFFERID)
    );
/
    create table if not exists  COMPANYOFFERDOCS (
        OFFERDOCID integer not null auto_increment,
        OFFERID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (OFFERDOCID)
    );
/
    create table if not exists  COMPANYOFFERPOSITIONS (
        POSITIONID integer not null auto_increment,
        OFFERID integer,
        PRODUCTID integer,
        PRODUCTNR varchar(255),
        DEDUCTION varchar(5),
        PRODUCTNAME varchar(255),
        QUANTITY bigint,
        NETAMOUNT double precision not null,
        PRETAX double precision not null,
        TAXTYPE varchar(150),
        `TYPE` varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    );
/
    create table if not exists  COMPANYOFFERRECEIVER (
        RECEIVERID integer not null auto_increment,
        OFFERID integer,
        CNUM integer,
        RECEIVERVIA varchar(150),
        GENDER varchar(20),
        SURNAME varchar(150),
        MITTELNAME varchar(150),
        `POSITION` varchar(150),
        EMAIL varchar(50),
        PHONE varchar(30),
        FAX varchar(30),
        STREET varchar(150),
        ZIP varchar(15),
        LOCATION varchar(80),
        PBOX varchar(100),
        COUNTRY varchar(100),
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        NAME varchar(150),
        primary key (RECEIVERID)
    );
/
    create table if not exists  COMPANYOFFERSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYOPPORTUNITY (
        OPPORTUNITYID integer not null auto_increment,
        COMPANYID integer,
        `NAME` varchar(255),
        SALESTAGE varchar(100),
        PROBABILITY varchar(100),
        OPPORTUNITYVALUE double precision,
        ISCLOSE bit,
        CLOSEDATE datetime,
        BUSINESSTYPE varchar(150),
        EVALUATIONSTATUS varchar(150),
        EVALUETIONDATE datetime,
        BUDGETSTATUS varchar(150),
        BUDGETDATE datetime,
        SALESTAGEDATE datetime,
        OPPORTUNITYSTATUS varchar(150),
        OPPORTUNITYSTATUSDATE datetime,
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (OPPORTUNITYID)
    );
/
    create table if not exists  COMPANYOPPORTUNITYBGSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYOPPORTUNITYBUSTYP (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYOPPORTUNITYCONTACT (
        OPPORTUNITYCONTACTID integer not null auto_increment,
        OPPORTUNITYID integer,
        POS integer,
        GENDER varchar(20),
        SURNAME varchar(150),
        `NAME` varchar(150),
        MITTELNAME varchar(150),
        STREET varchar(255),
        ZIP varchar(255),
        LOCATION varchar(255),
        PBOX varchar(255),
        COUNTRY varchar(255),
        `POSITION` varchar(150),
        BIRDDATE datetime,
        PHONE varchar(30),
        FAX varchar(30),
        MOBILE varchar(30),
        EMAIL varchar(50),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (OPPORTUNITYCONTACTID)
    );
/
    create table if not exists  COMPANYOPPORTUNITYEVSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYOPPORTUNITYSSTAGE (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYOPPORTUNITYSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYOPPORUNITYDOCS (
        DOCID integer not null auto_increment,
        OPPORTUNITYID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (DOCID)
    );
/
    create table if not exists  COMPANYORDER (
        ORDERID integer not null auto_increment,
        COMPANYID integer,
        ORDERNR varchar(150),
        OFFERID integer,
        `NAME` varchar(255),
        INVOICECREATED bit,
        STATUS varchar(150),
        ORDERVERSION integer,
        OFFERDATE datetime,
        VALIDTO datetime,
        ISRECIEVED bit,
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (ORDERID)
    );
/
    create table if not exists  COMPANYORDERDOCS (
        ORDERDOCID integer not null auto_increment,
        ORDERID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (ORDERDOCID)
    );
/
    create table if not exists  COMPANYORDERPOSITIONS (
        POSITIONID integer not null auto_increment,
        ORDERID integer,
        PRODUCTID integer,
        DEDUCTION varchar(5),
        PRODUCTNR varchar(255),
        PRODUCTNAME varchar(255),
        QUANTITY bigint,
        NETAMOUNT double precision,
        PRETAX double precision,
        TAXTYPE varchar(150),
        `TYPE` varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    );
/
    create table if not exists  COMPANYORDERRECEIVER (
        RECEIVERID integer not null auto_increment,
        ORDERID integer,
        CNUM integer,
        RECEIVERVIA varchar(50),
        GENDER varchar(20),
        SURNAME varchar(150),
        `NAME` varchar(150),
        MITTELNAME varchar(150),
        `POSITION` varchar(150),
        EMAIL varchar(50),
        PHONE varchar(30),
        FAX varchar(30),
        STREET varchar(50),
        ZIP varchar(15),
        LOCATION varchar(100),
        PBOX varchar(100),
        COUNTRY varchar(100),
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (RECEIVERID)
    );
/
    create table if not exists  COMPANYORDERSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYPRODUCTCATEGORY (
        ID integer not null auto_increment,
        `NAME` varchar(255),
        primary key (ID)
    );
/
    create table if not exists  COMPANYPRODUCTTAX (
        ID integer not null auto_increment,
        `NAME` varchar(255),
        TAXVALUE double precision,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYPRODUCTTAXVALUE (
        ID integer not null auto_increment,
        `NAME` varchar(255),
        primary key (ID)
    );
/
    create table if not exists  COMPANYPRODUCTTYPE (
        ID integer not null auto_increment,
        `NAME` varchar(255),
        primary key (ID)
    );
/
    create table if not exists  COMPANYSERVICE (
        SERVICEID integer not null auto_increment,
        COMPANYID integer,
        SERVICENR varchar(150),
        `NAME` varchar(255),
        CATEGORY varchar(255),
        `TYPE` varchar(255),
        STATUS varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (SERVICEID)
    );
/
    create table if not exists  COMPANYSERVICECATEGORY (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYSERVICEDOCS (
        SERVICEDOCID integer not null auto_increment,
        SERVICEID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (SERVICEDOCID)
    );
/
    create table if not exists  COMPANYSERVICEPOSITIONS (
        POSITIONID integer not null auto_increment,
        SERVICEID integer,
        PRODUCTID integer,
        DEDUCTION varchar(5),
        PRODUCTNR varchar(255),
        PRODUCTNAME varchar(255),
        QUANTITY bigint,
        NETAMOUNT double precision,
        PRETAX double precision,
        TAXTYPE varchar(150),
        `TYPE` varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    );
/
    create table if not exists  COMPANYSERVICEPSOL (
        PROSOLID integer not null auto_increment,
        SERVICEID integer,
        SOLUTIONNR varchar(150),
        `NAME` varchar(255),
        CLASSIFICATION varchar(255),
        CATEGORY varchar(255),
        `TYPE` varchar(255),
        STATUS varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (PROSOLID)
    );
/
    create table if not exists  COMPANYSERVICESTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  COMPANYSERVICETYPE (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMADDRESSTYPE (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMCALENDAR (
        CALENDARID integer not null auto_increment,
        CUSER varchar(150),
        `NAME` varchar(255),
        COLOR varchar(15),
        DESCRIPTION text,
        STARTDATE datetime,
        ENDDATE datetime,
        CICON longblob,
        primary key (CALENDARID)
    );
/
    create table if not exists  CRMINVOICE (
        INVOICEID integer not null auto_increment,
        ASSOSIATION varchar(150),
        INVOICENR integer,
        BEGINCHAR varchar(10),
        `NAME` varchar(255),
        STATUS varchar(255),
        CATEGORY varchar(255),
        `DATE` datetime,
        TAXTYPE varchar(150),
        GENDER varchar(100),
        `POSITION` varchar(100),
        COMPANYNAME varchar(255),
        CONTACTNAME varchar(255),
        CONTACTSURNAME varchar(255),
        CONTACTSTREET varchar(255),
        CONTACTZIP varchar(100),
        CONTACTLOCATION varchar(255),
        CONTACTPOSTCODE varchar(255),
        CONTACTCOUNTRY varchar(255),
        CONTACTTELEPHONE varchar(255),
        CONTACTFAX varchar(255),
        CONTACTEMAIL varchar(255),
        CONTACTWEB varchar(255),
        CONTACTDESCRIPTION text,
        CREATEDDATE datetime,
        CREATEDFROM varchar(255),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(255),
        primary key (INVOICEID)
    );
/
    create table if not exists  CRMINVOICECATEGORY (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMINVOICENUMBER (
        ID integer not null auto_increment,
        CATEGORYID integer,
        CATEGORY varchar(255),
        BEGINCHAR varchar(10),
        NUMBERFROM integer,
        NUMBERTO integer,
        primary key (ID)
    );
/
    create table if not exists  CRMINVOICEPOSITION (
        POSITIONID integer not null auto_increment,
        INVOICEID integer,
        PRODUCTID integer,
        DEDUCTION varchar(5),
        PRODUCTNR varchar(255),
        PRODUCTNAME varchar(255),
        QUANTITY bigint,
        NETAMOUNT double precision,
        PRETAX double precision,
        TAXTYPE varchar(150),
        `TYPE` varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    );
/
    create table if not exists  CRMINVOICESTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMPROBLEMSOLCATEGORY (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMPROBLEMSOLCLASS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMPROBLEMSOLDOCS (
        SOLUTIONDOCID integer not null auto_increment,
        SOLUTIONID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (SOLUTIONDOCID)
    );
/
    create table if not exists  CRMPROBLEMSOLPOSITION (
        POSITIONID integer not null auto_increment,
        SOLUTIONID integer,
        PRODUCTID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        PRODUCTNR varchar(255),
        PRODUCTNAME varchar(255),
        CATEGORY varchar(255),
        `TYPE` varchar(255),
        TAXTYPE varchar(255),
        NETAMOUNT double precision,
        PRETAX double precision,
        DIPENDENCYID integer,
        DIMENSIONID integer,
        PICTURE longblob,
        PICTURENAME varchar(255),
        DESCRIPTION text,
        primary key (POSITIONID)
    );
/
    create table if not exists  CRMPROBLEMSOLSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMPROBLEMSOLTYPE (
        ID integer not null auto_increment,
        `NAME` varchar(150),
        primary key (ID)
    );
/
    create table if not exists  CRMPROBLEMSOLUTIONS (
        PROSOLID integer not null auto_increment,
        SERVICENR varchar(150),
        `NAME` varchar(255),
        CLASSIFICATION varchar(255),
        CATEGORY varchar(255),
        `TYPE` varchar(255),
        STATUS varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (PROSOLID)
    );
/
    create table if not exists  CRMPRODUCT (
        PRODUCTID integer not null auto_increment,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        PRODUCTNR varchar(255),
        PRODUCTNAME varchar(255),
        CATEGORY varchar(255),
        `TYPE` varchar(255),
        TAXTYPE varchar(255),
        NETAMOUNT double precision,
        PRETAX double precision,
        SALEPRICE double precision,
        PICTURE longblob,
        PICTURENAME varchar(255),
        DESCRIPTION text,
        primary key (PRODUCTID)
    );
/
    create table if not exists  CRMPRODUCTDEPENDENCY (
        DEPENDENCYID integer not null auto_increment,
        PRODUCTID integer,
        PRODUCTIDID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        PRODUCTNR varchar(255),
        PRODUCTNAME varchar(255),
        primary key (DEPENDENCYID)
    );
/
    create table if not exists  CRMPRODUCTDIMENSION (
        DIMENSIONID integer not null auto_increment,
        PRODUCTID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        `DIMENSIONNAME` varchar(255),
        `DIMENSIONVALUE` varchar(255),
        primary key (DIMENSIONID)
    );
/
    create table if not exists  CRMPRODUCTDIMENSIONS (
        ID integer not null auto_increment,
        `NAME` varchar(255) not null,
        primary key (ID)
    );
/
    create table if not exists  CRMPRODUCTDOCS (
        PRODUCTDOCID integer not null auto_increment,
        PRODUCTID integer,
        `NAME` varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (PRODUCTDOCID)
    );
/
    create table if not exists  CRMPROJECT (
        PROJECTID integer not null auto_increment,
        PROJECTNR varchar(255),
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        `NAME` varchar(255),
        MANAGER varchar(255),
        BUDGET double precision,
        ACTUALCOST double precision,
        REMAINCOST double precision,
        STATUS varchar(255),
        VALIDFROM datetime,
        VALIDTO datetime,
        primary key (PROJECTID)
    );
/
    create table if not exists  CRMPROJECTCOST (
        COSTID integer not null auto_increment,
        TASKID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        `NAME` varchar(255),
        `VALUE` double precision,
        primary key (COSTID)
    );
/
    create table if not exists  CRMPROJECTCOSTS (
        ID integer not null auto_increment,
        `NAME` varchar(255) not null,
        primary key (ID)
    );
/
    create table if not exists  CRMPROJECTPROP (
        PROPERTIESID integer not null auto_increment,
        TASKID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        `NAME` varchar(255),
        `VALUE` varchar(255),
        primary key (PROPERTIESID)
    );
/
    create table if not exists  CRMPROJECTPROPS (
        ID integer not null auto_increment,
        `NAME` varchar(255) not null,
        primary key (ID)
    );
/
    create table if not exists  CRMPROJECTSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(255) not null,
        primary key (ID)
    );
/
    create table if not exists  CRMPROJECTTASK (
        TASKIID integer not null auto_increment,
        PROJECTID integer,
        TASKID varchar(255) not null,
        PARENTSTASKID text,
        X integer,
        Y integer,
        `NAME` varchar(255),
        STATUS varchar(255),
        `TYPE` varchar(255),
        DURATION integer,
        COLOR varchar(12),
        DONE integer,
        DESCRIPTION text,
        primary key (TASKIID)
    );
/
    create table if not exists  CRMPROJECTTASKSTATUS (
        ID integer not null auto_increment,
        `NAME` varchar(255) not null,
        primary key (ID)
    );
/
    create table if not exists  CRMPROJECTTASKTYPE (
        ID integer not null auto_increment,
        `NAME` varchar(255) not null,
        primary key (ID)
    );
/
    create table if not exists  EBICRMHISTORY (
        HISTORYID integer not null auto_increment,
        COMPANYID integer,
        CATEGORY varchar(255),
        CHANGEDVALUE text,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (HISTORYID)
    );
/
    create table if not exists  EBIDATASTORE (
        ID integer not null auto_increment,
        `NAME` varchar(255),
        TEXT text,
        EXTRA varchar(255),
        primary key (ID)
    );

/
    create table if not exists  EBIUSER (
        ID integer not null auto_increment,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        EBIUSER varchar(50),
        PASSWD varchar(255),
        IS_ADMIN bit,
        MODULEID text,
        CANSAVE bit,
        CANPRINT bit,
        CANDELETE bit,
        primary key (ID)
    );
/
    create table if not exists  MAIL_ACCOUNT (
        ID integer not null auto_increment,
        ACCOUNTNAME varchar(255),
        FOLDER_NAME varchar(120),
        DELETE_MESSAGE bit,
        SMTP_SERVER varchar(255),
        SMTP_USER varchar(255),
        SMTP_PASSWORD varchar(255),
        EMAILADRESS varchar(255),
        POP_SERVER varchar(255),
        POP_USER varchar(255),
        POP_PASSWORD varchar(255),
        EMAILS_TITLE varchar(150),
        CREATEFROM varchar(255),
        CREATEDATE datetime,
        primary key (ID)
    );
/
    create table if not exists  MAIL_TEMPLATE (
        ID integer not null auto_increment,
        SETFROM varchar(150),
        SETDATE datetime,
        `NAME` varchar(255),
        TEMPLATE text,
        ISACTIVE bit,
        primary key (ID)
    );
/
    create table if not exists  SET_REPORTFORMODULE (
        IDREPORTFORMODULE integer not null auto_increment,
        REPORTNAME varchar(255),
        REPORTCATEGORY varchar(255),
        REPORTFILENAME text,
        REPORTDATE datetime,
        SHOWASPDF bit,
        SHOWASWINDOW bit,
        PRINTAUTO bit,
        ISACTIVE bit,
        primary key (IDREPORTFORMODULE)
    );
/
    create table if not exists  SET_REPORTPARAMETER (
        PARAMID integer not null auto_increment,
        REPORTID integer,
        `POSITION` integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        PARAMALIAS varchar(200),
        PARAMNAME varchar(150),
        PARAMTYPE varchar(150),
        primary key (PARAMID)
    );
/
    CREATE INDEX FKA2D1D936EDEA7D2 ON ACCOUNTSTACKDOCS(ACCOUNTID);
/    
    alter table ACCOUNTSTACKDOCS
        add constraint FKA2D1D936EDEA7D2 
        foreign key (ACCOUNTID) 
        references ACCOUNTSTACK (ACSTACKID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK96B2E00A4B8DEA26 ON COMPANYACTIVITIES(COMPANYID);
/
    alter table COMPANYACTIVITIES
        add constraint FK96B2E00A4B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK42EFE545260096A5 ON COMPANYACTIVITIESDOCS(ACTIVITYID);
/
    alter table COMPANYACTIVITIESDOCS 
        add constraint FK42EFE545260096A5 
        foreign key (ACTIVITYID) 
        references COMPANYACTIVITIES (ACTIVITYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKC68CACD74B8DEA26 ON COMPANYADDRESS(COMPANYID);
/
    alter table COMPANYADDRESS 
        add constraint FKC68CACD74B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK620C50194B8DEA26 ON COMPANYBANK(COMPANYID);
/
    alter table COMPANYBANK 
        add constraint FK620C50194B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK3F24D9718586275C ON COMPANYCONTACTADDRESS(CONTACTID);
/
    alter table COMPANYCONTACTADDRESS 
        add constraint FK3F24D9718586275C 
        foreign key (CONTACTID) 
        references COMPANYCONTACTS (CONTACTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK31DA6BB04B8DEA26 ON COMPANYCONTACTS(COMPANYID);
/
    alter table COMPANYCONTACTS 
        add constraint FK31DA6BB04B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK339E62E64B8DEA26 ON COMPANYHIRARCHIE(COMPANYID);
/
    alter table COMPANYHIRARCHIE 
        add constraint FK339E62E64B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKB868AD71E2F680DB ON COMPANYMEETINGCONTACTS(MEETINGID);
/
    alter table COMPANYMEETINGCONTACTS 
        add constraint FKB868AD71E2F680DB 
        foreign key (MEETINGID) 
        references COMPANYMEETINGPROTOCOL (MEETINGPROTOCOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK4C71713AE2F680DB ON COMPANYMEETINGDOC(MEETINGID);
/
    alter table COMPANYMEETINGDOC 
        add constraint FK4C71713AE2F680DB 
        foreign key (MEETINGID) 
        references COMPANYMEETINGPROTOCOL (MEETINGPROTOCOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK9F45DBB64B8DEA26 ON COMPANYMEETINGPROTOCOL(COMPANYID);
/
    alter table COMPANYMEETINGPROTOCOL 
        add constraint FK9F45DBB64B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK25CC1DE610245D65 ON COMPANYNUMBER(CATEGORYID);
/
    alter table COMPANYNUMBER 
        add constraint FK25CC1DE610245D65 
        foreign key (CATEGORYID) 
        references COMPANYCATEGORY (ID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKE0370BFF4B8DEA26 ON COMPANYOFFER(COMPANYID);
/
    alter table COMPANYOFFER 
        add constraint FKE0370BFF4B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK95E30EBA33647FC5 ON COMPANYOFFERDOCS(OFFERID);
/
    alter table COMPANYOFFERDOCS 
        add constraint FK95E30EBA33647FC5 
        foreign key (OFFERID) 
        references COMPANYOFFER (OFFERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKF6859F2B33647FC5 ON COMPANYOFFERPOSITIONS(OFFERID);
/
    alter table COMPANYOFFERPOSITIONS 
        add constraint FKF6859F2B33647FC5 
        foreign key (OFFERID) 
        references COMPANYOFFER (OFFERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK589876EE33647FC5 ON COMPANYOFFERRECEIVER(OFFERID);
/
    alter table COMPANYOFFERRECEIVER 
        add constraint FK589876EE33647FC5 
        foreign key (OFFERID) 
        references COMPANYOFFER (OFFERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK2957C6364B8DEA26 ON COMPANYOPPORTUNITY(COMPANYID);
/
    alter table COMPANYOPPORTUNITY 
        add constraint FK2957C6364B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK6C7FA10A9A49D973 ON COMPANYOPPORTUNITYCONTACT(OPPORTUNITYID);
/
    alter table COMPANYOPPORTUNITYCONTACT 
        add constraint FK6C7FA10A9A49D973 
        foreign key (OPPORTUNITYID) 
        references COMPANYOPPORTUNITY (OPPORTUNITYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKE5037C3B9A49D973 ON COMPANYOPPORUNITYDOCS(OPPORTUNITYID);
/
    alter table COMPANYOPPORUNITYDOCS 
        add constraint FKE5037C3B9A49D973 
        foreign key (OPPORTUNITYID) 
        references COMPANYOPPORTUNITY (OPPORTUNITYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKE03C78F14B8DEA26 ON COMPANYORDER(COMPANYID);
/
    alter table COMPANYORDER 
        add constraint FKE03C78F14B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKAA4B2AC47C7E529 ON COMPANYORDERDOCS(ORDERID);
/
    alter table COMPANYORDERDOCS 
        add constraint FKAA4B2AC47C7E529 
        foreign key (ORDERID) 
        references COMPANYORDER (ORDERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK1546A27947C7E529 ON COMPANYORDERPOSITIONS(ORDERID);
/
    alter table COMPANYORDERPOSITIONS 
        add constraint FK1546A27947C7E529 
        foreign key (ORDERID) 
        references COMPANYORDER (ORDERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK8B22D1E047C7E529 ON COMPANYORDERRECEIVER(ORDERID);
/
    alter table COMPANYORDERRECEIVER 
        add constraint FK8B22D1E047C7E529 
        foreign key (ORDERID) 
        references COMPANYORDER (ORDERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK8138D7984B8DEA26 ON COMPANYSERVICE(COMPANYID);
/
    alter table COMPANYSERVICE 
        add constraint FK8138D7984B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK37365D33F54B1B7 ON COMPANYSERVICEDOCS(SERVICEID);
/
    alter table COMPANYSERVICEDOCS 
        add constraint FK37365D33F54B1B7 
        foreign key (SERVICEID) 
        references COMPANYSERVICE (SERVICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK233CCFB23F54B1B7 ON COMPANYSERVICEPOSITIONS(SERVICEID);
/
    alter table COMPANYSERVICEPOSITIONS 
        add constraint FK233CCFB23F54B1B7 
        foreign key (SERVICEID) 
        references COMPANYSERVICE (SERVICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK378EAB83F54B1B7 ON COMPANYSERVICEPSOL(SERVICEID);
/
    alter table COMPANYSERVICEPSOL 
        add constraint FK378EAB83F54B1B7 
        foreign key (SERVICEID) 
        references COMPANYSERVICE (SERVICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKC7921F987D593035 ON CRMINVOICENUMBER(CATEGORYID);
/
    alter table CRMINVOICENUMBER 
        add constraint FKC7921F987D593035 
        foreign key (CATEGORYID) 
        references CRMINVOICECATEGORY (ID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKC88C01B8FB72A7A6 ON CRMINVOICEPOSITION(INVOICEID);
/
    alter table CRMINVOICEPOSITION 
        add constraint FKC88C01B8FB72A7A6 
        foreign key (INVOICEID) 
        references CRMINVOICE (INVOICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK8EF9798A3838265E ON CRMPROBLEMSOLDOCS(SOLUTIONID);
/
    alter table CRMPROBLEMSOLDOCS 
        add constraint FK8EF9798A3838265E 
        foreign key (SOLUTIONID) 
        references CRMPROBLEMSOLUTIONS (PROSOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK14A3A8F83838265E ON CRMPROBLEMSOLPOSITION(SOLUTIONID);
/
    alter table CRMPROBLEMSOLPOSITION 
        add constraint FK14A3A8F83838265E 
        foreign key (SOLUTIONID) 
        references CRMPROBLEMSOLUTIONS (PROSOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKFAA00F5C9D2652AA ON CRMPRODUCTDEPENDENCY(PRODUCTID);
/
    alter table CRMPRODUCTDEPENDENCY 
        add constraint FKFAA00F5C9D2652AA 
        foreign key (PRODUCTID) 
        references CRMPRODUCT (PRODUCTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK744967159D2652AA ON CRMPRODUCTDIMENSION(PRODUCTID);
/
    alter table CRMPRODUCTDIMENSION 
        add constraint FK744967159D2652AA 
        foreign key (PRODUCTID) 
        references CRMPRODUCT (PRODUCTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FKFAE50B0C9D2652AA ON CRMPRODUCTDOCS(PRODUCTID);
/
    alter table CRMPRODUCTDOCS 
        add constraint FKFAE50B0C9D2652AA 
        foreign key (PRODUCTID) 
        references CRMPRODUCT (PRODUCTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK1BC7F1C8B0FD4C6F ON CRMPROJECTCOST(TASKID);
/
    alter table CRMPROJECTCOST 
        add constraint FK1BC7F1C8B0FD4C6F 
        foreign key (TASKID) 
        references CRMPROJECTTASK (TASKIID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK1BCDE55EB0FD4C6F ON CRMPROJECTPROP(TASKID);
/
    alter table CRMPROJECTPROP 
        add constraint FK1BCDE55EB0FD4C6F 
        foreign key (TASKID) 
        references CRMPROJECTTASK (TASKIID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK1BCF7780A6846C7E ON CRMPROJECTPROP(TASKID);
/
    alter table CRMPROJECTTASK 
        add constraint FK1BCF7780A6846C7E 
        foreign key (PROJECTID) 
        references CRMPROJECT (PROJECTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    CREATE INDEX FK5C3266D83724B25D ON SET_REPORTPARAMETER(REPORTID);
/
    alter table SET_REPORTPARAMETER 
        add constraint FK5C3266D83724B25D 
        foreign key (REPORTID) 
        references SET_REPORTFORMODULE (IDREPORTFORMODULE) ON DELETE CASCADE ON UPDATE CASCADE;
/