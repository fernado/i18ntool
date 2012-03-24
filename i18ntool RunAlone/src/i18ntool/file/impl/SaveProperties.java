/**
 * This is a save file class,
 * it has a mutithread to save the properties files.
 * so each thread will operate each property file.
 * 
 * currently can save properties by the order, and there is no error
 * again. using join() to solve this issue.
 * @author fernado  
 * @date 28/08/2010
 */
package i18ntool.file.impl;

import i18ntool.consts.Constant;
import i18ntool.consts.Filter;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import i18ntool.util.LogManager;
import i18ntool.util.MessageAssistant;
import i18ntool.util.NodeAssistant;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;
import iceworld.fernado.file.ISaveResource;
import iceworld.fernado.util.FileUtils;
import iceworld.fernado.util.MessageUtils;
import iceworld.fernado.util.SortedProperties;
import iceworld.fernado.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;


public class SaveProperties implements ISaveResource {

	private static final String ID = SaveProperties.class.getName();
	private static final Logger log = LogManager.getLogger(ID);
	
	private Filter filter;
	private String folder;
	private String comment;
	private String currentTime = Constants.EMPTY_STRING;
	
	private Set<String> wantedKeys = new HashSet<String>();

	public SaveProperties(final String folder, final Filter filter, final String comment) {
		this.folder = folder;
		this.filter = filter;
		this.comment = comment;
	}
	
	@Override
	public void removeResources() throws IOException {
		Utils.removeFiles(folder);
	}
	
	@Override
	public void saveResources() throws IOException, URISyntaxException {
		String[] messageFiles = MessageAssistant.getInstance().getMessageFiles();
		log.log(Level.INFO, "SAVE PROPERTIES TO FOLDER -- " + folder);
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(Display
				.getCurrent().getActiveShell());
		currentTime = new Date().toString();
		
		try {
			progressMonitorDialog.run(false, true, new SaveProgress(messageFiles,
					folder, MessageAssistant.getInstance().isEncoding()));
		} catch (InvocationTargetException e) {
			log.log(Level.WARNING, "SAVE PROPERTIES TO FILE -- INVOCATION TARGET EXCEPTION");
		} catch (InterruptedException e) {
			log.log(Level.WARNING, "SAVE PROPERTIES TO FILE -- INTERRUPTED EXCEPTION");
		}
	}
	
