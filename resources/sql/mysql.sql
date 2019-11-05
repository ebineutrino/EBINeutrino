/
    create table if not exists  ACCOUNTSTACK (
        ACSTACKID integer not null auto_increment,
        ACCOUNTNR varchar(255),
        ACCOUNT text,
        ACCOUNT_TYPE integer,
        ACCOUNT_TAX_TYPE varchar(255),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  ACCOUNTSTACKDOCS (
        ACCOUNTDOCID integer not null auto_increment,
        ACCOUNTID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (ACCOUNTDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANY (
        COMPANYID integer not null auto_increment,
        COMPANYNUMBER integer,
        CUSTOMERNR varchar(255),
        BEGINCHAR varchar(10),
        NAME varchar(255),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYACTIVITIESDOCS (
        ACTIVITYDOCID integer not null auto_increment,
        ACTIVITYID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (ACTIVITYDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYACTIVITYSTATUS (
        ID integer not null auto_increment,
        NAME varchar(255),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYACTIVITYTYPE (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYCATEGORY (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYCLASSIFICATION (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYCONTACTS (
        CONTACTID integer not null auto_increment,
        COMPANYID integer not null,
        MAINCONTACT bit,
        GENDER varchar(20),
        TITLE varchar(255),
        SURNAME varchar(150),
        NAME varchar(150),
        MITTELNAME varchar(150),
        POSITION varchar(150),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYCOOPERATION (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYHIRARCHIE (
        HIERARCHIEID integer not null auto_increment,
        COMPANYID integer,
        NAME varchar(255),
        ROOT integer,
        PARENT integer,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (HIERARCHIEID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYMEETINGCONTACTS (
        MEETINGCONTACTID integer not null auto_increment,
        MEETINGID integer,
        POS integer,
        GENDER varchar(20),
        SURNAME varchar(150),
        NAME varchar(150),
        MITTELNAME varchar(150),
        POSITION varchar(150),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYMEETINGDOC (
        MEETINGDOCID integer not null auto_increment,
        MEETINGID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (MEETINGDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYMEETINGTYPE (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYNUMBER (
        ID integer not null auto_increment,
        CATEGORYID integer,
        CATEGORY varchar(255),
        BEGINCHAR varchar(10),
        NUMBERFROM integer,
        NUMBERTO integer,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOFFER (
        OFFERID integer not null auto_increment,
        COMPANYID integer,
        OPPORTUNITYID integer,
        OFFERNR varchar(255) not null,
        NAME varchar(255),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOFFERDOCS (
        OFFERDOCID integer not null auto_increment,
        OFFERID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (OFFERDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
        TYPE varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOFFERRECEIVER (
        RECEIVERID integer not null auto_increment,
        OFFERID integer,
        CNUM integer,
        RECEIVERVIA varchar(150),
        GENDER varchar(20),
        SURNAME varchar(150),
        MITTELNAME varchar(150),
        POSITION varchar(150),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOFFERSTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORTUNITY (
        OPPORTUNITYID integer not null auto_increment,
        COMPANYID integer,
        NAME varchar(255),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORTUNITYBGSTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORTUNITYBUSTYP (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORTUNITYCONTACT (
        OPPORTUNITYCONTACTID integer not null auto_increment,
        OPPORTUNITYID integer,
        POS integer,
        GENDER varchar(20),
        SURNAME varchar(150),
        NAME varchar(150),
        MITTELNAME varchar(150),
        STREET varchar(255),
        ZIP varchar(255),
        LOCATION varchar(255),
        PBOX varchar(255),
        COUNTRY varchar(255),
        POSITION varchar(150),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORTUNITYEVSTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORTUNITYSSTAGE (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORTUNITYSTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYOPPORUNITYDOCS (
        DOCID integer not null auto_increment,
        OPPORTUNITYID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (DOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYORDER (
        ORDERID integer not null auto_increment,
        COMPANYID integer,
        ORDERNR varchar(150),
        OFFERID integer,
        NAME varchar(255),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYORDERDOCS (
        ORDERDOCID integer not null auto_increment,
        ORDERID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (ORDERDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
        TYPE varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYORDERRECEIVER (
        RECEIVERID integer not null auto_increment,
        ORDERID integer,
        CNUM integer,
        RECEIVERVIA varchar(50),
        GENDER varchar(20),
        SURNAME varchar(150),
        NAME varchar(150),
        MITTELNAME varchar(150),
        POSITION varchar(150),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYORDERSTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYPRODUCTCATEGORY (
        ID integer not null auto_increment,
        NAME varchar(255),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYPRODUCTTAX (
        ID integer not null auto_increment,
        NAME varchar(255),
        TAXVALUE double precision,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYPRODUCTTAXVALUE (
        ID integer not null auto_increment,
        NAME varchar(255),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYPRODUCTTYPE (
        ID integer not null auto_increment,
        NAME varchar(255),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYSERVICE (
        SERVICEID integer not null auto_increment,
        COMPANYID integer,
        SERVICENR varchar(150),
        NAME varchar(255),
        CATEGORY varchar(255),
        TYPE varchar(255),
        STATUS varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (SERVICEID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYSERVICECATEGORY (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYSERVICEDOCS (
        SERVICEDOCID integer not null auto_increment,
        SERVICEID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (SERVICEDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
        TYPE varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYSERVICEPSOL (
        PROSOLID integer not null auto_increment,
        SERVICEID integer,
        SOLUTIONNR varchar(150),
        NAME varchar(255),
        CLASSIFICATION varchar(255),
        CATEGORY varchar(255),
        TYPE varchar(255),
        STATUS varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (PROSOLID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYSERVICESTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  COMPANYSERVICETYPE (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMADDRESSTYPE (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCALENDAR (
        CALENDARID integer not null auto_increment,
        CUSER varchar(150),
        NAME varchar(255),
        COLOR varchar(15),
        DESCRIPTION text,
        STARTDATE datetime,
        ENDDATE datetime,
        CICON longblob,
        primary key (CALENDARID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCAMPAIGN (
        CAMPAIGNID integer not null auto_increment,
        CAMPAIGNNR varchar(255),
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        NAME varchar(255),
        STATUS varchar(255),
        VALIDFROM datetime,
        VALIDTO datetime,
        primary key (CAMPAIGNID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCAMPAIGNDOCS (
        DOCID integer not null auto_increment,
        CAMPAIGNID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        primary key (DOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCAMPAIGNPOSITION (
        POSITIONID integer not null auto_increment,
        CAMPAIGNID integer,
        PRODUCTID integer,
        PRODUCTNR varchar(255),
        DEDUCTION varchar(5),
        PRODUCTNAME varchar(255),
        QUANTITY bigint,
        NETAMOUNT double precision not null,
        PRETAX double precision not null,
        TAXTYPE varchar(150),
        TYPE varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCAMPAIGNPROP (
        PROPERTIESID integer not null auto_increment,
        CAMPAIGNID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        NAME varchar(255),
        VALUE varchar(255),
        primary key (PROPERTIESID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCAMPAIGNPROPS (
        ID integer not null auto_increment,
        NAME varchar(255) not null,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCAMPAIGNRECEIVER (
        RECEIVERID integer not null auto_increment,
        CAMPAIGNID integer,
        CNUM integer,
        RECEIVERVIA varchar(50),
        COMPANYNAME varchar(255),
        COMPANYNUMBER varchar(255),
        GENDER varchar(20),
        SURNAME varchar(150),
        NAME varchar(150),
        MITTELNAME varchar(150),
        POSITION varchar(150),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMCAMPAIGNSTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMINVOICE (
        INVOICEID integer not null auto_increment,
        ASSOSIATION varchar(150),
        INVOICENR integer,
        BEGINCHAR varchar(10),
        NAME varchar(255),
        STATUS varchar(255),
        CATEGORY varchar(255),
        DATE datetime,
        TAXTYPE varchar(150),
        GENDER varchar(100),
        POSITION varchar(100),
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMINVOICECATEGORY (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMINVOICENUMBER (
        ID integer not null auto_increment,
        CATEGORYID integer,
        CATEGORY varchar(255),
        BEGINCHAR varchar(10),
        NUMBERFROM integer,
        NUMBERTO integer,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
        TYPE varchar(255),
        CATEGORY varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        primary key (POSITIONID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMINVOICESTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROBLEMSOLCATEGORY (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROBLEMSOLCLASS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROBLEMSOLDOCS (
        SOLUTIONDOCID integer not null auto_increment,
        SOLUTIONID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (SOLUTIONDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
        TYPE varchar(255),
        TAXTYPE varchar(255),
        NETAMOUNT double precision,
        PRETAX double precision,
        DIPENDENCYID integer,
        DIMENSIONID integer,
        PICTURE longblob,
        PICTURENAME varchar(255),
        DESCRIPTION text,
        primary key (POSITIONID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROBLEMSOLSTATUS (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROBLEMSOLTYPE (
        ID integer not null auto_increment,
        NAME varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROBLEMSOLUTIONS (
        PROSOLID integer not null auto_increment,
        SERVICENR varchar(150),
        NAME varchar(255),
        CLASSIFICATION varchar(255),
        CATEGORY varchar(255),
        TYPE varchar(255),
        STATUS varchar(255),
        DESCRIPTION text,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (PROSOLID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
        TYPE varchar(255),
        TAXTYPE varchar(255),
        NETAMOUNT double precision,
        PRETAX double precision,
        SALEPRICE double precision,
        PICTURE longblob,
        PICTURENAME varchar(255),
        DESCRIPTION text,
        primary key (PRODUCTID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPRODUCTDIMENSION (
        DIMENSIONID integer not null auto_increment,
        PRODUCTID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        NAME varchar(255),
        VALUE varchar(255),
        primary key (DIMENSIONID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPRODUCTDIMENSIONS (
        ID integer not null auto_increment,
        NAME varchar(255) not null,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPRODUCTDOCS (
        PRODUCTDOCID integer not null auto_increment,
        PRODUCTID integer,
        NAME varchar(255),
        FILES longblob,
        CREATEDFROM varchar(150),
        CREATEDDATE datetime,
        primary key (PRODUCTDOCID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECT (
        PROJECTID integer not null auto_increment,
        PROJECTNR varchar(255),
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        NAME varchar(255),
        MANAGER varchar(255),
        BUDGET double precision,
        ACTUALCOST double precision,
        REMAINCOST double precision,
        STATUS varchar(255),
        VALIDFROM datetime,
        VALIDTO datetime,
        primary key (PROJECTID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTCOST (
        COSTID integer not null auto_increment,
        TASKID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        NAME varchar(255),
        VALUE double precision,
        primary key (COSTID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTCOSTS (
        ID integer not null auto_increment,
        NAME varchar(255) not null,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTPROP (
        PROPERTIESID integer not null auto_increment,
        TASKID integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        CHANGEDDATE datetime,
        CHANGEDFROM varchar(150),
        NAME varchar(255),
        VALUE varchar(255),
        primary key (PROPERTIESID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTPROPS (
        ID integer not null auto_increment,
        NAME varchar(255) not null,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTSTATUS (
        ID integer not null auto_increment,
        NAME varchar(255) not null,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTTASK (
        TASKIID integer not null auto_increment,
        PROJECTID integer,
        TASKID varchar(255) not null,
        PARENTSTASKID text,
        X integer,
        Y integer,
        NAME varchar(255),
        STATUS varchar(255),
        TYPE varchar(255),
        DURATION integer,
        COLOR varchar(12),
        DONE integer,
        DESCRIPTION text,
        primary key (TASKIID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTTASKSTATUS (
        ID integer not null auto_increment,
        NAME varchar(255) not null,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  CRMPROJECTTASKTYPE (
        ID integer not null auto_increment,
        NAME varchar(255) not null,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  EBICRMHISTORY (
        HISTORYID integer not null auto_increment,
        COMPANYID integer,
        CATEGORY varchar(255),
        CHANGEDVALUE text,
        CHANGEDFROM varchar(150),
        CHANGEDDATE datetime,
        primary key (HISTORYID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  EBIDATASTORE (
        ID integer not null auto_increment,
        NAME varchar(255),
        TEXT text,
        EXTRA varchar(255),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  EBIPESSIMISTIC (
        OPTIMISTICID integer not null auto_increment,
        RECORDID integer,
        MODULENAME varchar(255),
        USER varchar(255),
        LOCKDATE datetime,
        STATUS integer,
        primary key (OPTIMISTICID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  MAIL_ASSIGNED (
        ID integer not null auto_increment,
        COMPANYID integer,
        MAIL_DATE datetime,
        SETFROM varchar(150),
        MAIL_FROM varchar(255),
        MAIL_TO varchar(255),
        MAIL_CC varchar(255),
        MAIL_SUBJECT varchar(255),
        MAIL_MESSAGE text,
        ATTACHID varchar(255),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  MAIL_ATTACH (
        ID integer not null auto_increment,
        MAIL_ATTACHID varchar(255),
        FILENAME varchar(255),
        FILEBIN longblob,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  MAIL_DELETED (
        ID integer not null auto_increment,
        MAIL_FROM varchar(255),
        MAIL_TO varchar(255),
        MAIL_CC varchar(255),
        MAIL_SUBJECT varchar(255),
        MAIL_MESSAGE text,
        ATTACHID varchar(255),
        MAIL_DATE datetime,
        SETFROM varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  MAIL_INBOX (
        ID integer not null auto_increment,
        MAIL_FROM varchar(255),
        MAIL_TO varchar(255),
        MAIL_CC varchar(255),
        MAIL_SUBJECT varchar(255),
        MAIL_MESSAGE text,
        ATTACHID varchar(255),
        MAIL_DATE datetime,
        SETFROM varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  MAIL_OUTBOX (
        ID integer not null auto_increment,
        MAIL_FROM varchar(255),
        MAIL_TO varchar(255),
        MAIL_CC varchar(255),
        MAIL_SUBJECT varchar(255),
        MAIL_MESSAGE text,
        ATTACHID varchar(255),
        MAIL_DATE datetime,
        SETFROM varchar(150),
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  MAIL_TEMPLATE (
        ID integer not null auto_increment,
        SETFROM varchar(150),
        SETDATE datetime,
        NAME varchar(255),
        TEMPLATE text,
        ISACTIVE bit,
        primary key (ID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
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
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    create table if not exists  SET_REPORTPARAMETER (
        PARAMID integer not null auto_increment,
        REPORTID integer,
        POSITION integer,
        CREATEDDATE datetime,
        CREATEDFROM varchar(150),
        PARAMNAME varchar(150),
        PARAMTYPE varchar(150),
        primary key (PARAMID)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;
/
    alter table ACCOUNTSTACKDOCS 
        add index FKA2D1D936EDEA7D2 (ACCOUNTID), 
        add constraint FKA2D1D936EDEA7D2 
        foreign key (ACCOUNTID) 
        references ACCOUNTSTACK (ACSTACKID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYACTIVITIES 
        add index FK96B2E00A4B8DEA26 (COMPANYID), 
        add constraint FK96B2E00A4B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYACTIVITIESDOCS 
        add index FK42EFE545260096A5 (ACTIVITYID), 
        add constraint FK42EFE545260096A5 
        foreign key (ACTIVITYID) 
        references COMPANYACTIVITIES (ACTIVITYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYADDRESS 
        add index FKC68CACD74B8DEA26 (COMPANYID), 
        add constraint FKC68CACD74B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYBANK 
        add index FK620C50194B8DEA26 (COMPANYID), 
        add constraint FK620C50194B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYCONTACTADDRESS 
        add index FK3F24D9718586275C (CONTACTID), 
        add constraint FK3F24D9718586275C 
        foreign key (CONTACTID) 
        references COMPANYCONTACTS (CONTACTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYCONTACTS 
        add index FK31DA6BB04B8DEA26 (COMPANYID), 
        add constraint FK31DA6BB04B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYHIRARCHIE 
        add index FK339E62E64B8DEA26 (COMPANYID), 
        add constraint FK339E62E64B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYMEETINGCONTACTS 
        add index FKB868AD71E2F680DB (MEETINGID), 
        add constraint FKB868AD71E2F680DB 
        foreign key (MEETINGID) 
        references COMPANYMEETINGPROTOCOL (MEETINGPROTOCOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYMEETINGDOC 
        add index FK4C71713AE2F680DB (MEETINGID), 
        add constraint FK4C71713AE2F680DB 
        foreign key (MEETINGID) 
        references COMPANYMEETINGPROTOCOL (MEETINGPROTOCOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYMEETINGPROTOCOL 
        add index FK9F45DBB64B8DEA26 (COMPANYID), 
        add constraint FK9F45DBB64B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYNUMBER 
        add index FK25CC1DE610245D65 (CATEGORYID), 
        add constraint FK25CC1DE610245D65 
        foreign key (CATEGORYID) 
        references COMPANYCATEGORY (ID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYOFFER 
        add index FKE0370BFF4B8DEA26 (COMPANYID), 
        add constraint FKE0370BFF4B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYOFFERDOCS 
        add index FK95E30EBA33647FC5 (OFFERID), 
        add constraint FK95E30EBA33647FC5 
        foreign key (OFFERID) 
        references COMPANYOFFER (OFFERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYOFFERPOSITIONS 
        add index FKF6859F2B33647FC5 (OFFERID), 
        add constraint FKF6859F2B33647FC5 
        foreign key (OFFERID) 
        references COMPANYOFFER (OFFERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYOFFERRECEIVER 
        add index FK589876EE33647FC5 (OFFERID), 
        add constraint FK589876EE33647FC5 
        foreign key (OFFERID) 
        references COMPANYOFFER (OFFERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYOPPORTUNITY 
        add index FK2957C6364B8DEA26 (COMPANYID), 
        add constraint FK2957C6364B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYOPPORTUNITYCONTACT 
        add index FK6C7FA10A9A49D973 (OPPORTUNITYID), 
        add constraint FK6C7FA10A9A49D973 
        foreign key (OPPORTUNITYID) 
        references COMPANYOPPORTUNITY (OPPORTUNITYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYOPPORUNITYDOCS 
        add index FKE5037C3B9A49D973 (OPPORTUNITYID), 
        add constraint FKE5037C3B9A49D973 
        foreign key (OPPORTUNITYID) 
        references COMPANYOPPORTUNITY (OPPORTUNITYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYORDER 
        add index FKE03C78F14B8DEA26 (COMPANYID), 
        add constraint FKE03C78F14B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYORDERDOCS 
        add index FKAA4B2AC47C7E529 (ORDERID), 
        add constraint FKAA4B2AC47C7E529 
        foreign key (ORDERID) 
        references COMPANYORDER (ORDERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYORDERPOSITIONS 
        add index FK1546A27947C7E529 (ORDERID), 
        add constraint FK1546A27947C7E529 
        foreign key (ORDERID) 
        references COMPANYORDER (ORDERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYORDERRECEIVER 
        add index FK8B22D1E047C7E529 (ORDERID), 
        add constraint FK8B22D1E047C7E529 
        foreign key (ORDERID) 
        references COMPANYORDER (ORDERID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYSERVICE 
        add index FK8138D7984B8DEA26 (COMPANYID), 
        add constraint FK8138D7984B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYSERVICEDOCS 
        add index FK37365D33F54B1B7 (SERVICEID), 
        add constraint FK37365D33F54B1B7 
        foreign key (SERVICEID) 
        references COMPANYSERVICE (SERVICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYSERVICEPOSITIONS 
        add index FK233CCFB23F54B1B7 (SERVICEID), 
        add constraint FK233CCFB23F54B1B7 
        foreign key (SERVICEID) 
        references COMPANYSERVICE (SERVICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table COMPANYSERVICEPSOL 
        add index FK378EAB83F54B1B7 (SERVICEID), 
        add constraint FK378EAB83F54B1B7 
        foreign key (SERVICEID) 
        references COMPANYSERVICE (SERVICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMCAMPAIGNDOCS 
        add index FK2F505C0984114DEA (CAMPAIGNID), 
        add constraint FK2F505C0984114DEA 
        foreign key (CAMPAIGNID) 
        references CRMCAMPAIGN (CAMPAIGNID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMCAMPAIGNPOSITION 
        add index FK11B733F784114DEA (CAMPAIGNID), 
        add constraint FK11B733F784114DEA 
        foreign key (CAMPAIGNID) 
        references CRMCAMPAIGN (CAMPAIGNID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMCAMPAIGNPROP 
        add index FK2F55DD3184114DEA (CAMPAIGNID), 
        add constraint FK2F55DD3184114DEA 
        foreign key (CAMPAIGNID) 
        references CRMCAMPAIGN (CAMPAIGNID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMCAMPAIGNRECEIVER 
        add index FKB4F084BD84114DEA (CAMPAIGNID), 
        add constraint FKB4F084BD84114DEA 
        foreign key (CAMPAIGNID) 
        references CRMCAMPAIGN (CAMPAIGNID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMINVOICENUMBER 
        add index FKC7921F987D593035 (CATEGORYID), 
        add constraint FKC7921F987D593035 
        foreign key (CATEGORYID) 
        references CRMINVOICECATEGORY (ID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMINVOICEPOSITION 
        add index FKC88C01B8FB72A7A6 (INVOICEID), 
        add constraint FKC88C01B8FB72A7A6 
        foreign key (INVOICEID) 
        references CRMINVOICE (INVOICEID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPROBLEMSOLDOCS 
        add index FK8EF9798A3838265E (SOLUTIONID), 
        add constraint FK8EF9798A3838265E 
        foreign key (SOLUTIONID) 
        references CRMPROBLEMSOLUTIONS (PROSOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPROBLEMSOLPOSITION 
        add index FK14A3A8F83838265E (SOLUTIONID), 
        add constraint FK14A3A8F83838265E 
        foreign key (SOLUTIONID) 
        references CRMPROBLEMSOLUTIONS (PROSOLID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPRODUCTDEPENDENCY 
        add index FKFAA00F5C9D2652AA (PRODUCTID), 
        add constraint FKFAA00F5C9D2652AA 
        foreign key (PRODUCTID) 
        references CRMPRODUCT (PRODUCTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPRODUCTDIMENSION 
        add index FK744967159D2652AA (PRODUCTID), 
        add constraint FK744967159D2652AA 
        foreign key (PRODUCTID) 
        references CRMPRODUCT (PRODUCTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPRODUCTDOCS 
        add index FKFAE50B0C9D2652AA (PRODUCTID), 
        add constraint FKFAE50B0C9D2652AA 
        foreign key (PRODUCTID) 
        references CRMPRODUCT (PRODUCTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPROJECTCOST 
        add index FK1BC7F1C8B0FD4C6F (TASKID), 
        add constraint FK1BC7F1C8B0FD4C6F 
        foreign key (TASKID) 
        references CRMPROJECTTASK (TASKIID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPROJECTPROP 
        add index FK1BCDE55EB0FD4C6F (TASKID), 
        add constraint FK1BCDE55EB0FD4C6F 
        foreign key (TASKID) 
        references CRMPROJECTTASK (TASKIID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table CRMPROJECTTASK 
        add index FK1BCF7780A6846C7E (PROJECTID), 
        add constraint FK1BCF7780A6846C7E 
        foreign key (PROJECTID) 
        references CRMPROJECT (PROJECTID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table MAIL_ASSIGNED 
        add index FKCA87AF564B8DEA26 (COMPANYID), 
        add constraint FKCA87AF564B8DEA26 
        foreign key (COMPANYID) 
        references COMPANY (COMPANYID) ON DELETE CASCADE ON UPDATE CASCADE;
/
    alter table SET_REPORTPARAMETER 
        add index FK5C3266D83724B25D (REPORTID), 
        add constraint FK5C3266D83724B25D 
        foreign key (REPORTID) 
        references SET_REPORTFORMODULE (IDREPORTFORMODULE) ON DELETE CASCADE ON UPDATE CASCADE;
/