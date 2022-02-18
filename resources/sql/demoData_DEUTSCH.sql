INSERT INTO COMPANY (COMPANYID, COMPANYNUMBER, CUSTOMERNR, BEGINCHAR, `NAME`, NAME2, PHONE, FAX, EMAIL, EMPLOYEE, QUALIFICATION, CATEGORY, COOPERATION, ISLOCK, WEB, TAXNUMBER, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE, ISACTUAL) VALUES
(1, 1, 'ACV13244', 'B2B', 'EBI Neutrino R1 Open Source CRM ERP', 'Software Solution', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', '200', 'A', 'B2B Customer', 'Partner', false, 'www.ebineutrino.org', 'VAT392839389', 'EBI Neutrino R1 the open source crm erp framework', 'root', '2010-10-31', NULL, NULL, true);

INSERT INTO COMPANYACTIVITIES (ACTIVITYID, COMPANYID, ACTIVITYNAME, ACTIVITYTYPE, ACTIVITYSTATUS, DUEDATE, DURATION, ACOLOR, ACTIVITYCLOSEDATE, ACTIVITYISCLOSED, ACTIVITYDESCRIPTION, ISLOCK, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'Schreibe EMail', 'EMail', 'Offen', '2010-10-01 12:00:00', 30, '204,0,0', NULL, NULL, 'Write email to a customer', NULL, 'root', '2010-10-31', NULL, NULL),
(2, 1, 'Telefon Meeting', 'Meeting', 'Offen', '2010-10-17 12:00:00', 20, '51,153,255', NULL, NULL, '', NULL, 'root', '2010-10-31', NULL, NULL),
(3, 1, 'Schreibe Brief', 'Brief', 'Offen', '2010-10-13 12:00:00', 33, '5,125,255', NULL, NULL, 'write letter', NULL, 'root', '2010-10-31', NULL, NULL),
(4, 1, 'Fax senden', 'Fax', 'Offen', '2010-10-31 12:00:00', 20, '102,255,102', NULL, NULL, 'send a fax', NULL, 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYACTIVITYSTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen');

INSERT INTO COMPANYACTIVITYTYPE (ID, `NAME`) VALUES
(1, 'EMail'),
(2, 'Fax'),
(3, 'Brief'),
(4, 'Meeting'),
(5, 'Telefon');

INSERT INTO COMPANYADDRESS (ADDRESSID, COMPANYID, ADDRESSTYPE, STREET, ZIP, LOCATION, PBOX, COUNTRY, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'Business', 'Newstreet', '3839898', 'New City', '9023983', 'NewCountry', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYBANK (BANKID, COMPANYID, BANKNAME, BANKBSB, BANKACCOUNT, BANKBIC, BANKIBAN, BANKCOUNTRY, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'New Bank', '98398398', '90389398', 'BIC09309309', 'IBAN034890309', 'New Country', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYCATEGORY (ID, `NAME`) VALUES
(1, 'B2B Customer'),
(2, 'B2C Customer');

INSERT INTO COMPANYCLASSIFICATION (ID, `NAME`) VALUES
(1, 'A'),
(2, 'B'),
(3, 'C');

INSERT INTO COMPANYCONTACTS (CONTACTID, COMPANYID, MAINCONTACT, GENDER, TITLE, SURNAME, `NAME`, MITTELNAME, POSITION, BIRDDATE, PHONE, FAX, MOBILE, EMAIL, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, true, 'Mr', 'Prof.', 'Newone', 'James', '', 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYCONTACTADDRESS (ADDRESSID, CONTACTID, ADDRESSTYPE, STREET, ZIP, LOCATION, COUNTRY, PBOX, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'Business', 'Newstreet', '3839898', 'New City', 'NewCountry', '9023983', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYCOOPERATION (ID, `NAME`) VALUES
(1, 'Partner'),
(2, 'Verkäufer'),
(3, 'WiederVerkäufer'),
(4, 'Hersteller');

INSERT INTO COMPANYMEETINGPROTOCOL (MEETINGPROTOCOLID, COMPANYID, MEETINGSUBJECT, MEETINGTYPE, PROTOCOL, METINGDATE, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'Meeting über service', 'Meeting A', 'A meeting is a gathering of two or more people that has been convened for the purpose of achieving a common goal through verbal interaction, such as sharing information or reaching agreement. [2] Meetings may occur face to face or virtually, as mediated by communications technology, such as a telephone conference call, a skyped conference call or a videoconference.\n\nThus, a meeting may be distinguished from other gatherings, such as a chance encounter (not convened), a sports game or a concert (verbal interaction is incidental), a party or the company of friends (no common goal is to be achieved) and a demonstration (whose common goal is achieved mainly through the number of demonstrators present, not verbal interaction).', '2010-10-31', 'root', '2010-10-31', NULL, NULL),
(2, 1, 'New Meeting', 'Meeting B', 'A meeting is a gathering of two or more people that has been convened for the purpose of achieving a common goal through verbal interaction, such as sharing information or reaching agreement. [2] Meetings may occur face to face or virtually, as mediated by communications technology, such as a telephone conference call, a skyped conference call or a videoconference.\n\nThus, a meeting may be distinguished from other gatherings, such as a chance encounter (not convened), a sports game or a concert (verbal interaction is incidental), a party or the company of friends (no common goal is to be achieved) and a demonstration (whose common goal is achieved mainly through the number of demonstrators present, not verbal interaction).', '2010-10-31', 'root', '2010-10-31', NULL, NULL),
(3, 1, 'Meeting 2', 'Meeting C', 'Beginning with the early experimental work of the Gestaltists in Germany (e.g. Duncker, 1935 [2]), and continuing through the 1960s and early 1970s, research on problem solving typically conducted relatively simple, laboratory tasks (e.g. Duncker''s "X-ray" problem; Ewert & Lambert''s 1932 "disk" problem, later known as Tower of Hanoi) that appeared novel to participants (e.g. Mayer, 1992 [3]). Various reasons account for the choice of simple novel tasks: they had clearly defined optimal solutions, they were solvable within a relatively short time frame, researchers could trace participants'' problem-solving steps, and so on. The researchers made the underlying assumption, of course, that simple tasks such as the Tower of Hanoi captured the main properties of "real world" problems, and that the cognitive processes underlying participants'' attempts to solve simple problems were representative of the processes engaged in when solving "real world" problems. Thus researchers used simple problems for reasons of convenience, and thought generalizations to more complex problems would become possible. Perhaps the best-known and most impressive example of this line of research remains the work by Allen Newell and Herbert Simon [4].\n\n', NULL, 'root', '2010-10-31', NULL, NULL),
(4, 1, 'Meeting A', 'Meeting A', 'Meeting A', '2010-10-06', 'root', '2010-10-31', NULL, NULL),
(5, 1, 'Meeting B', 'Meeting B', 'Meeting B', NULL, 'root', '2010-10-31', NULL, NULL),
(6, 1, 'Meeting C', 'Meeting C', 'Meeting C', '2010-10-14', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYMEETINGTYPE (ID, `NAME`) VALUES
(1, 'Meeting A'),
(2, 'Meeting B'),
(3, 'Meeting C');

INSERT INTO COMPANYNUMBER (ID, CATEGORYID, CATEGORY, BEGINCHAR, NUMBERFROM, NUMBERTO) VALUES
(1, 1, 'B2B Customer', 'B2B', 1, 200);

INSERT INTO COMPANYOFFER (OFFERID, COMPANYID, OPPORTUNITYID, OFFERNR, `NAME`, `STATUS`, OFFERVERSION, OFFERDATE, VALIDTO, ISRECIEVED, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 1, 'OFR239384', 'Warscheinlichkeit ABC23494', 'Offen', NULL, '2010-10-01', '2010-10-31', false, 'Opportunity ABC23494', 'root', '2010-10-31', NULL, NULL),
(2, 1, NULL, '234324', 'Warscheinlichkeit ABD8383', 'Akzeptiert', NULL, '2010-10-07', '2010-10-07', false, 'test Opportunity', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYOFFERPOSITIONS (POSITIONID, OFFERID, PRODUCTID, PRODUCTNR, DEDUCTION, PRODUCTNAME, QUANTITY, NETAMOUNT, PRETAX, TAXTYPE, `TYPE`, CATEGORY, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDDATE, CHANGEDFROM) VALUES
(1, 1, NULL, 'FRT238383', '', 'Apfel', 500, 2, 1.5, 'IVA', 'Apple', 'Fruit', 'Mala di qualitá', 'root', '2010-10-31', NULL, NULL),
(2, 2, NULL, 'FRT238383', '', 'Apfel', 1, 2, 1.5, 'IVA', 'Apple', 'Fruit', 'Mala di qualitá', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYOFFERRECEIVER (RECEIVERID, OFFERID, CNUM, RECEIVERVIA, GENDER, SURNAME, MITTELNAME, POSITION, EMAIL, PHONE, FAX, STREET, ZIP, LOCATION, PBOX, COUNTRY, CREATEDFROM, CREATEDDATE, `NAME`) VALUES
(1, 1, 1, 'EMail', 'Mr', 'Newone', NULL, 'CEO', 'info@ebineutrino.org', '+49 8474 8493983', '+49 8474 8493983', 'Newstreet', '3839898', 'New City', '9023983', 'NewCountry', 'root', '2010-10-31', 'James'),
(2, 2, 0, 'EMail', 'Mr', 'Newone', NULL, 'CEO', 'info@ebineutrino.org', '+49 8474 8493983', '+49 8474 8493983', 'Newstreet', '3839898', 'New City', '9023983', 'NewCountry', 'root', '2010-10-31', 'James');

INSERT INTO COMPANYOFFERSTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'Verworfen'),
(4, 'Akzeptiert'),
(5, 'Unbekannt');

INSERT INTO COMPANYOPPORTUNITY (OPPORTUNITYID, COMPANYID, `NAME`, SALESTAGE, PROBABILITY, OPPORTUNITYVALUE, ISCLOSE, CLOSEDATE, BUSINESSTYPE, EVALUATIONSTATUS, EVALUETIONDATE, BUDGETSTATUS, BUDGETDATE, SALESTAGEDATE, OPPORTUNITYSTATUS, OPPORTUNITYSTATUSDATE, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'Opportunity ABC23494', 'Akzeptiert', '50%', 22, false, NULL, 'Hardware', 'In bearbeitung', '2010-10-31', 'Akzeptiert', '2010-10-31', '2010-10-31', 'Akzeptiert', '2010-10-31', 'Opportunity ABC23494', 'root', '2010-10-31', NULL, NULL),
(2, 1, 'test opportunity', 'Akzeptiert', '10%', 324324, false, NULL, 'Hardware', 'Akzeptiert', '2010-10-31', 'Akzeptiert', '2010-10-31', '2010-10-31', 'Akzeptiert', '2010-10-31', '', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYOPPORTUNITYBGSTATUS (ID, `NAME`) VALUES
(1, 'In Bearbeitung'),
(2, 'Unbekannt'),
(3, 'Akzeptiert');

INSERT INTO COMPANYOPPORTUNITYBUSTYP (ID, `NAME`) VALUES
(1, 'Software'),
(2, 'Hardware'),
(3, 'Mechanik'),
(4, 'Service');

INSERT INTO COMPANYOPPORTUNITYCONTACT (OPPORTUNITYCONTACTID, OPPORTUNITYID, POS, GENDER, SURNAME, `NAME`, MITTELNAME, POSITION, BIRDDATE, PHONE, FAX, MOBILE, EMAIL, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL),
(2, 2, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYOPPORTUNITYEVSTATUS (ID, `NAME`) VALUES
(1, 'In Bearbeitung'),
(2, 'Akzeptiert'),
(3, 'Start'),
(4, 'Ende'),
(5, 'Unbekannt');

INSERT INTO COMPANYOPPORTUNITYSSTAGE (ID, `NAME`) VALUES
(1, 'Akzeptiert'),
(2, 'Verworfen'),
(3, 'In Bearbeitung'),
(4, 'Geschlossen');

INSERT INTO COMPANYOPPORTUNITYSTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'Erfolgreich');

INSERT INTO COMPANYORDER (ORDERID, COMPANYID, ORDERNR, OFFERID, `NAME`, INVOICECREATED, `STATUS`, ORDERVERSION, OFFERDATE, VALIDTO, ISRECIEVED, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'ORD239384', 1, 'Bestellung ABC23494', NULL, 'Offen', NULL, '2010-10-01', '2010-10-31', false, 'Warscheinlichkeit ABC23494', 'root', '2010-10-31', 'root', '2010-10-31'),
(2, 1, '234324', 2, 'Bestellung ABD83838', NULL, 'Akzeptiert', NULL, '2010-10-19', '2010-10-27', false, 'Warscheinlichkeit', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYORDERPOSITIONS (POSITIONID, ORDERID, PRODUCTID, DEDUCTION, PRODUCTNR, PRODUCTNAME, QUANTITY, NETAMOUNT, PRETAX, TAXTYPE, `TYPE`, CATEGORY, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDDATE, CHANGEDFROM) VALUES
(1, 1, NULL, '', 'FRT238383', 'Apfel', 500, 2, 1.5, 'IVA', 'Apple', 'Fruit', 'Qualität Apfel', NULL, NULL, NULL, NULL),
(4, 2, NULL, '', 'FRT238383', 'Apfel', 1, 2, 1.5, 'IVA', 'Apple', 'Fruit', 'Qualität Apfel', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYORDERRECEIVER (RECEIVERID, ORDERID, CNUM, RECEIVERVIA, GENDER, SURNAME, `NAME`, MITTELNAME, POSITION, EMAIL, PHONE, FAX, STREET, ZIP, LOCATION, PBOX, COUNTRY, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, NULL, 'EMail', 'Mr', 'Newone', 'James', NULL, 'CEO', 'info@ebineutrino.org', NULL, '+49 8474 8493983', 'Newstreet', '3839898', 'New City', '9023983', 'NewCountry', NULL, NULL, NULL, NULL),
(3, 2, 0, 'EMail', 'Mr', 'Newone', 'James', NULL, 'CEO', 'info@ebineutrino.org', '+49 8474 8493983', '+49 8474 8493983', 'Newstreet', '3839898', 'New City', '9023983', 'NewCountry', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYORDERSTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'Akzeptiert'),
(4, 'Verworfen'),
(5, 'Unbekannt');

INSERT INTO COMPANYPRODUCTCATEGORY (ID, `NAME`) VALUES
(1, 'Hardware'),
(2, 'Software'),
(3, 'Mechanik'),
(4, 'Service'),
(5, 'Fruit');

INSERT INTO COMPANYPRODUCTTAX (ID, `NAME`, TAXVALUE, CREATEDDATE, CREATEDFROM, CHANGEDDATE, CHANGEDFROM) VALUES
(1, 'MwSt', 19, '2010-10-30', 'root', NULL, NULL);

INSERT INTO COMPANYPRODUCTTAXVALUE (ID, `NAME`) VALUES
(1, 'MwSt');

INSERT INTO COMPANYPRODUCTTYPE (ID, `NAME`) VALUES
(1, 'Mainbord'),
(2, 'Motor'),
(3, 'Bremse'),
(4, 'Windows'),
(5, 'Linux'),
(6, 'Support'),
(7, 'Apfel'),
(8, 'Tomaten');

INSERT INTO COMPANYSERVICE (SERVICEID, COMPANYID, SERVICENR, `NAME`, CATEGORY, `TYPE`, `STATUS`, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'ORD239384', 'Bestellung ABC23494', 'Fruit', 'Apple', 'Open', 'Angebot ABC23494', 'root', '2010-10-31', NULL, NULL),
(2, 1, '234324ddd', 'Bestellung x', 'Fruit', 'Apple', 'Offen', 'Angebot', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYSERVICECATEGORY (ID, `NAME`) VALUES
(1, 'Fruit'),
(2, 'Hardware'),
(3, 'Software'),
(4, 'Mechanik');

INSERT INTO COMPANYSERVICEPOSITIONS (POSITIONID, SERVICEID, PRODUCTID, DEDUCTION, PRODUCTNR, PRODUCTNAME, QUANTITY, NETAMOUNT, PRETAX, TAXTYPE, `TYPE`, CATEGORY, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDDATE, CHANGEDFROM) VALUES
(1, 1, NULL, '', 'FRT238383', 'Apfel', 500, 2, 1.5, 'IVA', 'Apfel', 'Fruit', 'Qualität Apfel', NULL, NULL, NULL, NULL),
(3, 2, NULL, '', 'FRT238383', 'Apfel', 1, 2, 1.5, 'IVA', 'Apfel', 'Fruit', 'Qualität Apfel', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYSERVICEPSOL (PROSOLID, SERVICEID, SOLUTIONNR, `NAME`, CLASSIFICATION, CATEGORY, `TYPE`, `STATUS`, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, 'PRO938494', 'Fruit', 'Please select', 'Fruit', 'Friut Service', 'Open', 'Problem solving is a mental process and is part of the larger problem process that includes problem finding and problem shaping. \nConsidered the most complex of all intellectual functions, problem solving has been defined as higher-order cognitive process that requires the modulation and \ncontrol of more routine or fundamental skills.[1] Problem solving occurs when an organism or an artificial intelligence system needs to move from a given state to a desired goal state.', 'root', '2010-10-31', NULL, NULL),
(3, 2, 'PRO938494', 'Fruit', 'Please select', 'Fruit', 'Friut Service', 'Open', 'Problem solving is a mental process and is part of the larger problem process that includes problem finding and problem shaping. \nConsidered the most complex of all intellectual functions, problem solving has been defined as higher-order cognitive process that requires the modulation and \ncontrol of more routine or fundamental skills.[1] Problem solving occurs when an organism or an artificial intelligence system needs to move from a given state to a desired goal state.', 'root', '2010-10-31', NULL, NULL);

INSERT INTO COMPANYSERVICESTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'Verworfen');

INSERT INTO COMPANYSERVICETYPE (ID, `NAME`) VALUES
(1, 'Apple'),
(2, 'Software bug'),
(3, 'Hardware bug'),
(4, 'Mechanics problem');

INSERT INTO CRMADDRESSTYPE (ID, `NAME`) VALUES
(1, 'Home'),
(2, 'Lieferadresse'),
(3, 'Business'),
(4, 'Privat');

INSERT INTO CRMINVOICE (INVOICEID, ASSOSIATION, INVOICENR, BEGINCHAR, `NAME`, `STATUS`, CATEGORY, `DATE`, TAXTYPE, GENDER, POSITION, COMPANYNAME, CONTACTNAME, CONTACTSURNAME, CONTACTSTREET, CONTACTZIP, CONTACTLOCATION, CONTACTPOSTCODE, CONTACTCOUNTRY, CONTACTTELEPHONE, CONTACTFAX, CONTACTEMAIL, CONTACTWEB, CONTACTDESCRIPTION, CREATEDDATE, CREATEDFROM, CHANGEDDATE, CHANGEDFROM) VALUES
(1, 'Order: 1', 3000, 'FRT', 'Invoice ABC23494', 'Open', 'Fruit', '2010-10-31', 'IVA', 'Mr', 'CEO', 'EBI Neutrino R1 Open Source CRM ERP', 'James', 'Newone', 'Newstreet', '3839898', 'New City', '9023983', 'NewCountry', '', '+49 8474 8493983', 'info@ebineutrino.org', 'www.ebineutrino.org', 'Some description', '2010-10-31', 'root', NULL, NULL),
(2, 'Service: 2', 3001, 'FRT', 'Rechnung', 'Open', 'Fruit', '2010-10-31', 'Please select', 'Mr', 'CEO', 'EBI Neutrino R1 Open Source CRM ERP', 'James', 'Newone', 'Newstreet', '3839898', 'New City', '9023983', 'NewCountry', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'www.ebineutrino.org', 'CEO of company', '2010-10-31', 'root', NULL, NULL);

INSERT INTO CRMINVOICECATEGORY (ID, `NAME`) VALUES
(1, 'Hardware'),
(2, 'Software'),
(3, 'Mechanik'),
(4, 'Fruit'),
(5, 'Service'),
(6, 'Cash');

INSERT INTO CRMINVOICENUMBER (ID, CATEGORYID, CATEGORY, BEGINCHAR, NUMBERFROM, NUMBERTO) VALUES
(1, 1, 'Hardware', 'HRD', 1, 1000),
(2, 2, 'Software', 'SOFT', 1000, 2000),
(3, 3, 'Mechanik', 'MEC', 2000, 3000),
(4, 4, 'Fruit', 'FRT', 3000, 4000),
(5, 5, 'Service', 'SERV', 4000, 5000),
(6, 6, 'Cash', 'CSH', 5000, 100000);

INSERT INTO CRMINVOICEPOSITION (POSITIONID, INVOICEID, PRODUCTID, DEDUCTION, PRODUCTNR, PRODUCTNAME, QUANTITY, NETAMOUNT, PRETAX, TAXTYPE, `TYPE`, CATEGORY, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDDATE, CHANGEDFROM) VALUES
(1, 1, NULL, '', 'FRT238383', 'Mela', 500, 2, 1.5, 'IVA', 'Apple', 'Fruit', 'Mala di qualitá', 'root', '2010-10-31', NULL, NULL),
(3, 2, NULL, '', 'FRT238383', 'Mela', 1, 2, 1.5, 'IVA', 'Apple', 'Fruit', 'Mala di qualitá', 'root', '2010-10-31', NULL, NULL);

INSERT INTO CRMINVOICESTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'Bezahlt'),
(4, 'Geliefert');

INSERT INTO CRMPROBLEMSOLCATEGORY (ID, `NAME`) VALUES
(1, 'Hardware'),
(2, 'Software'),
(3, 'Mechanik'),
(4, 'Fruit'),
(5, 'Support');

INSERT INTO CRMPROBLEMSOLCLASS (ID, `NAME`) VALUES
(1, 'A'),
(2, 'B'),
(3, 'C');

INSERT INTO CRMPROBLEMSOLSTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'Verworfen');

INSERT INTO CRMPROBLEMSOLTYPE (ID, `NAME`) VALUES
(1, 'Software Service'),
(2, 'Hardware Service'),
(3, 'Friut Service'),
(4, 'Mechanics Service');

INSERT INTO CRMPROBLEMSOLUTIONS (PROSOLID, SERVICENR, `NAME`, CLASSIFICATION, CATEGORY, `TYPE`, `STATUS`, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 'PRO938494', 'Fruit', 'Please select', 'Fruit', 'Friut Service', 'Open', 'Problem solving is a mental process and is part of the larger problem process that includes problem finding and problem shaping. \nConsidered the most complex of all intellectual functions, problem solving has been defined as higher-order cognitive process that requires the modulation and \ncontrol of more routine or fundamental skills.[1] Problem solving occurs when an organism or an artificial intelligence system needs to move from a given state to a desired goal state.', 'root', '2010-10-31', NULL, NULL);

INSERT INTO CRMPROBLEMSOLPOSITION (PRODUCTID, SOLUTIONID, CREATEDDATE, CREATEDFROM, CHANGEDDATE, CHANGEDFROM, PRODUCTNR, PRODUCTNAME, CATEGORY, `TYPE`, TAXTYPE, NETAMOUNT, PRETAX, DIPENDENCYID, DIMENSIONID, PICTURE, PICTURENAME, DESCRIPTION) VALUES
(1, 1, '2010-10-31', 'root', NULL, NULL, 'FRT238383', 'Mela', 'Fruit', 'Apple', 'IVA', 2, 1.5, NULL, NULL, NULL, NULL, 'Mela di qualitá');

INSERT INTO CRMPRODUCTDIMENSION (DIMENSIONID, PRODUCTID, CREATEDDATE, CREATEDFROM, CHANGEDDATE, CHANGEDFROM, `NAME`, `VALUE`) VALUES
(1, 1, '2010-10-30', 'root', NULL, NULL, 'Anzahl', '10 kg'),
(2, 1, '2010-10-30', 'root', NULL, NULL, 'Maß', '2 cm'),
(3, 1, '2010-10-30', 'root', NULL, NULL, 'Abmessungen', '1,50');

INSERT INTO CRMPRODUCTDIMENSIONS (ID, `NAME`) VALUES
(1, 'Abmessungen'),
(2, 'Anzahl'),
(3, 'Maß'),
(4, 'Gewicht'),
(5, 'Extras'),
(6, 'Farbe'),
(7, 'Rabatt');


INSERT INTO CRMPROJECTCOSTS (ID, `NAME`) VALUES
(1, 'Planung'),
(2, 'Entwicklung'),
(3, 'Service'),
(4, 'Arbeiten'),
(5, 'Support');


INSERT INTO CRMPROJECTPROPS (ID, `NAME`) VALUES
(1, 'Zeit'),
(2, 'Dauer'),
(3, 'Abmessung'),
(4, 'Mitarbeiter'),
(5, 'Maß'),
(6, 'Extras'),
(7, 'Extras1');

INSERT INTO CRMPROJECTSTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'In Bearbeitung'),
(4, 'Abgelehnt'),
(5, 'Erfolgreich');


INSERT INTO CRMPROJECTTASKSTATUS (ID, `NAME`) VALUES
(1, 'Offen'),
(2, 'Geschlossen'),
(3, 'Abgeschlossen'),
(4, 'Unbekannt');

INSERT INTO CRMPROJECTTASKTYPE (ID, `NAME`) VALUES
(1, 'Hardware Service'),
(2, 'Hadware Support'),
(3, 'Hardware Entwicklung'),
(4, 'Software Service'),
(5, 'Software Support'),
(6, 'Entwicklung'),
(7, 'Plannung'),
(8, 'Arbeit'),
(9, 'Support'),
(10, 'Service');

INSERT INTO COMPANYMEETINGCONTACTS (MEETINGCONTACTID, MEETINGID, POS, GENDER, SURNAME, `NAME`, MITTELNAME, POSITION, BIRDDATE, PHONE, FAX, MOBILE, EMAIL, DESCRIPTION, CREATEDFROM, CREATEDDATE, CHANGEDFROM, CHANGEDDATE) VALUES
(1, 1, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL),
(2, 2, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL),
(3, 3, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL),
(4, 4, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL),
(5, 5, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL),
(6, 6, NULL, 'Mr', 'Newone', 'James', NULL, 'CEO', '1960-01-29', '+49 8474 8493983', '+49 8474 8493983', '+49 8474 8493983', 'info@ebineutrino.org', 'CEO of company', 'root', '2010-10-31', NULL, NULL);

INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(1, 0,'100', 'IT Software', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(2, 0,'101', 'Konzession', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(3, 0,'102', 'Lizenz Verträge', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(4, 0, '103', 'Firmenwerte', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(5, 0, '104', 'Bauten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(6, 0, '105', 'Garagen', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(7, 0, '106', 'Maschinen', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(8, 0, '107', 'Anlagen', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(9, 0, '108', 'PKW', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(10, 0, '109', 'LKW', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(11, 0, '110', 'Einrichtungen', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(12, 0, '111', 'Büroeinrichtungen', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(13, 0, '112', 'Werkzeuge', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(14, 0, '113', 'Beteiligungen', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(15, 0, '114', 'Darlehen Haben', 0, 'Bitte wählen', 2, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(16, 0, '115', 'Darlehen Soll', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(17, 0, '116', 'Ausleihungen', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(18, 0, '117', 'Anzahlungen 19% MwSt', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(19, 0, '118', 'Geldtransit', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(20, 0, '119', 'Verrechnungen', 0, 'Bitte wählen', 2, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(21, 0, '120', 'Schecks', 0, 'Bitte wählen', 2, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(22, 0, '121', 'Kasse', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(23, 0, '122', 'Bank 1', 100, 'Bitte wählen', 3, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(24, 0, '123', 'Bank 2', 100, 'Bitte wählen', 3, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(25, 0, '124', 'Wertpapiere', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(26, 0, '125', 'Umsatzsteuer 19%', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(27, 0, '126', 'Erlöse', 19, 'MwSt', 2, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(28, 0, '127', 'Erlöse Leergut', 19, 'MwSt', 2, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(29, 0, '128', 'Provisionumsätze', 19, 'MwSt', 2, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(30, 0, '129', 'Entnahme', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(31, 0, '130', 'Nachlässe', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(32, 0, '131', 'Erhaltene Skonti', 0, 'Bitte wählen', 2, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(33, 0, '132', 'Löhne', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(34, 0, '133', 'Gehälter', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(35, 0, '134', 'Aushilfslöhne', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(36, 0, '135', 'Sozial Abgaben', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(37, 0, '136', 'Abschreibungen', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(38, 0, '137', 'Sonstige Abgaben', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(39, 0, '138', 'Leasing', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(40, 0, '139', 'Raumkosten', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(41, 0, '140', 'Raumkosten MwSt 19%', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(42, 0, '141', 'Gas, Strom, Wasser', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(43, 0, '142', 'KFZ-Kosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(44, 0, '143', 'KFZ-Reparaturkosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(45, 0, '144', 'Laufende KFZ-Kosten', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(46, 0, '145', 'Mautgebühren', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(47, 0, '146', 'Werbekosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(48, 0, '147', 'Bewirtungskosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(49, 0, '148', 'Reisekosten', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(50, 0, '149', 'Porto', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(51, 0, '150', 'Telefonkosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(52, 0, '151', 'Telefon und Internetkosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(53, 0, '152', 'Bürobedarf', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(54, 0, '153', 'Zeitschrift Abo Kosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(55, 0, '154', 'Buchführungskosten', 19, 'MwSt', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(56, 0, '155', 'Nebenkosten', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(57, 0, '156', 'Nicht abziehebare Vorsteuer', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(58, 0, '157', 'KFZ-Steuer', 0, 'Bitte wählen', 1, 'root', '2011-03-21');
INSERT INTO ACCOUNTSTACKCD (ACCOUNTSTACKCDID, ACCOUNTSTACKID, CREDITDEBITNUMBER, CREDITDEBITNAME, CREDITDEBITVALUE, CREDITDEBITTAXTNAME, CREDITDEBITTYPE, CREATEDFROM, CREATEDDATE) VALUES(59, 0, '158', 'Steuer', 0, 'Bitte wählen', 1, 'root', '2011-03-21');

