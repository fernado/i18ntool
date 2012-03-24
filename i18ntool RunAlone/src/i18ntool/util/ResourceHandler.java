/**
 * This class is used to read the properties files.
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.util;

import i18ntool.consts.Constant;
import i18ntool.consts.Preservation;
import i18ntool.consts.Status;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;
import iceworld.fernado.ext.util.ExtUtils;
import iceworld.fernado.util.FileUtils;
import iceworld.fernado.util.MessageUtils;
import iceworld.fernado.util.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ResourceHandler {

	private static final String ID = ResourceHandler.class.getName();
	private static final Logger log = LogManager.getLogger(ID);

	private static final ResourceHandler handler = new ResourceHandler();
	private static Properties configFiles;
	private static final String messageKeys = "i18nFiles";
	private static final String messageFolder = "i18nFolder";
	private static final String normalSave = "i18nNormalSave";
	private static String messageFile = Constants.EMPTY_STRING;
	private boolean valid = false;
	private static String[] messageFiles;
	private static String[] messageFilenames;
	private List<INode> backupNodes = new ArrayList<INode>();

	private INode root;
	private INode searchResultRoot;
	
	private String mainConfigFile;

	public static ResourceHandler getInstance() {
		return handler;
	}

	private ResourceHandler() {
		init();
	}
	
	private void init() {
		log.log(Level.INFO, "i18ntool.util.ResourceHandler.init()");
		loadConfigFiles();
		setMessageFiles();
		setPreservation();
		checkValid();
		generateNode();
		generateRows();
	}
	
	public void refresh() {
		log.log(Level.INFO, "i18ntool.util.ResourceHandler.refresh()");
		loadConfigFiles();
		clear();
		
		setMessageFiles();
		checkValid();
		generateNode();
		generateRows();
		setBackupNodes();
	}
	
	private void loadConfigFiles() {
		mainConfigFile = Utils.replaceBacklashAsSlash(
				SystemResource.getInstance().getProjectFolder() + Constant.CONF_PATH + Constant.SYS_RESOURCE);
		try {
			configFiles = FileUtils.loadProperties(mainConfigFile);
		} catch (IOException e) {
			log.log(Level.SEVERE, "CANT LOAD THE FILE - " + mainConfigFile);
		}
	}
	
	public void saveFolderInConfigFiles() {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		Properties prop = new Properties();
		try {
			fis = new FileInputStream(mainConfigFile);
			prop.load(fis);
			fos = new FileOutputStream(mainConfigFile);
			String folder = Utils.replaceBacklashAsSlash(MessageAssistant.getInstance().getMessageFolder());
			prop.setProperty(messageFolder, folder);
			prop.store(fos, Constants.EMPTY_STRING);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "CANT FIND THE FILE OF " + mainConfigFile);
		} catch (IOException e) {
			log.log(Level.SEVERE, "WRITE ERROR - " + mainConfigFile);
		}
	}
	
	public void savePreservationInConfigFiles() {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		Properties prop = new Properties();
		try {
			fis = new FileInputStream(mainConfigFile);
			prop.load(fis);
			fos = new FileOutputStream(mainConfigFile);
			prop.setProperty(normalSave, MessageAssistant.getInstance().getPreservation().toString());
			prop.store(fos, Constants.EMPTY_STRING);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "CANT FIND THE FILE OF " + mainConfigFile);
		} catch (IOException e) {
			log.log(Level.SEVERE, "WRITE ERROR - " + mainConfigFile);
		}
	}
	
	private void clear() {
		valid = false;
		messageFiles = new String[0];
		messageFilenames = new String[0];
		List<INode> rootChildren = root.getChildren();
		for (INode node: rootChildren) {
			((TreeNode) node).removeAllChildren();
		}
		((TreeNode) root).removeAllChildren();
		backupNodes.clear();
	}

	public void setBackupNodes() {
		Collections.copy(root.getChildren(), backupNodes);
	}

	public List<INode> getBackupNodes() {
		return backupNodes;
	}

	public final void setMessageFiles() {
		String value = configFiles.getProperty(messageKeys);
		MessageAssistant.getInstance().setMessageFolder(configFiles.getProperty(messageFolder));
		if (null != value) {
			messageFiles = value.split(",");
		} else {
			messageFiles = new String[0];
		}
	}
	
	public final String[] getMessageFiles() {
		return messageFiles;
	}
	
	public void setPreservation() {
		String value = configFiles.getProperty(normalSave);
		Preservation preservation = Preservation.UNKNOWN;
		if (!Utils.isEmpty(value)) {
			preservation = Preservation.parsePreservation(value);
		}
		MessageAssistant.getInstance().setPreservation(preservation);
	}

	/**
	 * check the i18n files are valid or not
	 */
	private final void checkValid() {
		String[] elements = getMessageFiles();
		messageFilenames = new String[getMessageFiles().length];
		if (elements.length > 0) {
			messageFile = elements[0].substring(0, elements[0].lastIndexOf(".properties"));
			messageFilenames[0] = messageFile;
			if (elements.length > 1) {
				for (int i = 1; i < elements.length; i++) {
					String element = elements[i].substring(0, elements[i]
							.lastIndexOf(".properties"));
					if (element.contains(messageFile)) {
						messageFilenames[i] = element;
						valid = true;
					} else {
						valid = false;
					}
				}
			}
		}
		if (false == valid) {
			log.log(Level.FINE, "message files is invalid");
		}
	}

	public final boolean isValid() {
		return valid;
	}

	/**
	 * load the properties values by keys
	 * and set to the TreeNode Object
	 */
	private void generateRows() {
		List<INode> rootChildren = root.getChildren();
		String filePath = Constants.EMPTY_STRING;
		String[] messageCharsetNames = new String[getMessageFiles().length]; 
		try {
			for (int i = 0; i < getMessageFiles().length; i++) {
				filePath = MessageAssistant.getInstance().getFolder() + messageFiles[i];
				String charsetName = ExtUtils.decodeFile(filePath);
				messageCharsetNames[i] = charsetName;
				MessageAssistant.getInstance().setMessageCharsetNames(messageCharsetNames);
				Map<String, String> propertyMap = FileUtils.loadPropertiesToMap(filePath, charsetName, i);
				for (INode child : rootChildren) {
					Map<String, ValueEntity> valueMap = ((TreeNode) child).getValueMap();
					ValueEntity valueEntity = new ValueEntity();
					if (null != propertyMap) {
						if (null == propertyMap.get(child.getName())) {
							valueEntity.setCurrent(null);
							valueEntity.setOriginal(null);
							valueMap.put(messageFiles[i], valueEntity);
						} else {
							String value = MessageUtils.ascii2Native(propertyMap.get(child.getName()));
							valueEntity.setCurrent(value);
							valueEntity.setOriginal(value);
							valueMap.put(messageFiles[i], valueEntity);
						}
					} else {
						valueEntity.setCurrent(null);
						valueEntity.setOriginal(null);
						valueMap.put(messageFiles[i], valueEntity);
					}
					((TreeNode) child).setValueMap(valueMap);
					if (0 == i) {
						String comment = propertyMap.get(Utils.commentOfKey(child.getName()));
						((TreeNode) child).setComment(comment);
					}
				}
			}
			for (INode child : rootChildren) {
				if (containsEmpty(((TreeNode) child).getValueMap())) {
					((TreeNode) child).setStatus(Status.LOAD_NEED_COMPLETE);
				}
			}
		} catch (FileNotFoundException e1) {
			log.log(Level.WARNING, "CANT LOAD PROPERTIES FROM THE FILE -" + filePath);
		} catch (IOException e1) {
			log.log(Level.WARNING, "CANT LOAD PROPERTIES FROM THE FILE -" + filePath);
		}
	}
	
	/**
	 * if map contains empty or null value, return true
	 * @param map
	 * @return
	 */
	public static boolean containsEmpty(final Map<String, ValueEntity> map) {
		for (Map.Entry<String, ValueEntity> me: map.entrySet()) {
			if (isEmpty(me.getValue())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEmpty(final ValueEntity valueEntity) {
		return (null == valueEntity || Utils.isEmpty(valueEntity.getCurrent()));
	}
	

	/**
	 * 
	 * lang country
	 * 
	 * @param str
	 *            _en_MY
	 * @return ["en", "MY"]
	 */
	public String[] parseLangAndCountry(String str) {
		StringTokenizer st = new StringTokenizer(str, Constants.UNDER_LINE);
		List<String> result = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String element = st.nextToken();
			if (!Constants.EMPTY_STRING.equals(element)) {
				result.add(element);
			}
		}
		return (String[]) result.toArray(new String[0]);
	}

	/**
	 * load the default property file to get the property keys
	 */
	private void generateNode() {
		List<String> list = new ArrayList<String>();
		root = new TreeNode(Constant.ROOT_KEY, Type.ROOT, Status.NORMAL);
		searchResultRoot = new TreeNode(Constant.ROOT_KEY, Type.ROOT, Status.NORMAL);
		
		Map<String, String> propertyMap = null;
		String filePath = MessageAssistant.getInstance().getFolder() + messageFiles[0];
		try {
			String charsetName = ExtUtils.decodeFile(filePath);
			propertyMap = FileUtils.loadPropertiesToMap(filePath, charsetName, 0);
		} catch (IOException e) {
			log.log(Level.WARNING, e.getMessage());
			log.log(Level.WARNING, "CANT LOAD PROPERTIES FROM THE FILE -" + filePath);
		}
		if (null != propertyMap) {
			for (Map.Entry<String, String> me: propertyMap.entrySet()) {
				if (!me.getKey().endsWith(Constants.COMMENT_SUFFIX)) {
					String keyComment = Utils.commentOfKey(me.getKey());
					if (!me.getKey().equals(keyComment)) {
						TreeNode node = new TreeNode(me.getKey(), Type.LEAF, Status.NORMAL);
						if (null != propertyMap.get(keyComment)) {
							node.setComment(propertyMap.get(keyComment));
						}
						root.addChild(node);
						list.add(node.getName());
					}
				}
			}
		}
	}

	public INode getNode() {
		return root;
	}

	public INode getSearchResultRoot() {
		return searchResultRoot;
	}
	
}