	/**
	 * 
	 * add wantedkeys to a map
	 * 
	 * @param keys
	 * @param str
	 * @return <key, value>
	 */
	private Map<String, String> addWantedKeys(final Set<String> keys, final String str) {
		List<INode> list = NodeAssistant.getInstance().getData().getChildren();
		Map<String, String> resultMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			INode node = list.get(i);
			String name = node.getName();
			Map<String, ValueEntity> map = ((TreeNode) node).getValueMap();
			for (String key : keys) {
				if (Utils.isSame(name, key)) {
					resultMap.put(key, map.get(str).getCurrent());
				}
			}
		}
		return resultMap;
	}

	/**
	 * Add wantedkeys to a file, which will contain all the key=value
	 * 
	 * @param str
	 * @param filePath
	 * @param charsetName
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void addExtProperties(final String str, final String filePath, final String charsetName)
			throws IOException, URISyntaxException {
		log.log(Level.INFO, "addExtProperties -- ");
		Map<String, String> map = addWantedKeys(wantedKeys, str);
		SortedProperties prop = new SortedProperties();
		Reader reader = null;
		Writer writer = null;
		String lFilePath = Utils.replaceBacklashAsSlash(filePath);
		Utils.createFolderAndFile(MessageAssistant.getInstance().getFolder(), lFilePath);
		try {
			reader = new InputStreamReader(new FileInputStream(lFilePath), charsetName);
			prop.load(reader);
			writer = new OutputStreamWriter(new FileOutputStream(lFilePath), charsetName);
			for (Map.Entry<String, String> me : map.entrySet()) {
				prop.setProperty(me.getKey(), me.getValue());
			}
			prop.store(writer, Constants.EMPTY_STRING);
		} finally {
			if (null != writer) {
				writer.close();
			}
			if (null != reader) {
				reader.close();
			}
		}
	}
	
	private void writeProperties(final String filePath, final int index, final boolean encoding) throws IOException, URISyntaxException {
		log.log(Level.INFO, "SAVE PROPERTIES TO FILE -- " + filePath);
		if (encoding) {
			writePropertiesWithWriter(filePath, index, MessageAssistant.getInstance().getMessageCharsetNames()[index]);
		} else {
			writePropertiesWithWriter(filePath, index, Constants.CHARSET_UTF_8);
		}
	}
	
	/**
	 * The writer is Writer Object
	 * @param filePath   file path, /i18nTool/resource/*.properties (message.properties, message_en_MY.properties)
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void writePropertiesWithWriter(final String filePath, final int index, final String charsetName) throws IOException,
			URISyntaxException {
		String lfilePath = Utils.replaceBacklashAsSlash(filePath);
		Utils.createFolderAndFile(MessageAssistant.getInstance().getFolder(), lfilePath);
		File file = new File(lfilePath);
		boolean need = false;
		Map<String, String> keyValue = filderRemoveUneedkeyValue(lfilePath);

		need = writeProperties(keyValue, file);
		
		StringBuilder builder = new StringBuilder();
		builder.append(addDefaultHeaderComment(file.getName()));
		builder.append(getKeyValue(keyValue, index));
		FileUtils.writeStringUsingCharsetToFile(filePath, builder.toString(), charsetName);
		
		if (!need) {
			file.delete();
		}
	}
	
	private boolean writeProperties(Map<String, String> keyValue, final File file) throws IOException {
		boolean need = false;
		switch (filter) {
		case NORMAL:
			need = writePropertiesNormal(keyValue, file.getName());
			break;
		case CHANGED_OR_EMPTY:
			writePropertiesChangedOrEmpty(keyValue, file.getName());
			break;
		case CHANGED:
			writePropertiesChanged(keyValue, file.getName());
			break;
		case EMPTY:
			writePropertiesEmpty(keyValue, file.getName());
			break;
		case NOT_EMPTY_VALUE:
			need = writePropertiesNotEmpty(keyValue, file.getName());
			break;
		case NOT_EMPTY_VALUE_BUT_ALLOW_ORIGINAL_VALUE_EMPTY:
			need = writePropertiesNotEmptyButAllowOriginalEmpty(keyValue,
					file.getName());
			break;
		default:
			break;
		}
		return need;
	}
	
	private String addDefaultHeaderComment(final String filename) {
		StringBuilder result = new StringBuilder();
		result.append(Constants.POUND).append(filename).append(Utils.getLF());
		result.append(Constants.POUND).append(currentTime).append(Utils.getLF());
		return result.toString();
	}
	
	private void addKeyComment(StringBuilder builder, final String key) {
		String comment = NodeAssistant.getInstance().getKeyComment(key);
		if (!Utils.isEmpty(comment)) {
			String[] subComments = comment.split(Utils.getLF());
			if (!Utils.isEmpty(subComments)) {
				for (String sub: subComments) {
					builder.append(Constants.POUND).append(sub).append(Utils.getLF());
				}
			}
		}
	}
	
	private String getKeyValue(Map<String, String> propertyMap, final int index) {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, String> me: propertyMap.entrySet()) {
			if (0 == index) {
				addKeyComment(result, me.getKey());
			}
			result.append(me.getKey()).append(Constants.EQUAL).append(me.getValue()).append(Utils.getLF());
		}
		return result.toString();
	}
	
	private Map<String, String> organizeProperties(Properties props) {
		Map<String, String> result = new TreeMap<String, String>();
		
		for (Entry<Object, Object> entry: props.entrySet()) {
			result.put((String) (entry.getKey()), (String) (entry.getValue()));
		}
		return result;
	}
	
	private Map<String, String> filderRemoveUneedkeyValue(final String filepath) throws IOException {
		Map<String, String> result = null;
		SortedProperties prop = new SortedProperties();
		InputStream fis = null;
		try {
			fis = new FileInputStream(filepath);
			prop.load(fis);
			result = organizeProperties(prop);
			filterRemoveUneedKeyValue(result);
		} finally {
			if (null != fis) {
				fis.close();
			}
		}
		return result;
	}
	
	private void filterRemoveUneedKeyValue(Map<String, String> map) {
		for (INode node: NodeAssistant.getInstance().getRemovedNodes()) {
			map.remove(node.getName());
		}
	}

	/**
	 * Write properties when the node's status is normal
	 * @param propertyMap
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private boolean writePropertiesNormal(Map<String, String> propertyMap, final String filename) throws IOException {
		boolean result = false;
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (Type.LEAF == node.getType()) {
				Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
				if (valueMap.isEmpty()) {
					propertyMap.put(node.getName(), Constants.EMPTY_STRING);
				} else if (null == valueMap.get(filename)
						|| Utils.isEmpty(valueMap.get(filename).getCurrent())) {
					propertyMap.put(node.getName(), Constants.EMPTY_STRING);
				} else {
					if (MessageAssistant.getInstance().isEncoding()) {
						propertyMap.put(node.getName(), MessageUtils.native2Ascii(valueMap.get(filename).getCurrent()));
					} else {
						propertyMap.put(node.getName(), valueMap.get(filename).getCurrent());
					}
				}
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 
	 * Write properties when the node's status is changed or empty
	 * 
	 * and if find the node's status is changed or empty
	 * and node's map <filename, status> is changed or empty,
	 * then append it's name to the wantedKeys
	 * 
	 * @param propertyMap
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private boolean writePropertiesChangedOrEmpty(Map<String, String> propertyMap, final String filename) throws IOException {
		boolean result = false;
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (Type.LEAF == node.getType()) {
				if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.LOAD_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()) {
					Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
					if (valueMap.isEmpty()) {
						propertyMap.put(node.getName(), Constants.EMPTY_STRING);
						wantedKeys.add(node.getName());
						result = true;
					} else if (null == valueMap.get(filename)
							|| Utils.isEmpty(valueMap.get(filename).getCurrent())) {
						propertyMap.put(node.getName(), Constants.EMPTY_STRING);
						wantedKeys.add(node.getName());
						result = true;
					} else {
						if (null != ((TreeNode) node).getStatusMap().get(filename)) {
							if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatusMap().get(filename)
									|| i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatusMap().get(filename)
									|| i18ntool.consts.Status.LOAD_NEED_COMPLETE == ((TreeNode) node).getStatusMap().get(filename)
									|| i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()) {
								propertyMap.put(node.getName(), valueMap.get(filename).getCurrent());
								wantedKeys.add(node.getName());
								result = true;
							}
						}
					}
					if (MessageAssistant.getInstance().isEncoding()) {
						propertyMap.put(node.getName(), MessageUtils.native2Ascii(valueMap.get(filename).getCurrent()));
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * Write properties when the node's status is changed
	 * 
	 * and if find the node's status is changed and node's map <filename, status> is changed,
	 * then append it's name to the wantedKeys
	 * 
	 * @param propertyMap
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private boolean writePropertiesChanged(Map<String, String> propertyMap, final String filename) throws IOException {
		boolean result = false;
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (Type.LEAF == node.getType()) {
				if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatus()) {
					Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
					if (MessageAssistant.getInstance().isEncoding()) {
						propertyMap.put(node.getName(), MessageUtils.native2Ascii(valueMap.get(filename).getCurrent()));
					} else {
						if (null != ((TreeNode) node).getStatusMap().get(filename)) {
							if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatusMap().get(filename)) {
								propertyMap.put(node.getName(), valueMap.get(filename).getCurrent());
								wantedKeys.add(node.getName());
								result = true;
							}
							if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatusMap().get(filename)) {
								propertyMap.put(node.getName(), valueMap.get(filename).getCurrent());
							} else if (i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatusMap().get(filename)) {
								propertyMap.put(node.getName(), Constants.EMPTY_STRING);
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write properties when the node's status is empty
	 * 
	 * and if find the node's status is empty, append it's name to the wantedKeys
	 * 
	 * @param propertyMap
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private boolean writePropertiesEmpty(Map<String, String> propertyMap, final String filename) throws IOException {
		boolean result = false;
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (Type.LEAF == node.getType()) {
				if (i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.LOAD_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()) {
					Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
					if (valueMap.isEmpty()) {
						propertyMap.put(node.getName(), Constants.EMPTY_STRING);
						wantedKeys.add(node.getName());
						result = true;
					} else if (null == valueMap.get(filename)
							|| Utils.isEmpty(valueMap.get(filename).getCurrent())) {
						propertyMap.put(node.getName(), Constants.EMPTY_STRING);
						wantedKeys.add(node.getName());
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Write properties when the node's status is not empty
	 * 
	 * and if find the node's status is not empty, append it's name to the wantedKeys
	 * 
	 * @param propertyMap
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private boolean writePropertiesNotEmpty(Map<String, String> propertyMap, final String filename) throws IOException {
		boolean result = false;
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (Type.LEAF == node.getType()) {
				Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
				if (null != valueMap.get(filename) 
						&& !Utils.isEmpty(valueMap.get(filename).getCurrent()) 
						&& MessageAssistant.getInstance().isEncoding()) {
					propertyMap.put(node.getName(), MessageUtils.native2Ascii(valueMap.get(filename).getCurrent()));
				} else {
					propertyMap.remove(node.getName());
				}
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Write properties when the node's status is not empty but old node's status is empty
	 * 
	 * and if find the node's status is not empty, append it's name to the wantedKeys
	 * 
	 * @param propertyMap
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private boolean writePropertiesNotEmptyButAllowOriginalEmpty(Map<String, String> propertyMap, final String filename) throws IOException {
		boolean result = false;
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			if (Type.LEAF == node.getType()) {
				Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
				if (null != valueMap.get(filename) 
						&& !Utils.isEmpty(valueMap.get(filename).getCurrent())
						|| Constants.EMPTY_STRING.equals(valueMap.get(filename).getOriginal())
						&& MessageAssistant.getInstance().isEncoding()) {
					propertyMap.put(node.getName(), MessageUtils.native2Ascii(valueMap.get(filename).getCurrent()));
				} else {
					propertyMap.remove(node.getName());
				}
				result = true;
			}
		}
		return result;
	}
	
	private class SaveProgress implements IRunnableWithProgress {

		private String[] messageFiles;
		private String folder;
		private boolean encoding;

		public SaveProgress(String[] messageFiles, String folder, boolean encoding) {
			this.messageFiles = messageFiles;
			this.folder = folder;
			this.encoding = encoding;
		}
            
		public void run(IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
			monitor.beginTask("Saving Properties to File", IProgressMonitor.UNKNOWN);
			Job job = new ActivateJob("Saving Properties to File");
			job.addJobChangeListener(new ActivateJobChangeAdapter());
			job.setUser(true);
			job.schedule();
			job.join();
			if (monitor.isCanceled()) {
				throw new InterruptedException("The long running operation was cancelled");
			}
		}

		private class ActivateJob extends Job {
			public ActivateJob(String name) {
				super(name);
			}

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				int work = messageFiles.length;
				double workRate = initWorkRate(work);
				IStatus result = null;
				try {
					for (int index = 0; index < messageFiles.length; index++) {
						String path = folder + messageFiles[index];
						writeProperties(path, index, encoding);
						monitor.worked(getWorked(workRate, index));
					}
					if (!Utils.isEmpty(comment)) {
						addExtProperties(comment, folder + Constant.ASSIST_FILE, Constants.CHARSET_UTF_8);
						monitor.worked((int) workRate * (work + 1));
					}
					result = new Status(Status.OK, "OK", "OK");
				} catch (Exception ex) {
					result = new Status(Status.ERROR, "", "FAIL IN WRITE PROPERTIES FILES");
				}
				return result;
			}
			
			private double initWorkRate(int work) {
				return !Utils.isEmpty(comment) ? (100 / (work + 1)) : (100 / work);
			}
			
			private int getWorked(double workRate, int index) {
				if (!Utils.isEmpty(comment)) {
					return ((int) workRate * (index + 1));
				}
				return ((int) workRate * index);
			}
		}

		private class ActivateJobChangeAdapter extends JobChangeAdapter {

			@Override
			public void done(final IJobChangeEvent event) {
				IStatus iStatus = event.getResult();
				String message = iStatus.getMessage();

				if (IStatus.OK != iStatus.getSeverity()) {
					log.log(Level.WARNING, "This action's status :" + message);
				}
			}
		}
	}

}
