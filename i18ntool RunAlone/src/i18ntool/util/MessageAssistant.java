/**
 * <pre>
 * This class is used for message folder, 
 * it will contains the the project folder when we change Resouce Locator
 * and for all the folder, replace "\" with "/"
 * 
 * </pre>
 * 
 * @author fernado
 * @date Jan 13, 2011
 */
package i18ntool.util;

import i18ntool.consts.Constant;
import i18ntool.consts.Preservation;
import iceworld.fernado.util.Utils;

public final class MessageAssistant {
	
	private static final MessageAssistant instance = new MessageAssistant();
	
	private MessageAssistant() {
		
	}
	public static final MessageAssistant getInstance() {
		return instance;
	}
	
	private String[] messageFiles;
	
	private boolean encoding;
	
	/**
	 * this is a flag for save empty value's key or not
	 * save configuration
	 */
	private Preservation preservation;
	
	/**
	 * customer resource folder
	 */
	private String messageFolder;
	
	private String[] messageCharsetNames;
	
    public String[] getMessageFiles() {
    	if (!Utils.isEmpty(messageFiles)) {
    		return messageFiles;
    	}
    	messageFiles = ResourceHandler.getInstance().getMessageFiles();
    	return messageFiles;
    }
    
    public String getMessageFolder() {
    	return messageFolder;
    }
    
    public void setMessageFolder(final String messageFolder) {
    	this.messageFolder = Utils.isEmpty(messageFolder) 
    		? messageFolder : Utils.replaceBacklashAsSlash(messageFolder);
    }
    
    public String getFolder() {
    	if (Utils.isEmpty(getMessageFolder())) {
    		return Utils.replaceBacklashAsSlash(SystemResource.getInstance().getProjectFolder());
    	} else if (Constant.RESOURCE_PATH.equals(getMessageFolder())) {
    		return Utils.replaceBacklashAsSlash(SystemResource.getInstance().getProjectFolder() + Constant.RESOURCE_PATH);
    	}
    	return getMessageFolder();
    }
    
	public boolean isEncoding() {
		return encoding;
	}

	public void setEncoding(final boolean encoding) {
		this.encoding = encoding;
	}
	
	public Preservation getPreservation() {
		return preservation;
	}
	
	public void setPreservation(Preservation preservation) {
		this.preservation = preservation;
	}
	
	public String[] getMessageCharsetNames() {
		return messageCharsetNames;
	}
	
	public void setMessageCharsetNames(String[] messageCharsetNames) {
		this.messageCharsetNames = messageCharsetNames;
	}
	
}
