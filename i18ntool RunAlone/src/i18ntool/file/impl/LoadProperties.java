/**
 * This is a load file class,
 * 
 * @author fernado  
 * @date 28/08/2010
 */
package i18ntool.file.impl;

import i18ntool.consts.Constant;
import i18ntool.consts.Status;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import i18ntool.util.MessageAssistant;
import i18ntool.util.NodeAssistant;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;
import iceworld.fernado.ext.util.ExtUtils;
import iceworld.fernado.file.ILoadResource;
import iceworld.fernado.util.FileUtils;
import iceworld.fernado.util.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

public class LoadProperties implements ILoadResource {

	private static final String ID = LoadProperties.class.getName();
	private static final Logger log = Logger.getLogger(ID);
	
	private INode root;
	private String i18nFileFolder;
	
	public LoadProperties(final String i18nFileFolder) {
		this.i18nFileFolder = i18nFileFolder;
		root = NodeAssistant.getInstance().getData();
	}
	
	@Override
	public boolean readResources() {
		NodeAssistant.getInstance().clearRemovedNodes();
		String filePath = MessageAssistant.getInstance().getFolder() + i18nFileFolder;
		String[] files = Utils.getFilesWithSuffix(filePath, Constant.MESSAGE_SUFFIX);
		if (Utils.isEmpty(files)) {
			return true;
		}
		String[] messages = MessageAssistant.getInstance().getMessageFiles();
		Map<Integer, String> map = new TreeMap<Integer, String>();
		
		for (int i = 0; i < messages.length; i++) {
			for (int j = 0; j < files.length; j++) {
				if (messages[i].equals(files[j])) {
					map.put(i, messages[i]);
					break;
				}
			}
		}
		
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(Display
				.getCurrent().getActiveShell());
		try {
			progressMonitorDialog.run(false, true, new LoadProgress(filePath, map, MessageAssistant.getInstance().isEncoding()));
		} catch (InvocationTargetException e) {
			log.log(Level.WARNING, "Save Properties to File -- Invocation Target");
		} catch (InterruptedException e) {
			log.log(Level.WARNING, "Save Properties to File -- Interrupted");
		}
		
		return false;
	}
	
	private void readProperties(final String filePath, final int index, final boolean encoding) {
		Map<String, String> propertyMap = null;
		File file = new File(filePath);
		try {
			if (encoding) {
				propertyMap = FileUtils.loadPropertiesToMap(filePath, index);
			} else {
				propertyMap = FileUtils.loadPropertiesToMap(filePath, ExtUtils.decodeFile(file), index);
			}
		} catch (IOException e) {
			log.log(Level.WARNING, "CANT LOAD PROPERTIES FROM THE FILE -" + filePath);
		}
		if (null != propertyMap) {
			
			for (Map.Entry<String, String> me: propertyMap.entrySet()) {
				String key = me.getKey();
				String value = me.getValue();
				INode child = root.getChild(key);
				if (null != child) {
					Map<String, ValueEntity> valueMap = ((TreeNode) child).getValueMap();
					ValueEntity valueEntity = valueMap.get(MessageAssistant.getInstance().getMessageFiles()[index]);
					valueEntity.setCurrent(value);
					valueEntity.setOriginal(value);
					valueMap.put(file.getName(), valueEntity);
					((TreeNode) child).setValueMap(valueMap);
					if (Utils.isEmpty(value)) {
						((TreeNode) child).setStatus(Status.LOAD_NEED_COMPLETE);
						Map<String, Status> statusMap = ((TreeNode) child).getStatusMap();
						statusMap.put(file.getName(), Status.LOAD_NEED_COMPLETE);
					} else {
						((TreeNode) child).setStatus(Status.NORMAL);
					}
				// only the messages.properties file can contain the new key, others will never add to the root, just can update
				} else if (index == 0) {
					INode node = new TreeNode(key, Type.LEAF, Status.NORMAL);
					Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
					ValueEntity valueEntity = valueMap.get(MessageAssistant.getInstance().getMessageFiles()[index]);
					valueEntity.setCurrent(value);
					valueEntity.setOriginal(value);
					valueMap.put(file.getName(), valueEntity);
					String comment = propertyMap.get(Utils.commentOfKey(node.getName()));
					((TreeNode) node).setValueMap(valueMap);
					((TreeNode) node).setComment(comment);
					root.addChild(node);
					NodeAssistant.getInstance().addTreeNodeChangeListener(node);
				}
			}
		}
	}
	
	private class LoadProgress implements IRunnableWithProgress {
		
		private boolean encoding;
		private String filePath;
		private Map<Integer, String> map;
		
		public LoadProgress(final String filePath, final Map<Integer, String> map, boolean encoding) {
			this.encoding = encoding;
			this.filePath = filePath;
			this.map = map;
		}

		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
			for (Map.Entry<Integer, String> me: map.entrySet()) {
				readProperties(filePath + me.getValue(), me.getKey(), encoding);
			}
		}

	}
}
