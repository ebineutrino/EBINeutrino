package ebiNeutrinoSDK.gui.component;


public abstract class EBIGRCManagementListener {

    public abstract boolean addNewTaskAction(TaskEvent event);
    public abstract boolean editTaskAction(TaskEvent event, boolean isFullEdit);
    public abstract boolean deleteTaskAction(TaskEvent event);
    public abstract boolean createRelation(long fromId, long toId);

}
