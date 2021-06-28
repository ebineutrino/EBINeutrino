package org.modules.models;

import java.util.ArrayList;
import java.util.List;
import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;
import lombok.Getter;
import lombok.Setter;
import org.sdk.model.hibernate.Companycontacts;
import org.sdk.model.hibernate.Companymeetingcontacts;
import org.sdk.model.hibernate.Companyopportunitycontact;

public class ModelCRMContact extends AbstractTableModel {

    @Getter
    @Setter
    public List<Companycontacts> availableContacts = new ArrayList();

    @Getter
    @Setter
    public List<Companyopportunitycontact> availableOpportunityContacts = new ArrayList();

    @Getter
    @Setter
    public List<Companymeetingcontacts> availableMeetingContacts = new ArrayList();

    public static int CRM_CONTACT = 0;
    public static int OPPORTUNITY_CONTACT = 1;
    public static int MEETING_CONTACT = 2;

    private int type = 0;

    public ModelCRMContact(int type) {
        this.type = type;
    }

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_CONTACT_POSITION"),
        EBISystem.i18n("EBI_LANG_C_GENDER"),
        EBISystem.i18n("EBI_LANG_SURNAME"),
        EBISystem.i18n("EBI_LANG_C_CNAME"),
        EBISystem.i18n("EBI_LANG_C_TELEPHONE"),
        EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE"),
        EBISystem.i18n("EBI_LANG_EMAIL"),
        EBISystem.i18n("EBI_LANG_DESCRIPTION")
    };

    public Object getRow(final int row) {
        Object contactObject = null;
        if (type == CRM_CONTACT) {
            contactObject = availableContacts.get(row);
        } else if (type == OPPORTUNITY_CONTACT) {
            contactObject = availableOpportunityContacts.get(row);
        } else if (type == MEETING_CONTACT) {
            contactObject = availableMeetingContacts.get(row);
        }
        return contactObject;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        int count = 0;
        if (type == CRM_CONTACT) {
            count = availableContacts == null ? 0 : availableContacts.size();
        } else if (type == OPPORTUNITY_CONTACT) {
            count = availableOpportunityContacts == null ? 0 : availableOpportunityContacts.size();
        } else if (type == MEETING_CONTACT) {
            count = availableMeetingContacts == null ? 0 : availableMeetingContacts.size();
        }
        return count;
    }

    @Override
    public String getColumnName(final int col) {
        return columnNames[col];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(final int row, final int col) {

        Object contactObject = null;
        if (type == CRM_CONTACT) {
            contactObject = getValueAtForContact(row, col);
        } else if (type == OPPORTUNITY_CONTACT) {
            contactObject = getValueAtForOpportunityContact(row, col);
        } else if (type == MEETING_CONTACT) {
            contactObject = getValueAtForMeetingContact(row, col);
        }
        return contactObject;
    }

    public Integer getId(int row) {
        Integer id = 0;
        if (type == CRM_CONTACT) {
            id = getIdForContact(row);
        } else if (type == OPPORTUNITY_CONTACT) {
            id = getIdForOpportunityContact(row);
        } else if (type == MEETING_CONTACT) {
            id = getIdForMeetingContact(row);
        }
        return id;
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        return true;
    }

    @Override
    public void setValueAt(final Object value, final int row, final int col) {
        if (type == CRM_CONTACT) {
            setValueAtForContact(value, row, col);
        } else if (type == OPPORTUNITY_CONTACT) {
            setValueAtForOpportunityContact(value, row, col);
        } else if (type == MEETING_CONTACT) {
            setValueAtForMeetingContact(value, row, col);
        }
        EBISystem.canRelease = false;
        fireTableCellUpdated(row, col);
    }

    public Integer getIdForContact(int row) {
        Integer id = -1;
        if (availableContacts != null) {
            Companycontacts contact = availableContacts.get(row);
            if (contact != null) {
                id = contact.getContactid();
            }
        }
        return id;
    }

    public Integer getIdForOpportunityContact(int row) {
        Integer id = -1;
        if (availableOpportunityContacts != null) {
            Companyopportunitycontact contact = availableOpportunityContacts.get(row);
            if (contact != null) {
                id = contact.getOpportunitycontactid();
            }
        }
        return id;
    }

    public Integer getIdForMeetingContact(int row) {
        Integer id = -1;
        if (availableMeetingContacts != null) {
            Companymeetingcontacts contact = availableMeetingContacts.get(row);
            if (contact != null) {
                id = contact.getMeetingcontactid();
            }
        }
        return id;
    }

    public void setValueAtForContact(final Object value, final int row, final int col) {
        String ret = value.toString();
        Companycontacts contact = availableContacts.get(row);
        if (availableContacts != null) {
            if (contact != null) {
                if (col == 0) {
                    contact.setPosition(ret);
                } else if (col == 1) {
                    contact.setGender(ret);
                } else if (col == 2) {
                    contact.setSurname(ret);
                } else if (col == 3) {
                    contact.setName(ret);
                } else if (col == 4) {
                    contact.setPhone(ret);
                } else if (col == 5) {
                    contact.setMobile(ret);
                } else if (col == 6) {
                    contact.setEmail(ret);
                } else if (col == 7) {
                    contact.setDescription(ret);
                }
            }
        }
    }

    public void setValueAtForOpportunityContact(final Object value, final int row, final int col) {
        String ret = value.toString();
        if (availableOpportunityContacts != null) {
            Companyopportunitycontact contact = availableOpportunityContacts.get(row);
            if (contact != null) {
                if (col == 0) {
                    contact.setPosition(ret);
                } else if (col == 1) {
                    contact.setGender(ret);
                } else if (col == 2) {
                    contact.setSurname(ret);
                } else if (col == 3) {
                    contact.setName(ret);
                } else if (col == 4) {
                    contact.setPhone(ret);
                } else if (col == 5) {
                    contact.setMobile(ret);
                } else if (col == 6) {
                    contact.setEmail(ret);
                } else if (col == 7) {
                    contact.setDescription(ret);
                }
            }
        }
    }

    public void setValueAtForMeetingContact(final Object value, final int row, final int col) {
        String ret = value.toString();
        if (availableMeetingContacts != null) {
            Companymeetingcontacts contact = availableMeetingContacts.get(row);
            if (contact != null) {
                if (col == 0) {
                    contact.setPosition(ret);
                } else if (col == 1) {
                    contact.setGender(ret);
                } else if (col == 2) {
                    contact.setSurname(ret);
                } else if (col == 3) {
                    contact.setName(ret);
                } else if (col == 4) {
                    contact.setPhone(ret);
                } else if (col == 5) {
                    contact.setMobile(ret);
                } else if (col == 6) {
                    contact.setEmail(ret);
                } else if (col == 7) {
                    contact.setDescription(ret);
                }
            }
        }
    }

    public Object getValueAtForContact(int row, int col) {
        Object ret = "";
        if (availableContacts != null) {
            Companycontacts contact = availableContacts.get(row);
            if (contact != null) {
                if (col == 0) {
                    ret = contact.getPosition();
                } else if (col == 1) {
                    ret = contact.getGender();
                } else if (col == 2) {
                    ret = contact.getSurname();
                } else if (col == 3) {
                    ret = contact.getName();
                } else if (col == 4) {
                    ret = contact.getPhone();
                } else if (col == 5) {
                    ret = contact.getMobile();
                } else if (col == 6) {
                    ret = contact.getEmail();
                } else if (col == 7) {
                    ret = contact.getDescription();
                } else if (col == -1) {
                    ret = contact.getContactid();
                }
            }
        }
        return ret;
    }

    public Object getValueAtForOpportunityContact(int row, int col) {
        Companyopportunitycontact contact = availableOpportunityContacts.get(row);
        Object ret = null;

        if (contact != null) {
            if (col == 0) {
                ret = contact.getPosition();
            } else if (col == 1) {
                ret = contact.getGender();
            } else if (col == 2) {
                ret = contact.getSurname();
            } else if (col == 3) {
                ret = contact.getName();
            } else if (col == 4) {
                ret = contact.getPhone();
            } else if (col == 5) {
                ret = contact.getMobile();
            } else if (col == 6) {
                ret = contact.getEmail();
            } else if (col == 7) {
                ret = contact.getDescription();
            } else if (col == -1) {
                ret = contact.getOpportunitycontactid();
            }
        } else {
            ret = "";
        }
        return ret;
    }

    public Object getValueAtForMeetingContact(int row, int col) {
        Companymeetingcontacts contact = availableMeetingContacts.get(row);
        Object ret = null;

        if (contact != null) {
            if (col == 0) {
                ret = contact.getPosition();
            } else if (col == 1) {
                ret = contact.getGender();
            } else if (col == 2) {
                ret = contact.getSurname();
            } else if (col == 3) {
                ret = contact.getName();
            } else if (col == 4) {
                ret = contact.getPhone();
            } else if (col == 5) {
                ret = contact.getMobile();
            } else if (col == 6) {
                ret = contact.getEmail();
            } else if (col == 7) {
                ret = contact.getDescription();
            } else if (col == -1) {
                ret = contact.getMeetingcontactid();
            }
        } else {
            ret = "";
        }
        return ret;
    }
}
